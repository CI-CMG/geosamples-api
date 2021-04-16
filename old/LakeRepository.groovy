package noaa.ncei.ogssd.geosamples.repository

import Repository

/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 *
 */

@Repository
class LakeRepository extends BaseRepository {
    static final String TABLENAME = 'curators_sample_tsqp'
    static final String recordsQueryString =
        "select distinct lake from ${noaa.ncei.ogssd.geosamples.repository.BaseRepository.SCHEMA}.${TABLENAME}"
    static final String countQueryString =
        "select count(distinct lake) from ${noaa.ncei.ogssd.geosamples.repository.BaseRepository.SCHEMA}.${TABLENAME}"
    static final String orderByClause = " order by lake"
    List defaultCriteria = ["lake is not null"]

}
