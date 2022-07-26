package gov.noaa.ncei.geosamples.api.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(value = HttpStatus.NOT_FOUND)
class GeosamplesResourceNotFoundException extends RuntimeException {
    GeosamplesResourceNotFoundException() {
        super();
    }
    GeosamplesResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    GeosamplesResourceNotFoundException(String message) {
        super(message);
    }
    GeosamplesResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
