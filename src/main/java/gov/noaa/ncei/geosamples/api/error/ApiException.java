package gov.noaa.ncei.geosamples.api.error;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

  private final ApiError apiError;
  private final HttpStatus httpStatus;

  public ApiException(HttpStatus httpStatus, ApiError apiError) {
    super();
    this.httpStatus = httpStatus;
    this.apiError = apiError;
  }

  public ApiException(String message, HttpStatus httpStatus, ApiError apiError) {
    super(message);
    this.httpStatus = httpStatus;
    this.apiError = apiError;
  }

  public ApiException(String message, Throwable cause, HttpStatus httpStatus, ApiError apiError) {
    super(message, cause);
    this.httpStatus = httpStatus;
    this.apiError = apiError;
  }

  public ApiException(Throwable cause, HttpStatus httpStatus, ApiError apiError) {
    super(cause);
    this.httpStatus = httpStatus;
    this.apiError = apiError;
  }

  public ApiError getApiError() {
    return apiError;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
