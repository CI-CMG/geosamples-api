package gov.noaa.ncei.geosamples.api.repository

import gov.noaa.ncei.geosamples.api.ServiceProperties
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

    private final SearchParamsHelper searchParamsHelper
    private final JdbcTemplate jdbcTemplate;

    // inject values from application-<profilename>.properties
    private final String sampleTable;
    private final String intervalTable;
    private final String facilityTable;
    private final String cruiseTable;
    private final String legTable;
    private final String platformTable;
    private final String cruisePlatformTable;
    private final String cruiseFacilityTable;

    @Autowired
    IntervalRepository(SearchParamsHelper searchParamsHelper, JdbcTemplate jdbcTemplate, ServiceProperties properties) {
        this.searchParamsHelper = searchParamsHelper;
        this.jdbcTemplate = jdbcTemplate;
        sampleTable = properties.getSampleTable();
        intervalTable = properties.getIntervalTable();
        facilityTable = properties.getFacilityTable();
        cruiseTable = properties.getCruiseTable();
        legTable = properties.getLegTable();
        platformTable = properties.getPlatformTable();
        cruisePlatformTable = properties.getCruisePlatformTable();
        cruiseFacilityTable = properties.getCruiseFacilityTable();
    }


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
        'f.facility_code as facility_code',
        'p.platform as platform',
        'c.cruise_name as cruise',
        's.sample as sample',
        's.device as device',
        'i.interval as interval',
        'i.depth_top as depth_top',
        'i.depth_bot as depth_bot',
        'i.lith1 as lith1',
        'i.text1 as text1',
        'i.lith2 as lith2',
        'i.text2 as text2',
        'i.comp1 as comp1',
        'i.comp2 as comp2',
        'i.comp3 as comp3',
        'i.comp4 as comp4',
        'i.comp5 as comp5',
        'i.comp6 as comp6',
        'i.description as description',
        'i.age as age',
        'i.absolute_age_top as absolute_age_top',
        'i.absolute_age_bot as absolute_age_bot',
        'i.weight as weight',
        'i.rock_lith as rock_lith',
        'i.rock_min as rock_min',
        'i.weath_meta as weath_meta',
        'i.remark as remark',
        'i.munsell_code as munsell_code',
        'i.exhaust_code as exhaust_code',
        'i.photo_link as photo_link',
        'i.lake as lake',
        'i.int_comments as int_comments',
        'i.igsn as igsn',
        'i.imlgs as imlgs'
    ]


    // TODO have a separate method which returns Interval objects rather than List<Map>?
    List getIntervals(GeosampleSearchParameterObject searchParams) {
        // searchParams are criteria against Geosamples table NOT Intervals table
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams, [], true)
        String sqlStmt = """select ${allIntervalFields.join(', ')} 
                            from ${intervalTable} i
                            inner join ${sampleTable} s on i.imlgs = s.imlgs 
                            inner join ${cruiseTable} c on s.cruise_id = c.id 
                            inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                            inner join ${platformTable} p on cp.platform_id = p.id
                            inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                            inner join ${facilityTable} f on cf.facility_id = f.id 
                            left join ${legTable} l on s.leg_id = l.id 
                             ${criteria.whereClause} order by imlgs, interval"""
        log.debug(sqlStmt)
        return jdbcTemplate.queryForList(sqlStmt, *criteria.values)
    }


    Map<String,Object> getIntervalsCount(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams, [], true)
        String queryString = """select count(*) as count
                            from ${intervalTable} i
                            inner join ${sampleTable} s on i.imlgs = s.imlgs 
                            inner join ${cruiseTable} c on s.cruise_id = c.id 
                            inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                            inner join ${platformTable} p on cp.platform_id = p.id
                            inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                            inner join ${facilityTable} f on cf.facility_id = f.id 
                            left join ${legTable} l on s.leg_id = l.id 
                             ${criteria.whereClause}"""
        return jdbcTemplate.queryForMap(queryString, *criteria.values)
    }


    /**
     * return a list the unique lithology(lith1, lith2, rock_lith) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueLithologyValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams, [], true)
        String queryString = """select distinct lithology from (
            (select distinct i.lith1 as lithology from ${intervalTable} i 
                    inner join ${sampleTable} s on i.imlgs = s.imlgs 
                    inner join ${cruiseTable} c on s.cruise_id = c.id 
                    inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                    inner join ${platformTable} p on cp.platform_id = p.id
                    inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                    inner join ${facilityTable} f on cf.facility_id = f.id 
                    left join ${legTable} l on s.leg_id = l.id 
                 ${criteria.whereClause})
            union
            (select distinct i.lith2 as lithology from ${intervalTable} i 
                    inner join ${sampleTable} s on i.imlgs = s.imlgs 
                    inner join ${cruiseTable} c on s.cruise_id = c.id 
                    inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                    inner join ${platformTable} p on cp.platform_id = p.id
                    inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                    inner join ${facilityTable} f on cf.facility_id = f.id 
                    left join ${legTable} l on s.leg_id = l.id 
                 ${criteria.whereClause})
            union
            (select distinct i.rock_lith as lithology from ${intervalTable} i
                    inner join ${sampleTable} s on i.imlgs = s.imlgs 
                    inner join ${cruiseTable} c on s.cruise_id = c.id 
                    inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                    inner join ${platformTable} p on cp.platform_id = p.id
                    inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                    inner join ${facilityTable} f on cf.facility_id = f.id 
                    left join ${legTable} l on s.leg_id = l.id 
                 ${criteria.whereClause})
        ) a where lithology is not null order by lithology"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values, *criteria.values, *criteria.values)
        return resultSet['lithology']
    }


    /**
     * return a list the unique texture(text1, text2) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueTextureValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams, [], true)
        String queryString = """select distinct texture from (
                (select distinct i.text1 as texture from ${intervalTable} i 
                    inner join ${sampleTable} s on i.imlgs = s.imlgs 
                    inner join ${cruiseTable} c on s.cruise_id = c.id 
                    inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                    inner join ${platformTable} p on cp.platform_id = p.id
                    inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                    inner join ${facilityTable} f on cf.facility_id = f.id 
                    left join ${legTable} l on s.leg_id = l.id 
                  ${criteria.whereClause})
                union
                (select distinct i.text2 as texture from ${intervalTable} i 
                    inner join ${sampleTable} s on i.imlgs = s.imlgs 
                    inner join ${cruiseTable} c on s.cruise_id = c.id 
                    inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                    inner join ${platformTable} p on cp.platform_id = p.id
                    inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                    inner join ${facilityTable} f on cf.facility_id = f.id 
                    left join ${legTable} l on s.leg_id = l.id 
                  ${criteria.whereClause})
            ) a where texture is not null order by texture"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values, *criteria.values)
        return resultSet['texture']
    }


    /**
     * return a list the unique mineralogy (rock_min) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueMineralogyValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams, [], true)
        String queryString = """select distinct i.rock_min as mineralogy from ${intervalTable} i 
                    inner join ${sampleTable} s on i.imlgs = s.imlgs 
                    inner join ${cruiseTable} c on s.cruise_id = c.id 
                    inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                    inner join ${platformTable} p on cp.platform_id = p.id
                    inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                    inner join ${facilityTable} f on cf.facility_id = f.id 
                    left join ${legTable} l on s.leg_id = l.id 
        ${criteria.whereClause ? criteria.whereClause + ' and i.rock_min is not null' : 'where i.rock_min is not null'} order by rock_min"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['mineralogy']
    }


    /**
     * return a list the unique weathering (weath_meta) values used in the curators_interval table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueWeatheringValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams, [], true)
        String queryString = """select distinct i.weath_meta as weath_meta from ${intervalTable} i 
                    inner join ${sampleTable} s on i.imlgs = s.imlgs 
                    inner join ${cruiseTable} c on s.cruise_id = c.id 
                    inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                    inner join ${platformTable} p on cp.platform_id = p.id
                    inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                    inner join ${facilityTable} f on cf.facility_id = f.id 
                    left join ${legTable} l on s.leg_id = l.id 
       ${criteria.whereClause ? criteria.whereClause + ' and i.weath_meta is not null' : 'where i.weath_meta is not null'} order by weath_meta """
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
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams, [], true)
        String queryString = """select distinct i.weath_meta as weath_meta from ${intervalTable} i 
                    inner join ${sampleTable} s on i.imlgs = s.imlgs 
                    inner join ${cruiseTable} c on s.cruise_id = c.id 
                    inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                    inner join ${platformTable} p on cp.platform_id = p.id
                    inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                    inner join ${facilityTable} f on cf.facility_id = f.id 
                    left join ${legTable} l on s.leg_id = l.id 
       ${criteria.whereClause ? criteria.whereClause + ' and i.weath_meta is not null' : 'where i.weath_meta is not null'} order by weath_meta """
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
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams, [], true)
        String queryString = """select distinct i.age as age from ${intervalTable} i 
                inner join ${sampleTable} s on i.imlgs = s.imlgs 
                inner join ${cruiseTable} c on s.cruise_id = c.id 
                inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                inner join ${platformTable} p on cp.platform_id = p.id
                inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                inner join ${facilityTable} f on cf.facility_id = f.id 
                left join ${legTable} l on s.leg_id = l.id 
        ${criteria.whereClause ? criteria.whereClause + ' and i.age is not null' : 'where i.age is not null'} order by age"""
        log.debug(queryString)
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['age']
    }
}
