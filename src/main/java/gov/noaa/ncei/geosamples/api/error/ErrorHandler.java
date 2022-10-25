package gov.noaa.ncei.geosamples.api.error;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import gov.noaa.ncei.geosamples.api.error.ApiError.ApiErrorBuilder;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return handleExceptionInternal(
        ex,
        ApiError.builder().error(String.format("Missing Request Parameter '%s'", ex.getParameterName())).build(),
        headers,
        status,
        request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    String name = ex.getPropertyName();
    if (!StringUtils.hasText(name)) {
      if (ex instanceof MethodArgumentTypeMismatchException) {
        name = ((MethodArgumentTypeMismatchException) ex).getName();
      }
    }
    return handleExceptionInternal(
        ex,
        ApiError.builder().error(String.format("Invalid Parameter '%s'", name)).build(),
        headers,
        status,
        request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ApiErrorBuilder errorBuilder = ApiError.builder().error("Invalid Request");
    ex.getBindingResult().getFieldErrors().forEach(error -> errorBuilder.fieldError(error.getField(), error.getDefaultMessage()));
    return handleExceptionInternal(ex, errorBuilder.build(), headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    if (ex.getCause() instanceof JsonMappingException) {
      @SuppressWarnings("unchecked") ResponseEntity<Object> responseEntity = (ResponseEntity) handleJsonMappingException(
          (JsonMappingException) ex.getCause());
      return responseEntity;
    }
    return super.handleHttpMessageNotReadable(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    LOGGER.debug("Unable to process request", ex);
    if (body == null) {
      body = ApiError.builder().error(status.getReasonPhrase()).build();
    }
    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  @ExceptionHandler(JsonMappingException.class)
  public ResponseEntity<ApiError> handleJsonMappingException(JsonMappingException ex) {
    LOGGER.debug("Unable to process JSON", ex);
    return new ResponseEntity<>(
        ApiError.builder().error("Invalid Request").fieldError(toPath(ex.getPath()), "Invalid Type").build(),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  private static String toPath(List<Reference> pathParts) {
    StringBuilder pathBuilder = new StringBuilder();
    for (int i = 0; i < pathParts.size(); i++) {
      Reference reference = pathParts.get(i);
      if (reference.getIndex() >= 0) {
        pathBuilder.append("[").append(reference.getIndex()).append("]");
      } else {
        if (i != 0) {
          pathBuilder.append(".");
        }
        pathBuilder.append(reference.getFieldName());
      }
    }
    return pathBuilder.toString();
  }


  private static ApiErrorBuilder buildFromViolations(Set<ConstraintViolation<?>> violations, boolean raw) {
    ApiErrorBuilder errorBuilder = ApiError.builder().error("Invalid Request");
    for (ConstraintViolation<?> violation : violations) {
      String path = violation.getPropertyPath().toString();
      if (!raw) {
        String[] parts = path.split("\\.", 2);
        if (parts.length > 1) {
          path = parts[1];
        }
      }
      errorBuilder.fieldError(path, violation.getMessage());
    }
    return errorBuilder;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
    LOGGER.debug("Controller rejected request", ex);
    ApiErrorBuilder errorBuilder = buildFromViolations(ex.getConstraintViolations(), false);
    return new ResponseEntity<>(errorBuilder.build(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiError> handleApiError(ApiException ex) {
    LOGGER.debug("Controller rejected request", ex);
    return new ResponseEntity<>(ex.getApiError(), ex.getHttpStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> fallbackErrorHandler(Exception e) {
    LOGGER.error("An error occurred while processing request", e);
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    if (e.getCause() instanceof HttpMediaTypeNotAcceptableException) {
      status = HttpStatus.NOT_ACCEPTABLE;
    }
    return new ResponseEntity<>(ApiError.builder().error(status.getReasonPhrase()).build(), status);
  }
}
