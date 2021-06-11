package noaa.ncei.ogssd.geosamples

import groovy.transform.ToString
import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Bean
import javax.validation.constraints.Min
import javax.validation.constraints.Size
import java.text.SimpleDateFormat

@Slf4j
@ToString(includeNames=true,ignoreNulls=true,includePackage=false)
class GeosamplesDTO {
    String repository
    @Size(max=24)
    String bbox
    Double xmin
    Double ymin
    Double xmax
    Double ymax
    String platform
    String lake
    String cruise
    String device
    Date startDate
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

    // paging controls
    @Min(0L)
    Integer startIndex = 0
    Integer limit = null

    // following are derived properties and not bound via Controller
    private String whereClause
    private List criteriaValues


    void setStart(String startStr) {
        // convert to 0-based
        startIndex = startStr.toInteger() - 1
    }

    Integer getEndIndex() {
        return (limit) ? (startIndex + limit) - 1 : -1
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
        this.bbox = coords.join(',')
        this.xmin = coords[0]
        this.ymin = coords[1]
        this.xmax = coords[2]
        this.ymax = coords[3]
    }

    void setStart_date(dateString) {
        // allows "wrap-around" date, e.g. 2021-16-02 == 2022-02-02
        this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString)
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


    String getWhereClause(defaultCriteria = []) {
        if (! this.whereClause) {
            this.buildWhereClauseAndCriteriaList(defaultCriteria)
        }
        return this.whereClause
    }

    List getCriteriaValues(defaultCriteria = []) {
        if (! this.criteriaValues) {
            this.buildWhereClauseAndCriteriaList(defaultCriteria)
        }
        return this.criteriaValues
    }

    /**
     * helper method to build SQL where clause and associated query values list.
     * WARNING: this method has an implicit coupling between the parameter names
     * and the database columns
     */
    private void buildWhereClauseAndCriteriaList(defaultCriteria = []) {
        List criteria = new ArrayList(defaultCriteria)
        List criteriaValues = []

        /* start of criteria */
        if (this.repository) {
            criteria << 'facility_code = ?'
            criteriaValues << this.repository
        }
        if (this.bbox) {
            criteria << "lon >= ? and lat >= ? and lon <= ? and lat <= ?"
            criteriaValues += [this.xmin, this.ymin, this.xmax, this.ymax]
        }
        if (this.platform) {
            criteria << 'platform = ?'
            criteriaValues << this.platform
        }
        if (this.lake) {
            criteria << 'lake = ?'
            criteriaValues << this.lake
        }
        if (this.cruise) {
            criteria << '(cruise = ? or leg = ?)'
            criteriaValues << this.cruise
            criteriaValues << this.cruise
        }
        if (this.device) {
            criteria << 'device = ?'
            criteriaValues << this.device
        }
        if (this.startDate) {
            criteria << "begin_date >= ?"
            criteriaValues << new java.sql.Date(this.startDate.getTime())
        }
        if (this.minDepth) {
            criteria << 'water_depth >= ?'
            criteriaValues << this.minDepth
        }
        if (this.maxDepth) {
            criteria.push('water_depth < ?')
            criteriaValues << this.maxDepth
        }
        if (this.igsn) {
            criteria.push('igsn = ?')
            criteriaValues << this.igsn
        }
        if (this.imlgs) {
            criteria.push('imlgs = ?')
            criteriaValues << this.imlgs
        }
        if (this.lithology) {
            criteria.push("""imlgs in (select imlgs from ${intervalTable} where (lith1 like ? or lith2 like ? or rock_lith like ?))""")
            criteriaValues << "%${this.lithology}%"
            criteriaValues << "%${this.lithology}%"
            criteriaValues << "%${this.lithology}%"
        }
        if (this.texture) {
            criteria.push("""imlgs in (select imlgs from ${intervalTable} where (text1 like ? or text2 like ?))""")
            criteriaValues << "%${this.texture}%"
            criteriaValues << "%${this.texture}%"
        }
        if (this.mineralogy) {
            criteria.push("""imlgs in (select imlgs from ${intervalTable} where rock_min like ?)""")
            criteriaValues << "%${this.mineralogy}%"
        }
        if (this.weathering) {
            criteria.push("""imlgs in (select imlgs from ${intervalTable} where weath_meta like ?)""")
            criteriaValues << "%${this.weathering}%"
        }
        if (this.metamorphism) {
            criteria.push("""imlgs in (select imlgs from ${intervalTable} where weath_meta like ?)""")
            criteriaValues << "%${this.metamorphism}%"
        }
        if (this.storageMethod) {
            criteria.push('storage_meth = ?')
            criteriaValues << storageMethod
        }
        if (this.province) {
            criteria.push('province = ?')
            criteriaValues << province
        }
        if (this.age) {
            criteria.push('age = ?')
            criteriaValues << age
        }
        /* end of criteria */

        if (criteria.size() > 0) {
            log.debug("where ${criteria.join(' and ')}")
            log.debug("criteriaValues: ${criteriaValues}")
            this.whereClause = " where ${criteria.join(' and ')}"
            this.criteriaValues = criteriaValues
        } else {
            this.whereClause = ''
            this.criteriaValues = []
        }
    }
}