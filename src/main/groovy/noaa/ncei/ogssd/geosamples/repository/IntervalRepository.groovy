package noaa.ncei.ogssd.geosamples.repository

import org.springframework.stereotype.Repository


/*
 * interface to database table(s).
 * WARNING: this class has a tight coupling to the underlying database schema
 */

@Repository
class IntervalRepository extends BaseRepository {
    static final String TABLENAME = 'curators_interval'
    static final String recordsQueryString = "select * from ${SCHEMA}.${TABLENAME}"
    static final String countQueryString = "select count(*) from ${SCHEMA}.${TABLENAME}"
    static final String orderByClause = " order by interval"
    List defaultCriteria = []

}

/*
select * from mud.curators_interval where imlgs='${sample.imlgs}' order by interval

select * from mud.curators_interval where facility_code='${sample.facility_code}' and cruise='${sample.cruise}'
and sample='${sample.sample}' and device='${sample.device}' order by interval
 */