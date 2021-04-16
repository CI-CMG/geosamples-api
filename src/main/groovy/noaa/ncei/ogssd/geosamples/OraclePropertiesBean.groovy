package noaa.ncei.ogssd.geosamples

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!postgres")
class OraclePropertiesBean implements DatabaseProperties {
    public static final String SCHEMA = 'mud'

    OraclePropertiesBean() {
        println('setting up Oracle database...')
    }

}
