package noaa.ncei.ogssd.geosamples

import groovy.util.logging.Slf4j
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Slf4j
@Component
@Profile("postgres")
class PostgresPropertiesBean implements DatabaseProperties {
    static final String SCHEMA = 'public'
    static final String DIALECT = 'PostgreSQL'
//    static final String SAMPLE_TABLE = 'curators_sample_tsqp'
//    static final String INTERVAL_TABLE = 'curators_interval'

    PostgresPropertiesBean() {
        log.debug('setting up Postgresql database...')
    }
}
