package noaa.ncei.ogssd.geosamples.repository

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.GeosamplesResourceNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Repository


/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 */
@Slf4j
@Repository
class SampleRepository extends BaseRepository {
    static final String TABLENAME = 'curators_sample_tsqp'
    final String recordsQueryString
    final String countQueryString
    static final String orderByClause = " order by cruise, begin_date, leg, sample, device"
    List defaultCriteria = []
    private final String schema
    private final String intervalTable
    private final String sampleTable


    // inject values from application.properties
    SampleRepository(
        @Value('${geosamples.schema: mud}') String schema,
        @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String sampleTable,
        @Value('${geosamples.interval_table: mud.curators_interval}') String intervalTable
    ) {
        this.schema = schema
        // fully qualified table names
        this.intervalTable = intervalTable
        this.sampleTable = sampleTable
        this.recordsQueryString = "select * from ${schema}.${TABLENAME}"
        this.countQueryString = "select count(*) from ${schema}.${TABLENAME}"
    }


    Map<String, Object> getRecordById(String id) {
        String queryString = "select * from ${schema}.${TABLENAME} where imlgs = ?"
        try {
            def result = jdbcTemplate.queryForMap(queryString, id)
            return result
        } catch (EmptyResultDataAccessException e) {
            throw new GeosamplesResourceNotFoundException('invalid IMLGS ID')
        }

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

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

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

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        // with default criteria, there will always be a whereClause
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

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        // with default criteria, there will always be a whereClause
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

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        // with default criteria, there will always be a whereClause
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

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        // with default criteria, there will always be a whereClause
        String queryString = """select distinct igsn from ${sampleTable} ${whereClause} order by igsn"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['igsn']
    }

}