package gov.noaa.ncei.geosamples.api.model

import gov.noaa.ncei.geosamples.api.error.GeosamplesBadRequestException
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size
import io.swagger.v3.oas.annotations.media.Schema
/*
 * Class used to pass data from controllers to repositories
 */
@Slf4j
@Component
@ToString(includeNames=true,ignoreNulls=true,includePackage=false)
class GeosampleSearchParameterObject {
    @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String sampleTable
    String repository
    String bbox
    Double xmin
    Double ymin
    Double xmax
    Double ymax
    String platform
    String lake
    String cruise
    String device
    @Size(min=4, max=8)
    String startDate
//    Date startDate
    @Min(0L)
    Integer minDepth
    Integer maxDepth
    String igsn
    String lithology
    String texture
    String mineralogy
    String weathering
    String metamorphism
    String storageMethod
    String province
    String age
    @Size(min=12, max=12)
    String imlgs
    String publish

    // paging controls
    @Min(0L)
    Integer offset = 0
    //TODO allow requests w/o limits? e.g. populate table which pages client-side?
    @Min(1L) @Max(5000L)
    Integer pageSize = 500

    void setPage_size(String pageSizeStr) {
        pageSize = pageSizeStr.toInteger()
    }


    /*
     * custom setters.  Type conversion exceptions handled as HTTP status 400.
     */
    // TODO build custom validation annotation for geographic coordinates
    // format: minx,miny,maxx,maxy
    void setBbox(String bboxString) {
        def coords = bboxString.split(',').collect { Double.parseDouble(it.trim()) }
        if (coords[0] < -180 || coords[1] < -90 || coords[2] > 180 || coords[3] > 90
                || coords[0] >= coords[2] || coords[1] >= coords[3]) {
            throw new GeosamplesBadRequestException("invalid geographic coordinates provided for bbox")
        }
        // ensure there are no spaces in the provided value
        bbox = coords.join(',')
        xmin = coords[0]
        ymin = coords[1]
        xmax = coords[2]
        ymax = coords[3]
    }

//    void setStart_date(dateString) {
//        // allows "wrap-around" date, e.g. 2021-16-02 == 2022-02-02
//        this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString)
//    }
    void setStart_date(dateString) {
        this.startDate = dateString
    }

    // work around changing the property name from REST parameter name convention to Java variable convention
    void setMin_depth(String minDepthString) {
        this.minDepth = minDepthString as Integer
    }

    void setMax_depth(String maxDepthString) {
        this.maxDepth = maxDepthString as Integer
    }

    void setStorage_method(String storageMethod) {
        this.storageMethod = storageMethod
    }

}

