package gov.noaa.ncei.geosamples.api.repository


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
    @Autowired
    SearchParamsHelper searchParamsHelper

    @Autowired
    JdbcTemplate jdbcTemplate

    // inject values from application-<profilename>.properties
    @Value('${geosamples.sample_table}') String sampleTable
    @Value('${geosamples.facility_table}') String facilityTable
    @Value('${geosamples.cruise_facility_table}') String cruiseFacilityTable


    List<Facility> getRepositories(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)

        //drive query from curators_sample_tsqp since only it has many of the parameters to support search criteria
        String queryString = """select a.sample_count as sample_count, f.facility_code as facility_code, f.facility as facility, f.facility_comment as facility_comment
            from
            (select count(*) as sample_count, f.id as id from ${sampleTable} s
               inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
               inner join ${facilityTable} f on cf.facility_id = f.id
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
               inner join ${cruiseFacilityTable} cf on s.cruise_facility_id = cf.id 
               inner join ${facilityTable} f on cf.facility_id = f.id
           ${criteria.whereClause} order by facility_code"""
        return jdbcTemplate.queryForList(queryString, *criteria.values)
    }
}
