package gov.noaa.ncei.geosamples.api.repository


import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject
import groovy.util.logging.Slf4j
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
    SearchParamsHelper searchParamsHelper

    @Autowired
    JdbcTemplate jdbcTemplate;

    // inject values from application-<profilename>.properties
    @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String sampleTable
    @Value('${geosamples.interval_table: mud.curators_interval}') String intervalTable

    // all possible fields that could be returned
    //    private final List allIntervalFields = [
    //      'facility_code', 'ship_code', 'platform', 'cruise', 'sample', 'device', 'interval', 'depth_top', 'depth_top_mm',
    //      'depth_bot', 'depth_bot_mm', 'dhcore_id', 'dhcore_length', 'dhcore_length_mm', 'dhcore_interval',
    //      'dtop_in_dhcore', 'dtop_mm_in_dhcore', 'dbot_in_dhcore', 'dbot_mm_in_dhcore', 'lith1', 'text1', 'lith2', 'text2',
    //      'comp1', 'comp2', 'comp3', 'comp4', 'comp5', 'comp6', 'description', 'age', 'absolute_age_top',
    //      'absolute_age_bot', 'weight', 'rock_lith', 'rock_min', 'weath_meta', 'remark', 'munsell_code', 'munsell',
    //      'exhaust_code', 'photo_link', 'lake', 'unit_number', 'int_comments', 'dhdevice', 'cmcd_top', 'mmcd_top',
    //      'cmcd_bot', 'mmcd_bot', 'publish', 'previous_state', 'igsn', 'imlgs', 'parent_igsn'
    //    ]

    // all fields selected to return
    static private final List allIntervalFields = [
        'facility_code', 'platform', 'cruise', 'sample', 'device', 'interval', 'depth_top',
        'depth_bot', 'lith1', 'text1', 'lith2', 'text2',
        'comp1', 'comp2', 'comp3', 'comp4', 'comp5', 'comp6', 'description', 'age', 'absolute_age_top',
        'absolute_age_bot', 'weight', 'rock_lith', 'rock_min', 'weath_meta', 'remark', 'munsell_code',
        'exhaust_code', 'photo_link', 'lake', 'int_comments', 'igsn', 'imlgs'
    ]


    // TODO have a separate method which returns Interval objects rather than List<Map>?
    List getIntervals(GeosampleSearchParameterObject searchParams) {
        // searchParams are criteria against Geosamples table NOT Intervals table
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String sqlStmt = """select ${allIntervalFields.join(', ')} from ${intervalTable} where imlgs in
        (select imlgs from ${sampleTable} ${criteria.whereClause}) order by imlgs, interval"""
        log.debug(sqlStmt)
        return jdbcTemplate.queryForList(sqlStmt, *criteria.values)
    }


    Map<String,Object> getIntervalsCount(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = """select count(*) as count from ${intervalTable} where imlgs in 
        (select imlgs from ${sampleTable} ${criteria.whereClause}"""
        return jdbcTemplate.queryForMap(queryString, *criteria.values)
    }


    /**
     * return a list the unique lithology(lith1, lith2, rock_lith) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueLithologyValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = """select distinct lithology from (
            (select distinct lith1 as lithology from ${intervalTable} where imlgs in (select imlgs from ${sampleTable} ${criteria.whereClause}))
            union
            (select distinct lith2 as lithology from ${intervalTable} where imlgs in (select imlgs from ${sampleTable} ${criteria.whereClause}))
            union
            (select distinct rock_lith as lithology from ${intervalTable} where imlgs in (select imlgs from ${sampleTable} ${criteria.whereClause}))
        ) a where lithology is not null order by lithology"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values, *criteria.values, *criteria.values)
        return resultSet['lithology']
    }


    /**
     * return a list the unique texture(text1, text2) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueTextureValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = """select distinct texture from (
                (select distinct text1 as texture from ${intervalTable} where imlgs in (select imlgs from ${sampleTable} ${criteria.whereClause}))
                union
                (select distinct text2 as texture from ${intervalTable} where imlgs in (select imlgs from ${sampleTable} ${criteria.whereClause}))
            ) a where texture is not null order by texture"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values, *criteria.values)
        return resultSet['texture']
    }


    /**
     * return a list the unique mineralogy (rock_min) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueMineralogyValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = """select distinct rock_min as mineralogy from ${intervalTable} where rock_min is not null 
        and imlgs in (select imlgs from ${sampleTable} ${criteria.whereClause}) order by rock_min"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['mineralogy']
    }


    /**
     * return a list the unique weathering (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueWeatheringValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = """select distinct weath_meta from ${intervalTable} where weath_meta is not null
        and imlgs in (select imlgs from ${sampleTable} ${criteria.whereClause}) order by weath_meta"""
        log.debug(queryString)
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        // split out the weathering code from the combined value. Assumes separator is consistent
        return resultSet['weath_meta'].findAll({it.startsWith('weathering')}).collect {it.trim().split(' - ')[-1]}
    }


    /**
     * return a list the unique metamorphism (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueMetamorphismValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = """select distinct weath_meta from ${intervalTable} where weath_meta is not null
        and imlgs in (select imlgs from ${sampleTable} ${criteria.whereClause}) order by weath_meta"""
        log.debug(queryString)
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        // split out the weathering code from the combined value. Assumes separator is consistent
        return resultSet['weath_meta'].findAll({it.startsWith('metamorphism')}).collect {it.trim().split(' - ')[-1]}
    }


    /**
     * return a list the unique geologic age (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueGeologicAgeValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = """select distinct age from ${intervalTable} where age is not null
        and imlgs in (select imlgs from ${sampleTable} ${criteria.whereClause}) order by age"""
        log.debug(queryString)
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['age']
    }
}
