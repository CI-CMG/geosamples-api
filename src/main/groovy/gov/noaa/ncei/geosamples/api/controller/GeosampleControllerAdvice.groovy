package gov.noaa.ncei.geosamples.api.controller

import gov.noaa.ncei.geosamples.api.error.GeosamplesResourceNotFoundException
import gov.noaa.ncei.geosamples.api.error.GeosampleNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GeosampleControllerAdvice {

    /**
     * specific Geosample resource not found exception. Error essage defined in the exception
     */
    @ResponseBody
    @ExceptionHandler(GeosampleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String geosampleNotFoundHandler(GeosampleNotFoundException ex) {
        return ex.getMessage();
    }

    /**
     * general resource not found exception. Error message defined in the calling method
     */
    @ResponseBody
    @ExceptionHandler(GeosamplesResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String geosamplesResourceNotFoundHandler(GeosamplesResourceNotFoundException ex) {
        return ex.getMessage();
    }

}

