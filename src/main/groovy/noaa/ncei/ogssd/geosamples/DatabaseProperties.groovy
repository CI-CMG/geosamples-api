package noaa.ncei.ogssd.geosamples

interface DatabaseProperties {
    static final String SCHEMA
    static final String DIALECT
    static final String SAMPLE_TABLE = 'curators_sample_tsqp'
    static final String INTERVAL_TABLE = 'curators_interval'
}