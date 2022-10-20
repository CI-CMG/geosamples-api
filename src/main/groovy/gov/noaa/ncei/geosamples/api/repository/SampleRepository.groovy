package gov.noaa.ncei.geosamples.api.repository

import gov.noaa.ncei.geosamples.api.ServiceProperties
import gov.noaa.ncei.geosamples.api.error.GeosamplesBadRequestException
import groovy.util.logging.Slf4j
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject
import gov.noaa.ncei.geosamples.api.error.GeosamplesResourceNotFoundException
import gov.noaa.ncei.geosamples.api.model.Cruise
import gov.noaa.ncei.geosamples.api.model.Sample
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 */
@Slf4j
@Repository
class SampleRepository {






    static private final String orderByClause = " order by cruise, begin_date, leg, sample, device"
//    static final String orderByClause = " order by a.cruise, a.begin_date, a.leg, a.sample, a.device"

    // inject values from application-<profilename>.properties
    private final String sampleTable;
    private final String intervalTable;
    private final String facilityTable;
    private final String linksTable;
    private final String cruiseLinksTable;
    private final String cruiseTable;
    private final String legTable;
    private final String platformTable;
    private final String cruisePlatformTable;
    private final String cruiseFacilityTable;

    private final SearchParamsHelper searchParamsHelper;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    SampleRepository(SearchParamsHelper searchParamsHelper, ServiceProperties properties, JdbcTemplate jdbcTemplate) {
        this.searchParamsHelper = searchParamsHelper;
        this.jdbcTemplate = jdbcTemplate;
        sampleTable = properties.getSampleTable();
        intervalTable = properties.getIntervalTable();
        facilityTable = properties.getFacilityTable();
        linksTable = properties.getLinksTable();
        cruiseLinksTable = properties.getCruiseLinksTable();
        cruiseTable = properties.getCruiseTable();
        legTable = properties.getLegTable();
        platformTable = properties.getPlatformTable();
        cruisePlatformTable = properties.getCruisePlatformTable();
        cruiseFacilityTable = properties.getCruiseFacilityTable();
    }


    // skip SHAPE column since there are problems serializing SDO_Geometry into JSON
//    private final List allSampleFields = [
//        'facility_code', 'ship_code', 'platform', 'cruise', 'sample', 'device', 'begin_date', 'end_date', 'lat',
//        'latdeg', 'latmin', 'ns', 'end_lat', 'end_latdeg', 'end_latmin', 'end_ns', 'lon', 'londeg', 'lonmin', 'ew',
//        'end_lon', 'end_londeg', 'end_lonmin', 'end_ew', 'latlon_orig', 'water_depth', 'end_water_depth',
//        'storage_meth', 'cored_length', 'cored_length_mm', 'cored_diam', 'cored_diam_mm', 'pi', 'province', 'lake',
//        'other_link', 'last_update', 'igsn', 'leg', 'sample_comments', 'publish', 'previous_state', 'objectid',
//        'show_sampl', 'imlgs'
//    ]

    static private final List allSampleFields = [
            'f.facility_code as facility_code',
            'p.platform as platform',
            'c.cruise_name as cruise',
            's.sample as sample',
            's.device as device',
            's.begin_date as begin_date',
            's.end_date as end_date',
            's.lat as lat',
            's.end_lat as end_lat',
            's.lon as lon',
            's.end_lon as end_lon',
            's.latlon_orig as latlon_orig',
            's.water_depth as water_depth',
            's.end_water_depth as end_water_depth',
            's.storage_meth as storage_meth',
            's.cored_length as cored_length',
            's.cored_length_mm as cored_length_mm',
            's.cored_diam as cored_diam',
            's.cored_diam_mm as cored_diam_mm',
            's.pi as pi',
            's.province as province',
            's.lake as lake',
            's.other_link as other_link',
            's.last_update as last_update',
            's.igsn as igsn',
            'l.leg_name as leg',
            's.sample_comments as sample_comments',
            's.show_sampl as show_sampl',
            's.imlgs as imlgs',
            's.publish as publish'
    ]

    // subset of fields used for display in webapp
    static private final List displaySampleFields = [
        'f.facility_code as facility_code',
        'p.platform as platform',
        'c.cruise_name as cruise',
        's.sample as sample',
        's.device as device',
        's.begin_date as begin_date',
        's.lat as lat',
        's.lon as lon',
        's.water_depth as water_depth',
        's.storage_meth as storage_meth',
        's.cored_length as cored_length',
        's.igsn as igsn',
        'l.leg_name as leg',
        's.imlgs as imlgs'
    ]


    List<Sample> getSamples(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String sqlStmt = "select ${allSampleFields.join(', ')} from ${sampleTable} s " +
                " inner join ${cruiseTable} c on s.cruise_id = c.id \n" +
                " inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id \n" +
                " inner join ${platformTable} p on cp.platform_id = p.id\n" +
                " inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id \n" +
                " inner join ${facilityTable} f on cf.facility_id = f.id \n" +
                " left join ${legTable} l on s.leg_id = l.id " +
                "${criteria.whereClause} ${orderByClause}"
        log.debug(sqlStmt)
        logCriteriaValues(criteria.values)
        // error if pass null as criteriaValues
        return jdbcTemplate.query(sqlStmt, new BeanPropertyRowMapper(Sample.class), *criteria.values)
    }


    /**
     * return a List<Map> rather then List<Sample>
     */
    List<Map> getRawSamples(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String sqlStmt = "select ${allSampleFields.join(', ')} from ${sampleTable} ${criteria.whereClause} ${orderByClause}"
        log.debug(sqlStmt)
        logCriteriaValues(criteria.values)
        // error if pass null as criteriaValues
        return jdbcTemplate.queryForList(sqlStmt, *criteria.values)
    }


    // TODO return bare count?
    Map<String,Object> getSamplesCount(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        logCriteriaValues(criteria.values)
        String sqlStmt = "select count(*) as count from ${sampleTable} ${criteria.whereClause}"
        log.debug(sqlStmt)
        return jdbcTemplate.queryForMap(sqlStmt, *criteria.values)
    }


    Sample getSampleById(String id) {
        // depends on facility table in order to return facility name
        String sqlStmt = """select ${allSampleFields.join(', ')}, f.facility  
                       from ${sampleTable} s 
                        inner join ${cruiseTable} c on s.cruise_id = c.id 
                        inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                        inner join ${platformTable} p on cp.platform_id = p.id
                        inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                        inner join ${facilityTable} f on cf.facility_id = f.id 
                        left join ${legTable} l on s.leg_id = l.id 
                        where s.imlgs = ?"""
        def sample
        try {
            sample = jdbcTemplate.queryForObject(sqlStmt, new BeanPropertyRowMapper(Sample.class), id)
        } catch (EmptyResultDataAccessException e) {
            throw new GeosamplesResourceNotFoundException('invalid IMLGS ID')
        }
        // use separate queries to aggregate associated information rather than more complicated single SQL
        sample.addLinks(getLinksById(id))
        sample.addIntervals(getIntervalsByImlgsId(id))
        sample.addCruiseLinks(getCruiseLinks(sample.cruise, sample.platform, sample.leg))
        return sample
    }


    List getLinksById(String id) {
        String sqlStmt = """select 
        datalink as link, link_level as linklevel, link_source as source, link_type as type 
        from ${linksTable} where imlgs = ?"""
        return jdbcTemplate.queryForList(sqlStmt, id)
    }


    /**
     * get all interval records associated with given IMLGS geosample
     *
     * although this method more logically belongs in the IntervalRepository, it is used by a method in this repository
     * and this avoids nesting one repository as a dependency of another
     */
    List getIntervalsByImlgsId(String id) {
        String sqlStmt = "select ${IntervalRepository.allIntervalFields.join(', ')} " +
                "from ${intervalTable} i " +
                "  inner join ${sampleTable} s on i.imlgs = s.imlgs " +
                "  inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "where i.imlgs = ?"
        return jdbcTemplate.queryForList(sqlStmt, id)
    }


    // TODO combine w/ getSampleRecords?
    // WARNING: result set limited by pageSize in parameters object (default of 500)
    List<Sample> getDisplayRecords(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String sqlStmt = """select ${displaySampleFields.join(', ')} from ${sampleTable} s 
                                inner join ${cruiseTable} c on s.cruise_id = c.id 
                                inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                                inner join ${platformTable} p on cp.platform_id = p.id
                                inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                                inner join ${facilityTable} f on cf.facility_id = f.id 
                                left join ${legTable} l on s.leg_id = l.id 
            ${criteria.whereClause} 
            ${orderByClause} offset ${searchParams.offset} rows fetch next ${searchParams.pageSize} rows only"""
        log.debug(sqlStmt)
        logCriteriaValues(criteria.values)
        return jdbcTemplate.query(sqlStmt, new BeanPropertyRowMapper(Sample.class), *criteria.values)
    }


    /**
     * return a list the unique storage methods (storage_meth) values used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueStorageMethods(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = "select distinct s.storage_meth as storage_meth from ${sampleTable} s " +
                "inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "  left join ${legTable} l on s.leg_id = l.id " +
                "${criteria.whereClause ? criteria.whereClause + ' and s.storage_meth is not null' : ' where s.storage_meth is not null'} order by storage_meth"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['storage_meth']
    }


    /**
     * return a list the unique physiographic province names (province) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniquePhysiographicProvinces(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = "select distinct s.province as province from ${sampleTable} s " +
                "  inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "  left join ${legTable} l on s.leg_id = l.id " +
                "${criteria.whereClause ? criteria.whereClause + ' and s.province is not null' : ' where s.province is not null'} order by province"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['province']
    }


    /**
     * return a list the unique devices (device) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getDeviceNames(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = "select distinct s.device as device from ${sampleTable} s " +
                "  inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "  left join ${legTable} l on s.leg_id = l.id " +
                "${criteria.whereClause ? criteria.whereClause + ' and s.device is not null' : ' where s.device is not null'} order by device"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['device']
    }


    /**
     * return a list the unique lake names (lake) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getLakes(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = "select distinct s.lake as lake from ${sampleTable} s " +
                "  inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "  left join ${legTable} l on s.leg_id = l.id " +
                "${criteria.whereClause ? criteria.whereClause + ' and s.lake is not null' : ' where s.lake is not null'} order by lake"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['lake']
    }


    /**
     * return a list the unique IGSN values used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getIgsnValues(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = "select distinct s.igsn as igsn from ${sampleTable} s " +
                "  inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "  left join ${legTable} l on s.leg_id = l.id " +
                "${criteria.whereClause ? criteria.whereClause + ' and s.igsn is not null' : ' where s.igsn is not null'} order by igsn"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['igsn']
    }


    /**
     * return a list the unique cruise and leg names used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getCruiseNames(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        // combine cruise and leg values into response
        String queryString = """select distinct cruise from (
            (select distinct c.cruise_name as cruise from ${sampleTable} s 
                inner join ${cruiseTable} c on s.cruise_id = c.id 
                inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                inner join ${platformTable} p on cp.platform_id = p.id 
                inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                inner join ${facilityTable} f on cf.facility_id = f.id 
                left join ${legTable} l on s.leg_id = l.id 
            ${criteria.whereClause})
            union
            (select distinct l.leg_name as cruise from ${sampleTable} s 
                inner join ${cruiseTable} c on s.cruise_id = c.id 
                inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id 
                inner join ${platformTable} p on cp.platform_id = p.id 
                inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
                inner join ${facilityTable} f on cf.facility_id = f.id 
                left join ${legTable} l on s.leg_id = l.id 
            ${criteria.whereClause})
        ) a where cruise is not null order by cruise"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values, *criteria.values)
        return (resultSet.findAll { it['cruise']}['cruise'])
    }


    /**
     * return a list the unique platform names used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. repository, etc.
     */
    List getPlatformNames(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String queryString = "select distinct p.platform as platform from ${sampleTable} s " +
                "  inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "  left join ${legTable} l on s.leg_id = l.id " +
                "${criteria.whereClause} order by platform"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteria.values)
        return resultSet['platform']
    }


    List<Cruise> getCruises(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)
        String sqlStmt = "select distinct c.cruise_name as cruise, l.leg_name as leg, p.platform as platform, f.facility_code as facility_code from ${sampleTable} s " +
                "  inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "  left join ${legTable} l on s.leg_id = l.id" +
                "${criteria.whereClause} order by cruise, platform, leg, facility_code"
        return jdbcTemplate.query(sqlStmt, new BeanPropertyRowMapper(Cruise.class), *criteria.values)
    }


    // there can be multiple cruises with identical IDs
    List<Cruise> getCruiseById(String cruise) {
        String sqlStmt = "select distinct c.cruise_name as cruise, l.leg_name as leg, p.platform as platform, f.facility_code as facility_code from ${sampleTable} s " +
                "  inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "  left join ${legTable} l on s.leg_id = l.id " +
                "where c.cruise_name = ? or l.leg_name = ?"
        List<Cruise> results = jdbcTemplate.query(sqlStmt, new BeanPropertyRowMapper(Cruise.class), cruise, cruise)

        if (results.size() == 0) {
            throw new GeosamplesResourceNotFoundException('invalid cruise ID')
        }
        if (results.size() > 1) {
            log.debug("${results.size()} records found for cruise ${cruise}")
        }
        for (result in results) {
            result.addLinks(getCruiseLinks(result.cruise, result.platform, result.leg))
        }
        return results
    }


    // there should be only one cruise with given ID and platform
    Cruise getCruiseByIdAndPlatform(String cruise, String platform) {
        String sqlStmt = "select distinct c.cruise_name as cruise, l.leg_name as leg, p.platform as platform, f.facility_code as  facility_code from ${sampleTable} s " +
                "  inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "  inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "  inner join ${platformTable} p on cp.platform_id = p.id " +
                "  inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "  inner join ${facilityTable} f on cf.facility_id = f.id " +
                "  left join ${legTable} l on s.leg_id = l.id " +
                " where (c.cruise_name = ? or l.leg_name = ?) and p.platform = ?"
        List<Cruise> results = jdbcTemplate.query(sqlStmt, new BeanPropertyRowMapper(Cruise.class), cruise, cruise, platform)

        if (results.size() == 0) {
            throw new GeosamplesResourceNotFoundException('no cruise with this Id and Platform')
        }
        if (results.size() > 1) {
            // TODO different exception?
            //  this is not really a bad request as much as invalid assumption about the underlying database. Perhaps an
            //  invalidstate exception instead?
            throw new GeosamplesBadRequestException('Id and Platform should uniquely identify a cruise')
        }
        Cruise result = results[0]
        result.addLinks(getCruiseLinks(result.cruise, result.platform, result.leg))
        return result
    }


    List getCruiseLinks(cruise, platform, leg) {
        // cruise, platform should never be null
        if (! leg) {
            String sqlStmt = """select 
        cl.datalink as link, cl.link_level as linklevel, cl.link_source as source, cl.link_type as type 
        from ${cruiseLinksTable} cl 
            inner join ${cruisePlatformTable} cp on cl.cruise_platform_id = cp.id
            inner join ${platformTable} p on cp.platform_id = p.id
            inner join ${cruiseTable} c on cp.cruise_id = c.id
            left join ${legTable} l on cl.leg_id = l.id
            where c.cruise_name = ? and p.platform = ? and cl.leg_id is null"""
            return jdbcTemplate.queryForList(sqlStmt, cruise, platform)
        } else {
            String sqlStmt = """select 
        cl.datalink as link, cl.link_level as linklevel, cl.link_source as source, cl.link_type as type 
        from ${cruiseLinksTable} cl 
            inner join ${cruisePlatformTable} cp on cl.cruise_platform_id = cp.id
            inner join ${platformTable} p on cp.platform_id = p.id
            inner join ${cruiseTable} c on cp.cruise_id = c.id
            left join ${legTable} l on cl.leg_id = l.id
            where c.cruise_name = ? and p.platform = ? and l.leg_name = ?"""
            return jdbcTemplate.queryForList(sqlStmt, cruise, platform, leg)
        }
    }


    Map getDepthRange(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams, ['water_depth is not null'])
        String sqlStmt = "select min(s.water_depth) as \"MIN(WATER_DEPTH)\", max(s.water_depth) as \"MAX(WATER_DEPTH)\" from ${sampleTable} s " +
                "inner join ${cruiseTable} c on s.cruise_id = c.id " +
                "   inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id " +
                "   inner join ${platformTable} p on cp.platform_id = p.id " +
                "   inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id " +
                "   inner join ${facilityTable} f on cf.facility_id = f.id " +
                "   left join ${legTable} l on s.leg_id = l.id " +
                "${criteria.whereClause}"
        log.debug(sqlStmt)
        return jdbcTemplate.queryForMap(sqlStmt, *criteria.values)
    }


    def logCriteriaValues(List criteriaValues) {
        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }
    }

}