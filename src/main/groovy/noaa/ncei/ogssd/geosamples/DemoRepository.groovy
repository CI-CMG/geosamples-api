package noaa.ncei.ogssd.geosamples

// used to type the conditional repository beans
interface DemoRepository {
    List getRecords()
    List databaseSpecificMethod()
}