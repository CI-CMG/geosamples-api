package noaa.ncei.ogssd.geosamples.repository

import org.springframework.stereotype.Repository

/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 *
 */

@Repository
class PlatformRepository extends BaseRepository {
    static final String TABLENAME = 'curators_sample_tsqp'
    static final String recordsQueryString =
        "select distinct platform as name from ${SCHEMA}.${TABLENAME}"
    static final String countQueryString =
        "select count(distinct platform) from ${SCHEMA}.${TABLENAME}"
    static final String orderByClause = " order by platform"
    List defaultCriteria = ["platform is not null"]

}
