package noaa.ncei.ogssd.geosamples.repository

import Repository

/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 *
 */

@Repository
class DeviceRepository extends BaseRepository {
    static final String TABLENAME = 'curators_sample_tsqp'
    static final String recordsQueryString = "select distinct device from ${noaa.ncei.ogssd.geosamples.repository.BaseRepository.SCHEMA}.${TABLENAME}"
    static final String countQueryString = "select count(distinct device) from ${noaa.ncei.ogssd.geosamples.repository.BaseRepository.SCHEMA}.${TABLENAME}"
    static final String orderByClause = " order by device"
    List defaultCriteria = []

}
