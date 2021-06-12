package noaa.ncei.ogssd.geosamples.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
class Sample {
    @NotNull
    Integer objectid
    @NotNull @Size(min=12, max=12)
    String imlgs
    String facilityCode
    String facility
    String shipCode
    String platform
    String cruise
    String sample
    String device
    // YYYY or YYYYMMDD. Convert to Date?
    String beginDate
    String end_date
    // TODO Float or Double?
    Float lat
    Float end_lat
    Float lon
    Float end_lon
    @Size(min=1, max=1)
    String latlon_orig
    Float water_depth
    Float end_water_depth
    String storage_meth
    Integer cored_length
    Integer cored_length_mm
    Integer cored_diam
    Integer cored_diam_mm
    String pi
    String province
    String lake
    String other_link
    String last_update
    String igsn
    String leg
    String sample_comments
    String showSampl
}
