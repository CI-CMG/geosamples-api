package noaa.ncei.ogssd.geosamples.repository

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository


/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 */
@Slf4j
@Repository
class IntervalRepository extends BaseRepository {
    static final String TABLENAME = 'curators_interval'
    static final String recordsQueryString = "select * from ${SCHEMA}.${TABLENAME}"
    static final String countQueryString = "select count(*) from ${SCHEMA}.${TABLENAME}"
    static final String orderByClause = " order by interval"
    List defaultCriteria = []

    private final String schema
    private final String intervalTable
    private final String sampleTable

    // inject values from application.properties
    IntervalRepository(
            @Value('${geosamples.schema: mud}') String schema,
            @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String sampleTable,
            @Value('${geosamples.interval_table: mud.curators_interval}') String intervalTable
    ) {
        this.schema = schema
        // fully qualified table names
        this.intervalTable = intervalTable
        this.sampleTable = sampleTable
    }  // end constructor


    /**
     * return a list the unique lithology(lith1, lith2, rock_lith) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueLithologyValues(Map<String,Object>searchParameters) {
        log.debug("inside getUniqueLithologyValues with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters)
        String whereClause = response[0]
        def criteriaValues = response[1]

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        def resultSet
        if (whereClause) {
            String queryString = """select distinct lithology from (
                (select distinct lith1 as lithology from ${intervalTable} ${whereClause})
                union
                (select distinct lith2 as lithology from ${intervalTable} ${whereClause})
                union
                (select distinct rock_lith as lithology from ${intervalTable} ${whereClause})
            ) a order by lithology"""
            resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues, *criteriaValues, *criteriaValues)
        } else {
            String queryString = """select distinct lithology from (
                (select distinct lith1 as lithology from ${intervalTable})
                union
                (select distinct lith2 as lithology from ${intervalTable})
                union
                (select distinct rock_lith as lithology from ${intervalTable})
            ) a order by lithology"""
            resultSet = jdbcTemplate.queryForList(queryString)
        }
        // filter out null values here because doing so in the SQL statement requires 3 separate where clauses
        return (resultSet.findAll { it['lithology']}['lithology'])
    }


    /**
     * return a list the unique texture(text1, text2) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueTextureValues(Map<String,Object>searchParameters) {
        log.debug("inside getUniqueTextureValues with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters)
        String whereClause = response[0]
        def criteriaValues = response[1]

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        def resultSet
        if (whereClause) {
            String queryString = """select distinct texture from (
                (select distinct text1 as texture from ${intervalTable} ${whereClause})
                union
                (select distinct text2 as texture from ${intervalTable} ${whereClause})
            ) a order by lithology"""
            resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues, *criteriaValues)
        } else {
            String queryString = """select distinct texture from (
                (select distinct text1 as texture from ${intervalTable})
                union
                (select distinct text2 as texture from ${intervalTable})
            ) a order by texture"""
            resultSet = jdbcTemplate.queryForList(queryString)
        }
        // filter out null values here because doing so in the SQL statement requires 3 separate where clauses
        return (resultSet.findAll { it['texture']}['texture'])
    }


    /**
     * return a list the unique mineralogy (rock_min) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueMineralogyValues(Map<String,Object>searchParameters) {
        log.debug("inside getUniqueMineralogyValues with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["rock_min is not null"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        // with default criteria, there will always be a whereClause
        String queryString = """select distinct rock_min as mineralogy from ${intervalTable} ${whereClause} order by rock_min"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['mineralogy']
    }


    /**
     * return a list the unique weathering (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueWeatheringValues(Map<String,Object>searchParameters) {
        log.debug("inside getUniqueWeatheringValues with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["weath_meta like 'weathering - %'"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        // with default criteria, there will always be a whereClause
        String queryString = """select distinct weath_meta as weathering from ${intervalTable} ${whereClause} order by weath_meta"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['weathering'].collect {it.trim().split(' - ')[-1]}
    }


    /**
     * return a list the unique metamorphism (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueMetamorphismValues(Map<String,Object>searchParameters) {
        log.debug("inside getUniqueMetamorphismValues with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["weath_meta like 'metamorphism - %'"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        // with default criteria, there will always be a whereClause
        String queryString = """select distinct weath_meta as metamorphism from ${intervalTable} ${whereClause} order by weath_meta"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['metamorphism'].collect {it.trim().split(' - ')[-1]}
    }

    /**
     * return a list the unique geologic age (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueGeologicAgeValues(Map<String,Object>searchParameters) {
        log.debug("inside getUniqueGeologicAgeValues with ${searchParameters}")

        def response = geosamplesService.buildWhereClause(searchParameters, ["age is not null"])
        String whereClause = response[0]
        def criteriaValues = response[1]

        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }

        // with default criteria, there will always be a whereClause
        String queryString = """select distinct age from ${intervalTable} ${whereClause} order by age"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['age']
    }
}
/*
select * from mud.curators_interval where imlgs='${sample.imlgs}' order by interval

select * from mud.curators_interval where facility_code='${sample.facility_code}' and cruise='${sample.cruise}'
and sample='${sample.sample}' and device='${sample.device}' order by interval
 */