package gov.noaa.ncei.geosamples.api.repository

import gov.noaa.ncei.geosamples.api.ServiceProperties
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/*
 * helper class to build the SQL where clause used against the sample table
 */

@Slf4j
@Component
class SearchParamsHelper {

    private final String sampleTable;
    private final String intervalTable;
    private final String facilityTable;
    private final String cruiseFacilityTable;
    private final String cruisePlatformTable;
    private final String platformTable;
    private final String cruiseTable;
    private final String legTable;

    @Autowired
    SearchParamsHelper(ServiceProperties properties) {
        sampleTable = properties.getSampleTable();
        intervalTable = properties.getIntervalTable();
        facilityTable = properties.getFacilityTable();
        cruiseFacilityTable = properties.getCruiseFacilityTable();
        cruisePlatformTable = properties.getCruisePlatformTable();
        platformTable = properties.getPlatformTable();
        cruiseTable = properties.getCruiseTable();
        legTable = properties.getLegTable();
    }

    /**
     * helper method to build SQL where clause and associated query values list.
     * WARNING: this method has an implicit coupling between the parameter names
     * and the database columns
     */
    private Map<String,List> buildWhereClauseAndCriteriaList(GeosampleSearchParameterObject searchParams, defaultCriteria = [], intervalQuery = false) {
        List criteria = new ArrayList(defaultCriteria)
        List criteriaValues = []

        /* start of criteria */
        if (searchParams.repository) {
            criteria << "f.facility_code = ?"
            criteriaValues << searchParams.repository
        }
        if (searchParams.bbox) {
            criteria << "s.lon >= ? and s.lat >= ? and s.lon <= ? and s.lat <= ?"
            criteriaValues += [searchParams.xmin, searchParams.ymin, searchParams.xmax, searchParams.ymax]
        }
        if (searchParams.platform) {
            criteria << "p.platform = ?"
            criteriaValues << searchParams.platform
        }
        if (searchParams.lake) {
            criteria << "s.lake = ?"
            criteriaValues << searchParams.lake
        }
        if (searchParams.cruise) {
            criteria << "(c.cruise_name = ? or l.leg_name = ?)"
            criteriaValues << searchParams.cruise
            criteriaValues << searchParams.cruise
        }
        if (searchParams.device) {
            criteria << 's.device = ?'
            criteriaValues << searchParams.device
        }
//        if (this.startDate) {
//            criteria << "begin_date >= ?"
//            criteriaValues << new java.sql.Date(this.startDate.getTime())
//        }
        // BEGIN_DATE is text field in format YYYYMMDD
        if (searchParams.startDate) {
            criteria << "s.begin_date like ?"
            criteriaValues << "${searchParams.startDate}%"
        }
        if (searchParams.minDepth) {
            criteria << 's.water_depth >= ?'
            criteriaValues << searchParams.minDepth
        }
        if (searchParams.maxDepth) {
            criteria << 's.water_depth <= ?'
            criteriaValues << searchParams.maxDepth
        }
        if (searchParams.igsn) {
            criteria << 's.igsn = ?'
            criteriaValues << searchParams.igsn
        }
        if (searchParams.imlgs) {
            criteria << "s.imlgs = ?"
            criteriaValues << searchParams.imlgs
        }
        if (searchParams.lithology) {
            if(intervalQuery) {
                criteria << """(i.lith1 like ? or i.lith2 like ? or i.rock_lith like ?)"""
            } else {
                criteria << """s.imlgs in (select imlgs from ${intervalTable} where (lith1 like ? or lith2 like ? or 
                    rock_lith like ?))"""
            }
            criteriaValues << "%${searchParams.lithology}%"
            criteriaValues << "%${searchParams.lithology}%"
            criteriaValues << "%${searchParams.lithology}%"
        }
        if (searchParams.texture) {
            if(intervalQuery) {
                criteria << "(i.text1 like ? or i.text2 like ?)"
            } else {
                criteria << "s.imlgs in (select imlgs from ${intervalTable} where (text1 like ? or text2 like ?))"
            }
            criteriaValues << "%${searchParams.texture}%"
            criteriaValues << "%${searchParams.texture}%"
        }
        if (searchParams.mineralogy) {
            if(intervalQuery) {
                criteria << "i.rock_min like ?"
            } else {
                criteria << "s.imlgs in (select imlgs from ${intervalTable} where rock_min like ?)"
            }
            criteriaValues << "%${searchParams.mineralogy}%"
        }
        if (searchParams.weathering) {
            if(intervalQuery) {
                criteria << "i.weath_meta like ?"
            } else {
                criteria << "s.imlgs in (select imlgs from ${intervalTable} where weath_meta like ?)"
            }
            criteriaValues << "%${searchParams.weathering}%"
        }
        if (searchParams.metamorphism) {
            if(intervalQuery) {
                criteria << "i.weath_meta like ?"
            } else {
                criteria << "s.imlgs in (select imlgs from ${intervalTable} where weath_meta like ?)"
            }
            criteriaValues << "%${searchParams.metamorphism}%"
        }
        if (searchParams.storageMethod) {
            criteria << 's.storage_meth = ?'
            criteriaValues << searchParams.storageMethod
        }
        if (searchParams.province) {
            criteria << 's.province = ?'
            criteriaValues << searchParams.province
        }
        if (searchParams.age) {
            if(intervalQuery) {
                criteria << "i.age like ?"
            } else {
                criteria << "s.imlgs in (select imlgs from ${intervalTable} where age like ?)"
            }
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
