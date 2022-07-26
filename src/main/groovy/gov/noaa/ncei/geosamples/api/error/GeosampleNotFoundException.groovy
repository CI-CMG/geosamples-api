package gov.noaa.ncei.geosamples.api.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(value = HttpStatus.NOT_FOUND)
class GeosampleNotFoundException extends RuntimeException {
    GeosampleNotFoundException(id) {
        super("Could not find geosample " + id);
    }
}
