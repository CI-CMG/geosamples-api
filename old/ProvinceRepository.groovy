package noaa.ncei.ogssd.geosamples.repository

import Repository

/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 *
 */

@Repository
class ProvinceRepository extends BaseRepository {
    static final String TABLENAME = 'curators_sample_tsqp'
    static final String recordsQueryString =
        "select distinct province from ${noaa.ncei.ogssd.geosamples.repository.BaseRepository.SCHEMA}.${TABLENAME}"
    static final String countQueryString =
        "select count(distinct platform) from ${noaa.ncei.ogssd.geosamples.repository.BaseRepository.SCHEMA}.${TABLENAME}"
    static final String orderByClause = " order by province"
    List defaultCriteria = ["province is not null"]

}
