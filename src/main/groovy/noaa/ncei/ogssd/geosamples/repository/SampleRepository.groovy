package noaa.ncei.ogssd.geosamples.repository

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.GeosamplesResourceNotFoundException
import noaa.ncei.ogssd.geosamples.GeosamplesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 */
@Slf4j
@Repository
class SampleRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GeosamplesService geosamplesService

    static final String orderByClause = " order by cruise, begin_date, leg, sample, device"
    List defaultCriteria = []
    private final String intervalTable
    private final String sampleTable

    // skip SHAPE column since there are problems serializing SDO_Geometry into JSON
    private final List allSampleFields = [
        'facility_code', 'ship_code', 'platform', 'cruise', 'sample', 'device', 'begin_date', 'end_date', 'lat',
        'latdeg', 'latmin', 'ns', 'end_lat', 'end_latdeg', 'end_latmin', 'end_ns', 'lon', 'londeg', 'lonmin', 'ew',
        'end_lon', 'end_londeg', 'end_lonmin', 'end_ew', 'latlon_orig', 'water_depth', 'end_water_depth',
        'storage_meth', 'cored_length', 'cored_length_mm', 'cored_diam', 'cored_diam_mm', 'pi', 'province', 'lake',
        'other_link', 'last_update', 'igsn', 'leg', 'sample_comments', 'publish', 'previous_state', 'objectid',
        'show_sampl', 'imlgs'
    ]

    // subset of fields used for display in webapp
    private final List displaySampleFields = [
        'facility_code', 'platform', 'cruise', 'sample', 'device', 'begin_date', 'lat', 'lon', 'water_depth',
        'storage_meth', 'cored_length', 'igsn', 'leg', 'objectid', 'imlgs'
    ]


    // inject values from application-<profilename>.properties
    SampleRepository(
        @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String sampleTable,
        @Value('${geosamples.interval_table: mud.curators_interval}') String intervalTable
    ) {
        // fully qualified table names
        this.intervalTable = intervalTable
        this.sampleTable = sampleTable
    }


    List getSamples(Map<String,Object>searchParameters) {
        log.debug("inside getSamples with ${searchParameters}")

        // TODO have buildWhereClause return ['', []] rather than [null, null] when no criteria
        def response = geosamplesService.buildWhereClause(searchParameters, defaultCriteria)
        String whereClause = response[0] ?: ''
        def criteriaValues = response[1] ?: []
        logCriteriaValues(criteriaValues)
        String sqlStmt = """select ${allSampleFields.join(', ')} from ${sampleTable} ${whereClause} ${orderByClause}"""
        log.debug(sqlStmt)
        // error if pass null as criteriaValues
        return jdbcTemplate.queryForList(sqlStmt, *criteriaValues)
    }


    Map<String,Object> getSamplesCount(Map<String,Object>searchParameters) {
        log.debug("inside getSamplesCount with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, defaultCriteria)
        String whereClause = response[0] ?: ''
        def criteriaValues = response[1] ?: []
        logCriteriaValues(criteriaValues)
        String sqlStmt = "select count(*) from ${sampleTable} ${whereClause}"
        return jdbcTemplate.queryForMap(sqlStmt, *criteriaValues)
    }


    Map<String, Object> getSampleById(String id) {
        String sqlStmt = "select ${allSampleFields.join(', ')} from ${sampleTable} where imlgs = ?"
        try {
            def result = jdbcTemplate.queryForMap(sqlStmt, id)
            return result
        } catch (EmptyResultDataAccessException e) {
            throw new GeosamplesResourceNotFoundException('invalid IMLGS ID')
        }
    }


    // TODO combine w/ getSampleRecords
    List getDisplayRecords(Map<String,Object>searchParameters) {
        log.debug("inside getDisplayRecords with ${searchParameters}")
        def response = geosamplesService.buildWhereClause(searchParameters, defaultCriteria)
        String whereClause = response[0] ?: ''
        def criteriaValues = response[1] ?: []
        logCriteriaValues(criteriaValues)
        String sqlStmt = "select ${displaySampleFields.join(', ')} from ${sampleTable} ${whereClause} ${orderByClause}"
        log.debug(sqlStmt)
        return jdbcTemplate.queryForList(sqlStmt, *criteriaValues)
    }


    /**
     * return a list the unique storage methods (storage_meth) values used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueStorageMethods(Map<String,Object>searchParameters) {
        log.debug("inside getUniqueStorageMethods with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["storage_meth is not null"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        // with default criteria, there will always be a whereClause
        String queryString = """select distinct storage_meth from ${sampleTable} ${whereClause} order by storage_meth"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['storage_meth']
    }


    /**
     * return a list the unique physiographic province names (province) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniquePhysiographicProvinces(Map<String,Object>searchParameters) {
        log.debug("inside getUniquePhysiographicProvinces with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["province is not null"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        String queryString = """select distinct province from ${sampleTable} ${whereClause} order by province"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['province']
    }


    /**
     * return a list the unique devices (device) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getDeviceNames(Map<String,Object>searchParameters) {
        log.debug("inside getDeviceNames with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["device is not null"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        String queryString = """select distinct device from ${sampleTable} ${whereClause} order by device"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['device']
    }


    /**
     * return a list the unique lake names (lake) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getLakes(Map<String,Object>searchParameters) {
        log.debug("inside getLakes with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["lake is not null"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        String queryString = """select distinct lake from ${sampleTable} ${whereClause} order by lake"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['lake']
    }


    /**
     * return a list the unique lake names (lake) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getIgsnValues(Map<String,Object>searchParameters) {
        log.debug("inside getIgsnValues with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["igsn is not null"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        // with default criteria, there will always be a whereClause
        String queryString = "select distinct igsn from ${sampleTable} ${whereClause} order by igsn"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['igsn']
    }


    List getCruiseNames(Map<String, Object> searchParameters) {
        log.debug("inside getCruiseNames with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["cruise is not null"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        // TODO combine cruise and leg values into response?
        String queryString = """select distinct cruise from ${sampleTable} ${whereClause} order by cruise"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['cruise']
    }


    List getPlatformNames(Map<String, Object> searchParameters) {
        log.debug("inside getPlatformNames with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["platform is not null"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        String queryString = """select distinct platform from ${sampleTable} ${whereClause} order by platform"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['platform']
    }


    List getCruises(Map<String, Object> searchParameters) {
        //TODO
        return null
    }


    def logCriteriaValues(List criteriaValues) {
        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }
    }
}