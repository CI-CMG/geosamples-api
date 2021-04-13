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
class SampleRepository extends BaseRepository {
    static final String TABLENAME = 'curators_sample_tsqp'
    final String recordsQueryString
    final String countQueryString
    static final String orderByClause = " order by cruise, begin_date, leg, sample, device"
    List defaultCriteria = []
    private String schema


    // inject value from application.properties
    SampleRepository(@Value('${geosamples.schema: mud}') String schema) {
        this.schema = schema
        this.recordsQueryString = "select * from ${schema}.${TABLENAME}"
        this.countQueryString = "select count(*) from ${schema}.${TABLENAME}"
    }


    Map<String, Object> getRecordById(String id) {
        String queryString = "select * from ${schema}.${TABLENAME} where imlgs = ?"
        try {
            def result = jdbcTemplate.queryForMap(queryString, id)
            return result
        } catch (EmptyResultDataAccessException e) {
            throw new GeosamplesResourceNotFoundException('invalid IMLGS ID')
        }

    }
}