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
    @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String sampleTable
    @Value('${geosamples.facility_table: mud.curators_facility}') String facilityTable


    List<Facility> getRepositories(GeosampleSearchParameterObject searchParams) {
        Map criteria = this.searchParamsHelper.buildWhereClauseAndCriteriaList(searchParams)

        //drive query from curators_sample_tsqp since only it has many of the parameters to support search criteria
        String queryString = """select sample_count, b.facility_code, b.facility, b.facility_comment
            from
            (select count(*) as sample_count, facility_code from ${sampleTable} a ${criteria.whereClause} group by facility_code) a
            inner join
            (select facility_code, facility, facility_comment from ${facilityTable}) b
            on a.facility_code = b.facility_code order by facility_code"""
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
        String queryString = """select distinct a.facility_code, b.facility
           from ${sampleTable} a inner join ${facilityTable} b
           on a.FACILITY_CODE = b.FACILITY_CODE ${criteria.whereClause} order by facility_code"""
        return jdbcTemplate.queryForList(queryString, *criteria.values)
    }
}
