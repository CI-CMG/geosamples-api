package gov.noaa.ncei.geosamples.api.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
class Facility {
    String facility
    String facilityCode
    Integer sampleCount
    String facilityComment
    String instCode
    String addr1
    String addr2
    String addr3
    String addr4
    String contact1
    String contact2
    String contact3
    String emailLink
    String urlLink
    String ftpLink
    String otherLink
}
