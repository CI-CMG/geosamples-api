package noaa.ncei.ogssd.geosamples

import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/*
 * demo of common database access methods using database-specific properties in conditional beans
 */
@Slf4j
@Component
@Profile("postgres")
class PostgresDemoRepository extends AbstractDemoRepository implements DemoRepository {
    static final String DIALECT = "PostgreSQL"
    static final String SAMPLE_TABLE = 'PUBLIC.CURATORS_SAMPLE_TSQP'

    List databaseSpecificMethod() {
        log.debug('inside Postgres-specific method...')
        return []
    }
}
