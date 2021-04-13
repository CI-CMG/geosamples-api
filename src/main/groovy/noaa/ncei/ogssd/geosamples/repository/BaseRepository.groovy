package noaa.ncei.ogssd.geosamples.repository

import groovy.sql.Sql
import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.GeosamplesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

import javax.sql.DataSource

/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 *
 */
@Slf4j
@Repository
abstract class BaseRepository {
    static final String SCHEMA = 'public'
//    static final String SCHEMA = 'mud'

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GeosamplesService geosamplesService

//    @Autowired
//    DataSource dataSource;
//    @Autowired
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    // use for native groovy.sql queries
//    def sql

//   //  create the groovy.sql.Sql instance in constructor since DataSource
//   //  component not available earlier
//    BaseRepository(DataSource dataSource) {
//        this.sql = Sql.newInstance(dataSource)
//    }

//    // default constructor required if the alternate above is used
//    BaseRepository() {}

    List getRecords(Map<String,Object>searchParameters) {
        log.debug("inside getRecords with ${searchParameters}")
        def response = geosamplesService.buildWhereClause(searchParameters, defaultCriteria)
        String whereClause = response[0]
        def criteriaValues = response[1]
        log.debug(recordsQueryString + whereClause + orderByClause)
        log.debug(criteriaValues)
        if (whereClause) {
            return jdbcTemplate.queryForList(recordsQueryString + whereClause + orderByClause, *criteriaValues)
        } else {
            return jdbcTemplate.queryForList(recordsQueryString + orderByClause)
        }
    }


    Map<String,Object> getCount(Map<String,Object>searchParameters) {
        log.debug("inside getCount with ${searchParameters}")
        def response = geosamplesService.buildWhereClause(searchParameters, defaultCriteria)
        String whereClause = response[0]
        def criteriaValues = response[1]
        String queryString = "select count(*) from ${SCHEMA}.${TABLENAME}"
        if (whereClause) {
            return jdbcTemplate.queryForMap(countQueryString + whereClause, *criteriaValues)
        } else {
            return jdbcTemplate.queryForMap(countQueryString)
        }
    }
}
