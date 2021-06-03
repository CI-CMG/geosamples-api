package noaa.ncei.ogssd.geosamples.repository

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.GeosamplesDTO
import noaa.ncei.ogssd.geosamples.GeosamplesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository


/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 */
@Slf4j
@Repository
class IntervalRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;
    private final String intervalTable
    private final String sampleTable


    // inject values from application-<profilename>.properties
    IntervalRepository(
            @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String sampleTable,
            @Value('${geosamples.interval_table: mud.curators_interval}') String intervalTable
    ) {
        // fully qualified table names
        this.intervalTable = intervalTable
        this.sampleTable = sampleTable
    }


    List getIntervals(GeosamplesDTO searchParams) {
        log.debug("inside getIntervals with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues
        // TODO select which fields to return
        String sqlStmt = "select * from ${intervalTable} ${whereClause} order by imlgs, interval"
        log.debug(sqlStmt)
        return jdbcTemplate.queryForList(sqlStmt, *criteriaValues)
    }


    Map<String,Object> getIntervalsCount(GeosamplesDTO searchParams) {
        log.debug("inside getCount with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues
        String queryString = "select count(*) from ${intervalTable} ${whereClause}"
        return jdbcTemplate.queryForMap(queryString, *criteriaValues)
    }


    /**
     * return a list the unique lithology(lith1, lith2, rock_lith) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueLithologyValues(GeosamplesDTO searchParams) {
        log.debug("inside getUniqueLithologyValues with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues
        String queryString = """select distinct lithology from (
            (select distinct lith1 as lithology from ${intervalTable} ${whereClause})
            union
            (select distinct lith2 as lithology from ${intervalTable} ${whereClause})
            union
            (select distinct rock_lith as lithology from ${intervalTable} ${whereClause})
        ) a where lithology is not null order by lithology"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues, *criteriaValues, *criteriaValues)
        // filter out null values here because doing so in the SQL statement requires 3 separate where clauses
        return (resultSet.findAll { it['lithology']}['lithology'])
    }


    /**
     * return a list the unique texture(text1, text2) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueTextureValues(GeosamplesDTO searchParams) {
        log.debug("inside getUniqueTextureValues with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues
        String queryString = """select distinct texture from (
                (select distinct text1 as texture from ${intervalTable} ${whereClause})
                union
                (select distinct text2 as texture from ${intervalTable} ${whereClause})
            ) a where texture is not null order by texture"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues, *criteriaValues)
        // filter out null values here because doing so in the SQL statement requires 3 separate where clauses
        return (resultSet.findAll { it['texture']}['texture'])
    }


    /**
     * return a list the unique mineralogy (rock_min) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueMineralogyValues(GeosamplesDTO searchParams) {
        log.debug("inside getUniqueMineralogyValues with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['rock_min is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = """select distinct rock_min as mineralogy from ${intervalTable} ${whereClause} order by rock_min"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['mineralogy']
    }


    /**
     * return a list the unique weathering (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueWeatheringValues(GeosamplesDTO searchParams) {
        log.debug("inside getUniqueWeatheringValues with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['weath_meta is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = """select distinct weath_meta as weathering from ${intervalTable} ${whereClause} order by weath_meta"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['weathering'].collect {it.trim().split(' - ')[-1]}
    }


    /**
     * return a list the unique metamorphism (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueMetamorphismValues(GeosamplesDTO searchParams) {
        log.debug("inside getUniqueMetamorphismValues with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['weath_meta is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = """select distinct weath_meta as metamorphism from ${intervalTable} ${whereClause} order by weath_meta"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['metamorphism'].collect {it.trim().split(' - ')[-1]}
    }


    /**
     * return a list the unique geologic age (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueGeologicAgeValues(GeosamplesDTO searchParams) {
        log.debug("inside getUniqueGeologicAgeValues with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['age is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = """select distinct age from ${intervalTable} ${whereClause} order by age"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['age']
    }
}
