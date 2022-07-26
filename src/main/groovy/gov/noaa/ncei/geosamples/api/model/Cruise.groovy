package gov.noaa.ncei.geosamples.api.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
class Cruise {
    @NotNull
    String cruise
    @NotNull
    String facilityCode
    @NotNull
    String platform
    String leg

    //ancillary information not set by JDBCTemplate
    List links = []

    def addLinks(List<Map> links) {
        this.links += links
    }
}
