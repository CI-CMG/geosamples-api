package gov.noaa.ncei.geosamples.api.repository

import gov.noaa.ncei.geosamples.api.ServiceProperties
import groovy.util.logging.Slf4j
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject
import gov.noaa.ncei.geosamples.api.error.GeosamplesResourceNotFoundException
import gov.noaa.ncei.geosamples.api.model.Facility
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
class FacilityRepository {

    private final SearchParamsHelper searchParamsHelper
    private final JdbcTemplate jdbcTemplate

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
    FacilityRepository(SearchParamsHelper searchParamsHelper, JdbcTemplate jdbcTemplate, ServiceProperties properties) {
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


    List<Facility> getRepositories(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)

        //drive query from curators_sample_tsqp since only it has many of the parameters to support search criteria
        String queryString = """select a.sample_count as sample_count, f.facility_code as facility_code, f.facility as facility, f.facility_comment as facility_comment
            from
            (select count(*) as sample_count, f.id as id from ${sampleTable} s
               inner join ${cruiseTable} c on s.cruise_id = c.id 
               inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id
               inner join ${platformTable} p on cp.platform_id = p.id
               inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
               inner join ${facilityTable} f on cf.facility_id = f.id 
               left join ${legTable} l on s.leg_id = l.id 
              ${criteria.whereClause} group by f.id) a
            inner join ${facilityTable} f on a.id = f.id
            order by facility_code"""
        log.debug(queryString)
        return jdbcTemplate.query(queryString, new BeanPropertyRowMapper(Facility.class), *criteria.values)
    }


    Facility getRepositoryById(String id) {
        String queryString = "select * from ${facilityTable} where facility_code = ?"
        try {
            return jdbcTemplate.queryForObject(queryString, new BeanPropertyRowMapper(Facility.class), id)
        } catch (EmptyResultDataAccessException e) {
            throw new GeosamplesResourceNotFoundException('invalid repository ID')
        }
    }


    /**
     * return only the list of names/codes, generally used to populate Select lists in webapp
     */
    List getRepositoryNames(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)

        //show only facility names actually used in IMLGS
        String queryString = """select distinct f.facility_code as facility_code, f.facility as facility
           from ${sampleTable} s 
               inner join ${cruiseTable} c on s.cruise_id = c.id 
               inner join ${cruisePlatformTable} cp on s.cruise_platform_id = cp.id
               inner join ${platformTable} p on cp.platform_id = p.id
               inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
               inner join ${facilityTable} f on cf.facility_id = f.id 
               left join ${legTable} l on s.leg_id = l.id 
           ${criteria.whereClause} order by facility_code"""
        return jdbcTemplate.queryForList(queryString, *criteria.values)
    }
}
