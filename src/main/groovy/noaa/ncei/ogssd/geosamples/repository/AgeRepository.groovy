package noaa.ncei.ogssd.geosamples.repository

import org.springframework.stereotype.Repository


/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 */

@Repository
class AgeRepository extends BaseRepository {
    static final String TABLENAME = 'curators_interval'
    static final String recordsQueryString = "select distinct age from ${SCHEMA}.${TABLENAME}"
    static final String countQueryString = "select count(distinct age) from ${SCHEMA}.${TABLENAME}"
    static final String orderByClause = " order by age"
    List defaultCriteria = []

}
