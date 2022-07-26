package gov.noaa.ncei.geosamples.api.repository


import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/*
 * helper class to build the SQL where clause used against the sample table
 */

@Slf4j
@Component
class SearchParamsHelper {
    @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String sampleTable
    @Value('${geosamples.interval_table: mud.curators_interval}') String intervalTable
    @Value('${geosamples.facility_table: mud.curators_facility}') String facilityTable


    /**
     * helper method to build SQL where clause and associated query values list.
     * WARNING: this method has an implicit coupling between the parameter names
     * and the database columns
     */
    private Map<String,List> buildWhereClauseAndCriteriaList(GeosampleSearchParameterObject searchParams, defaultCriteria = []) {
        List criteria = new ArrayList(defaultCriteria)
        List criteriaValues = []

        /* start of criteria */
        if (searchParams.repository) {
            criteria << "facility_code = ?"
            criteriaValues << searchParams.repository
        }
        if (searchParams.bbox) {
            criteria << "lon >= ? and lat >= ? and lon <= ? and lat <= ?"
            criteriaValues += [searchParams.xmin, searchParams.ymin, searchParams.xmax, searchParams.ymax]
        }
        if (searchParams.platform) {
            criteria << "platform = ?"
            criteriaValues << searchParams.platform
        }
        if (searchParams.lake) {
            criteria << "lake = ?"
            criteriaValues << searchParams.lake
        }
        if (searchParams.cruise) {
            criteria << "(cruise = ? or leg = ?)"
            criteriaValues << searchParams.cruise
            criteriaValues << searchParams.cruise
        }
        if (searchParams.device) {
            criteria << 'device = ?'
            criteriaValues << searchParams.device
        }
//        if (this.startDate) {
//            criteria << "begin_date >= ?"
//            criteriaValues << new java.sql.Date(this.startDate.getTime())
//        }
        // BEGIN_DATE is text field in format YYYYMMDD
        if (searchParams.startDate) {
            criteria << "begin_date like ?"
            criteriaValues << "${searchParams.startDate}%"
        }
        if (searchParams.minDepth) {
            criteria << 'water_depth >= ?'
            criteriaValues << searchParams.minDepth
        }
        if (searchParams.maxDepth) {
            criteria << 'water_depth <= ?'
            criteriaValues << searchParams.maxDepth
        }
        if (searchParams.igsn) {
            criteria << 'igsn = ?'
            criteriaValues << searchParams.igsn
        }
        if (searchParams.imlgs) {
            criteria << "imlgs = ?"
            criteriaValues << searchParams.imlgs
        }
        if (searchParams.lithology) {
            criteria << """imlgs in (select imlgs from ${intervalTable} where (lith1 like ? or lith2 like ? or 
                    rock_lith like ?))"""
            criteriaValues << "%${searchParams.lithology}%"
            criteriaValues << "%${searchParams.lithology}%"
            criteriaValues << "%${searchParams.lithology}%"
        }
        if (searchParams.texture) {
            criteria << "imlgs in (select imlgs from ${intervalTable} where (text1 like ? or text2 like ?))"
            criteriaValues << "%${searchParams.texture}%"
            criteriaValues << "%${searchParams.texture}%"
        }
        if (searchParams.mineralogy) {
            criteria << "imlgs in (select imlgs from ${intervalTable} where rock_min like ?)"
            criteriaValues << "%${searchParams.mineralogy}%"
        }
        if (searchParams.weathering) {
            criteria << "imlgs in (select imlgs from ${intervalTable} where weath_meta like ?)"
            criteriaValues << "%${searchParams.weathering}%"
        }
        if (searchParams.metamorphism) {
            criteria << "imlgs in (select imlgs from ${intervalTable} where weath_meta like ?)"
            criteriaValues << "%${searchParams.metamorphism}%"
        }
        if (searchParams.storageMethod) {
            criteria << 'storage_meth = ?'
            criteriaValues << searchParams.storageMethod
        }
        if (searchParams.province) {
            criteria << 'province = ?'
            criteriaValues << searchParams.province
        }
        if (searchParams.age) {
            criteria << 'age = ?'
            criteriaValues << searchParams.age
        }
        /* end of criteria */

        if (criteria.size() > 0) {
            return ['whereClause': " where ${criteria.join(' and ')}", 'values': criteriaValues]
        } else {
            return ['whereClause': '', 'values': []]
        }
    }
}
