package noaa.ncei.ogssd.geosamples.repository

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.GeosamplesDTO
import noaa.ncei.ogssd.geosamples.GeosamplesResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
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
    JdbcTemplate jdbcTemplate;
    private final String facilityTable
    private final String sampleTable

    // inject values from application-<profilename>.properties
    FacilityRepository(
            @Value('${geosamples.sample_table: mud.curators_sample_tsqp}') String sampleTable,
            @Value('${geosamples.facility_table: mud.curators_facility}') String facilityTable
    ) {
        // fully qualified table names
        this.facilityTable = facilityTable
        this.sampleTable = sampleTable
    }


    List getRepositories(GeosamplesDTO searchParams) {
        log.debug("inside getRepositories with ${searchParams}")
        jdbcTemplate.setResultsMapCaseInsensitive(true)
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues

        //drive query from curators_sample_tsqp since only it has many of the parameters to support search criteria
        String queryString = """select sample_count, b.facility_code, b.facility, b.facility_comment
            from
            (select count(*) as sample_count, facility_code from ${sampleTable} ${whereClause} group by facility_code) a
            inner join
            (select facility_code, facility, facility_comment from ${facilityTable}) b
            on a.facility_code = b.facility_code order by facility_code"""
        log.debug(queryString)
        return jdbcTemplate.queryForList(queryString, *criteriaValues)
    }


    Map<String,Object> getRepositoryById(String id) {
        jdbcTemplate.setResultsMapCaseInsensitive(true)
        String queryString = "select * from ${facilityTable} where facility_code = ?"
        try {
            return jdbcTemplate.queryForMap(queryString, id)
        } catch (EmptyResultDataAccessException e) {
            throw new GeosamplesResourceNotFoundException('invalid repository ID')
        }
    }


    /**
     * return only the list of names, generally used to populate Select lists in webapp
     */
    List getRepositoryNames(GeosamplesDTO searchParams) {
        jdbcTemplate.setResultsMapCaseInsensitive(true)
        log.debug("inside getRepositoryNames with ${searchParams}")
        String whereClause = searchParams.whereClause
        List criteriaValues = searchParams.criteriaValues

        //show only names actually used in IMLGS
        String queryString = """select distinct a.facility_code, b.facility
           from ${sampleTable} a inner join ${facilityTable} b
           on a.FACILITY_CODE = b.FACILITY_CODE ${whereClause} order by facility_code"""
        return jdbcTemplate.queryForList(queryString, *criteriaValues)
    }
}
