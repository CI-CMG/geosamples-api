package noaa.ncei.ogssd.geosamples

import groovy.util.logging.Slf4j

/*
 * demo of common database access methods using database-specific properties in conditional beans
 */
@Slf4j
class AbstractDemoRepository {

    List getRecords() {
        log.debug("inside getRecords method using ${DIALECT}. Table name is ${SAMPLE_TABLE}.")
        return []
    }


}
