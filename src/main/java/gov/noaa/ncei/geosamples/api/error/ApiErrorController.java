package gov.noaa.ncei.geosamples.api.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApiErrorController implements ErrorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiErrorController.class);

  @RequestMapping("/error")
  public String error(HttpServletRequest request) {
    Object exObj = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    if (exObj instanceof Exception) {
      LOGGER.debug("Web Exception", (Exception) exObj);
    }

    if (exObj instanceof ApiException) {
      throw (ApiException) exObj;
    }

    if (exObj instanceof Exception && ((Exception) exObj).getCause() instanceof ApiException) {
      throw (ApiException) ((Exception) exObj).getCause();
    }

    Object codeObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (codeObj instanceof Integer) {
      HttpStatus status = HttpStatus.resolve((Integer)codeObj);
      if(status == null) {
        status = HttpStatus.INTERNAL_SERVER_ERROR;
      }
      throw new ApiException(status, ApiError.builder().error(status.getReasonPhrase()).build());
    }

    if (exObj instanceof RuntimeException) {
      throw (RuntimeException) exObj;
    }
    if (exObj instanceof Exception) {
      throw new RuntimeException("An error occurred processing request", (Exception) exObj);
    }
    throw new IllegalStateException("An error occurred processing request");
  }

}
