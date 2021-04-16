package noaa.ncei.ogssd.geosamples

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.repository.SampleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat

@Slf4j
@Service
class GeosamplesService {
    private final String schema
    private final String intervalTable
    private final String sampleTable

    @Autowired
    GeosamplesService(
            @Value('${geosamples.schema: mud}') String schema,
            @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String curatorTable,
            @Value('${geosamples.interval_table: curators_interval}') String intervalTable
    ) {
        this.schema = schema
        this.intervalTable = intervalTable
        this.sampleTable = sampleTable
    }
/**
 * helper method to build SQL where clause and associated query values list.
 * WARNING: this method has an implicit coupling between the parameter names
 * and the database columns
 */
    List buildWhereClause(parameters, List<String> defaultCriteria = []) {
        log.debug("inside buildWhereClause with ${parameters} and ${defaultCriteria}")
        List criteria = new ArrayList(defaultCriteria)
        List criteriaValues = []

        /* start of criteria */
        if (parameters['repository']) {
            criteria << 'facility_code = ?'
            criteriaValues << parameters['repository']
        }
        if (parameters['bbox']) {
            def coords = parameters['bbox'].split(',').collect { Double.parseDouble(it.trim()) }
            criteria << "lon >= ? and lat >= ? and lon <= ? and lat <= ?"
            criteriaValues += coords
        }
        if (parameters['platform']) {
            criteria << 'platform = ?'
            criteriaValues << parameters['platform']
        }
        if (parameters['lake']) {
            criteria << 'lake = ?'
            criteriaValues << parameters['lake']
        }
        if (parameters['cruise']) {
            criteria << '(cruise = ? or leg = ?)'
            criteriaValues << parameters['cruise']
            criteriaValues << parameters['cruise']
        }
        if (parameters['device']) {
            criteria << 'device = ?'
            criteriaValues << parameters['device']
        }
        if (parameters['startDate']) {
            def startDate = new SimpleDateFormat("yyyy-MM-dd").parse(parameters['startDate'])
            criteria << "begin_date >= ?"
            criteriaValues << new java.sql.Date(startDate.getTime())
        }
        if (parameters['minDepth']) {
            criteria << 'water_depth >= ?'
            criteriaValues << parameters['minDepth']
        }
        if (parameters['maxDepth']) {
            criteria.push('water_depth < ?')
            criteriaValues << parameters['maxDepth']
        }
        if (parameters['igsn']) {
            criteria.push('igsn = ?')
            criteriaValues << parameters['igsn']
        }
        if (parameters['imlgs']) {
            criteria.push('imlgs = ?')
            criteriaValues << parameters['imlgs']
        }
        if (parameters['lithology']) {
            criteria.push("""imlgs in (select imlgs from ${intervalTable} where (lith1 like ? or lith2 like ? or rock_lith like ?))""")
            criteriaValues << "%${parameters['lithology']}%"
            criteriaValues << "%${parameters['lithology']}%"
            criteriaValues << "%${parameters['lithology']}%"
        }
        if (parameters['texture']) {
            criteria.push("""imlgs in (select imlgs from ${intervalTable} where (text1 like ? or text2 like ?))""")
            criteriaValues << "%${parameters['texture']}%"
            criteriaValues << "%${parameters['texture']}%"
        }
        if (parameters['mineralogy']) {
            criteria.push("""imlgs in (select imlgs from ${intervalTable} where rock_min like ?)""")
            criteriaValues << "%${parameters['mineralogy']}%"
        }
        if (parameters['weathering']) {
            criteria.push("""imlgs in (select imlgs from ${intervalTable} where weath_meta like ?)""")
            criteriaValues << "%${parameters['weathering']}%"
        }

        /* end of criteria */

        if (criteria.size() > 0) {
            log.debug("returning ${" where ${criteria.join(' and ')}"}, ${criteriaValues}")
            return [" where ${criteria.join(' and ')}", criteriaValues]
        } else {
            return [null, null]
        }
    }

}
