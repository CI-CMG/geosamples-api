package noaa.ncei.ogssd.geosamples.repository

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.GeosamplesResourceNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Repository


/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 */
@Slf4j
@Repository
class FacilityRepository extends BaseRepository {

    static final String TABLENAME = 'curators_sample_tsqp'
    static final String JOINTABLE = 'curators_facility'
    final String recordsQueryString
    final String countQueryString
    final String namesQueryString
    static final String orderByClause = " order by facility_code"
    List defaultCriteria = []
    private String schema


    // inject value from application.properties
    FacilityRepository( @Value('${geosamples.schema: mud}') String schema) {
        this.schema = schema
//        this.recordsQueryString = """select distinct a.facility_code, b.facility
//           from ${schema}.${TABLENAME} a inner join ${schema}.${JOINTABLE} b
//           on a.FACILITY_CODE = b.FACILITY_CODE"""

        this.recordsQueryString =
            """select sample_count, b.facility_code, b.facility, b.facility_comment 
            from 
            (select count(*) as sample_count, facility_code from ${schema}.${TABLENAME} group by facility_code) a
            full outer join 
            (select facility_code, facility, facility_comment from ${schema}.${JOINTABLE}) b 
            on a.facility_code = b.facility_code"""
        this.namesQueryString = """select distinct a.facility_code, b.facility
           from ${schema}.${TABLENAME} a inner join ${schema}.${JOINTABLE} b
           on a.FACILITY_CODE = b.FACILITY_CODE"""
        this.countQueryString = "select count(distinct facility_code) from ${schema}.${TABLENAME}"
    }


    Map<String,Object> getRecordById(String id) {
        // JOINTABLE is a misnomer in this case
        String queryString = "select * from ${schema}.${JOINTABLE} where facility_code = ?"
        try {
            def result = jdbcTemplate.queryForMap(queryString, id)
            return result
        } catch (EmptyResultDataAccessException e) {
            throw new GeosamplesResourceNotFoundException('invalid repository ID')
        }

    }
}
