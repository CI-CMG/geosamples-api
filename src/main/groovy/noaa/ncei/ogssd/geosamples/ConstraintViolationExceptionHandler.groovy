/*
credit to @eugenp (https://github.com/eugenp/tutorials/blob/master/spring-web-modules/spring-mvc-xml/src/main/java/com/baeldung/spring/controller/ConstraintViolationExceptionHandler.java)
 */
package noaa.ncei.ogssd.geosamples

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;


@ControllerAdvice
public class ConstraintViolationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = [ConstraintViolationException.class])
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}