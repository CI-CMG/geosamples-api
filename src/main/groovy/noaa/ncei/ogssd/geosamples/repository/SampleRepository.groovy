package noaa.ncei.ogssd.geosamples.repository

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.GeosamplesDTO
import noaa.ncei.ogssd.geosamples.GeosamplesResourceNotFoundException
import noaa.ncei.ogssd.geosamples.domain.Cruise
import noaa.ncei.ogssd.geosamples.domain.Facility
import noaa.ncei.ogssd.geosamples.domain.Sample
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
    @Autowired
    JdbcTemplate jdbcTemplate

    static final String orderByClause = " order by cruise, begin_date, leg, sample, device"
    private final String intervalTable
    private final String sampleTable
    private final String facilityTable
    private final String linksTable
    private final String cruiseLinksTable

    // skip SHAPE column since there are problems serializing SDO_Geometry into JSON
//    private final List allSampleFields = [
//        'facility_code', 'ship_code', 'platform', 'cruise', 'sample', 'device', 'begin_date', 'end_date', 'lat',
//        'latdeg', 'latmin', 'ns', 'end_lat', 'end_latdeg', 'end_latmin', 'end_ns', 'lon', 'londeg', 'lonmin', 'ew',
//        'end_lon', 'end_londeg', 'end_lonmin', 'end_ew', 'latlon_orig', 'water_depth', 'end_water_depth',
//        'storage_meth', 'cored_length', 'cored_length_mm', 'cored_diam', 'cored_diam_mm', 'pi', 'province', 'lake',
//        'other_link', 'last_update', 'igsn', 'leg', 'sample_comments', 'publish', 'previous_state', 'objectid',
//        'show_sampl', 'imlgs'
//    ]

    private final List allSampleFields = [
            'facility_code', 'ship_code', 'platform', 'cruise', 'sample', 'device', 'begin_date', 'end_date', 'lat',
            'end_lat', 'lon', 'end_lon', 'latlon_orig', 'water_depth', 'end_water_depth',
            'storage_meth', 'cored_length', 'cored_length_mm', 'cored_diam', 'cored_diam_mm', 'pi', 'province', 'lake',
            'other_link', 'last_update', 'igsn', 'leg', 'sample_comments', 'objectid',
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
        @Value('${geosamples.interval_table: mud.curators_interval}') String intervalTable,
        @Value('${geosamples.facility_table: mud.curators_facility}') String facilityTable,
        @Value('${geosamples.links_table: mud.curators_sample_links}') String linksTable,
        @Value('${geosamples.cruise_links_table: mud.curators_cruise_links}') String cruiseLinksTable
    ) {
        // fully qualified table names
        this.intervalTable = intervalTable
        this.sampleTable = sampleTable
        this.facilityTable = facilityTable
        this.linksTable = linksTable
        this.cruiseLinksTable = cruiseLinksTable
    }


    List<Sample> getSamples(GeosamplesDTO searchParams) {
        log.debug("inside getSamples with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues
        logCriteriaValues(criteriaValues)
        String sqlStmt = "select ${allSampleFields.join(', ')} from ${sampleTable} ${whereClause} ${orderByClause}"
        log.debug(sqlStmt)
        // error if pass null as criteriaValues
        return jdbcTemplate.query(sqlStmt, new BeanPropertyRowMapper(Sample.class), *criteriaValues)
    }


    List getRawSamples(GeosamplesDTO searchParams) {
        log.debug("inside getRawSamples with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues
        logCriteriaValues(criteriaValues)
        String sqlStmt = "select ${allSampleFields.join(', ')} from ${sampleTable} ${whereClause} ${orderByClause}"
        log.debug(sqlStmt)
        // error if pass null as criteriaValues
        return jdbcTemplate.queryForList(sqlStmt, *criteriaValues)
    }


    // TODO return bare count
    Map<String,Object> getSamplesCount(GeosamplesDTO searchParams) {
        log.debug("inside getSamplesCount with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues
        logCriteriaValues(criteriaValues)
        String sqlStmt = "select count(*) as count from ${sampleTable} ${whereClause}"
        return jdbcTemplate.queryForMap(sqlStmt, *criteriaValues)
    }


    Sample getSampleById(String id) {
        List qualifiedFields = allSampleFields.collect { "a.${it}"}
        String sqlStmt = """select ${qualifiedFields.join(', ')}, b.facility  
        from ${sampleTable} a inner join ${facilityTable} b on a.FACILITY_CODE = b.FACILITY_CODE where imlgs = ?"""
        def sample
        try {
            sample = jdbcTemplate.queryForObject(sqlStmt, new BeanPropertyRowMapper(Sample.class), id)
        } catch (EmptyResultDataAccessException e) {
            throw new GeosamplesResourceNotFoundException('invalid IMLGS ID')
        }
        sample.addLinks(getLinksById(id))
        sample.addIntervals(getIntervalsByImlgsId(id))
        sample.addCruiseLinks(getCruiseLinks(sample.cruise, sample.platform, sample.leg))
        return sample
    }


    List getLinksById(String id) {
        String sqlStmt = """select 
        datalink as link, link_level as level, link_source as source, link_type as type 
        from ${linksTable} where imlgs = ?"""
        return jdbcTemplate.queryForList(sqlStmt, id)
    }


    List getIntervalsByImlgsId(String id) {
        String sqlStmt = """select 
       interval, depth_top, depth_top_mm, depth_bot, depth_bot_mm, dhcore_id, dhcore_length, dhcore_length_mm,
       dhcore_interval, dtop_in_dhcore, dtop_mm_in_dhcore, dbot_in_dhcore, dbot_mm_in_dhcore, lith1, text1,
       lith2, text2, comp1, comp2, comp3, comp4, comp5, comp6, description, age, absolute_age_top,
       absolute_age_bot, weight, rock_lith, rock_min, weath_meta, remark, munsell_code, munsell, exhaust_code,
       photo_link, unit_number, int_comments, dhdevice, cmcd_top, mmcd_top, cmcd_bot, mmcd_bot
        from ${intervalTable} where imlgs = ?"""
        return jdbcTemplate.queryForList(sqlStmt, id)
    }


    // TODO combine w/ getSampleRecords
    List<Sample> getDisplayRecords(GeosamplesDTO searchParams) {
        log.debug("inside getDisplayRecords with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues
        logCriteriaValues(criteriaValues)
        String sqlStmt = """select ${displaySampleFields.join(', ')} from ${sampleTable} ${whereClause} 
            ${orderByClause} offset ${searchParams.offset} rows fetch next ${searchParams.pageSize} rows only"""
        log.debug(sqlStmt)
        return jdbcTemplate.query(sqlStmt, new BeanPropertyRowMapper(Sample.class), *criteriaValues)
    }


    /**
     * return a list the unique storage methods (storage_meth) values used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniqueStorageMethods(GeosamplesDTO searchParams) {
        log.debug("inside getUniqueStorageMethods with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['storage_meth is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = "select distinct storage_meth from ${sampleTable} ${whereClause} order by storage_meth"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['storage_meth']
    }


    /**
     * return a list the unique physiographic province names (province) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getUniquePhysiographicProvinces(GeosamplesDTO searchParams) {
        log.debug("inside getUniquePhysiographicProvinces with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['province is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = "select distinct province from ${sampleTable} ${whereClause} order by province"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['province']
    }


    /**
     * return a list the unique devices (device) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getDeviceNames(GeosamplesDTO searchParams) {
        log.debug("inside getDeviceNames with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['device is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = "select distinct device from ${sampleTable} ${whereClause} order by device"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['device']
    }


    /**
     * return a list the unique lake names (lake) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getLakes(GeosamplesDTO searchParams) {
        log.debug("inside getLakes with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['lake is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = "select distinct lake from ${sampleTable} ${whereClause} order by lake"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['lake']
    }


    /**
     * return a list the unique lake names (lake) used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getIgsnValues(GeosamplesDTO searchParams) {
        log.debug("inside getIgsnValues with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['igsn is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = "select distinct igsn from ${sampleTable} ${whereClause} order by igsn"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['igsn']
    }


    /**
     * return a list the unique cruise names used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. platform, repository, etc.
     */
    List getCruiseNames(GeosamplesDTO searchParams) {
        log.debug("inside getCruiseNames with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues

        // combine cruise and leg values into response
        String queryString = """select distinct cruise from (
            (select distinct cruise from ${sampleTable} ${whereClause})
            union
            (select distinct leg as cruise from ${sampleTable} ${whereClause})
        ) a where cruise is not null order by cruise"""
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues, *criteriaValues)
        log.debug("${resultSet.size()} unique cruise/leg names found")
        return (resultSet.findAll { it['cruise']}['cruise'])
    }


    /**
     * return a list the unique platform names used in the curators_sample table.  Results
     * may be constrained by search parameters, e.g. repository, etc.
     */
    List getPlatformNames(GeosamplesDTO searchParams) {
        log.debug("inside getPlatformNames with ${searchParams}")
        String whereClause = searchParams.getWhereClause(['platform is not null'])
        List criteriaValues = searchParams.criteriaValues
        String queryString = "select distinct platform from ${sampleTable} ${whereClause} order by platform"
        def resultSet = jdbcTemplate.queryForList(queryString, *criteriaValues)
        return resultSet['platform']
    }


    List<Cruise> getCruises(GeosamplesDTO searchParams) {
        String whereClause = searchParams.getWhereClause()
        List criteriaValues = searchParams.criteriaValues
        String sqlStmt = "select distinct cruise, leg, platform, facility_code from ${sampleTable} ${whereClause} order by cruise, platform, leg, facility_code"
        return jdbcTemplate.query(sqlStmt, new BeanPropertyRowMapper(Cruise.class), *criteriaValues)
    }


    Cruise getCruiseById(String cruise) {
        String sqlStmt = "select distinct cruise, leg, platform, facility_code from ${sampleTable} where cruise = ? or leg = ?"
//        def resultSet = jdbcTemplate.queryForList(queryString, cruise, cruise)
        List<Cruise> results = jdbcTemplate.query(sqlStmt, new BeanPropertyRowMapper(Cruise.class), cruise, cruise)

        if (results.size() == 0) {
            throw new GeosamplesResourceNotFoundException('invalid cruise ID')
        }
        // cruise ID and platform required to uniquely identify record?
        if (results.size() > 1) {
            log.warn("${results.size()} records found for cruise ${cruise}. Returning the first one.")
        }
        Cruise result = results[0]
        result.addLinks(getCruiseLinks(result.cruise, result.platform, result.leg))
        return result
    }


    List getCruiseLinks(cruise, platform, leg) {
        // cruise, platform should never be null
        if (! leg) {
            String sqlStmt = """select 
        datalink as link, link_level as level, link_source as source, link_type as type 
        from ${cruiseLinksTable} where cruise = ? and platform = ? and leg is null"""
            return jdbcTemplate.queryForList(sqlStmt, cruise, platform)
        } else {
            String sqlStmt = """select 
        datalink as link, link_level as level, link_source as source, link_type as type 
        from ${cruiseLinksTable} where cruise = ? and platform = ? and leg = ?"""
            return jdbcTemplate.queryForList(sqlStmt, cruise, platform, leg)
        }
    }


    def logCriteriaValues(List criteriaValues) {
        if (criteriaValues) {
            log.debug(criteriaValues.toListString())
        } else {
            log.debug('no criteria values')
        }
    }
}