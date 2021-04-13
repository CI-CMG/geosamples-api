package noaa.ncei.ogssd.geosamples

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class GeosamplesBadRequestException extends RuntimeException {
    GeosamplesBadRequestException() {
        super();
    }

    GeosamplesBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    GeosamplesBadRequestException(String message) {
        super(message);
    }

    GeosamplesBadRequestException(Throwable cause) {
        super(cause);
    }

}
