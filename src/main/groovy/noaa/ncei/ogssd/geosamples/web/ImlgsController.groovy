package noaa.ncei.ogssd.geosamples.web


import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag
import noaa.ncei.ogssd.geosamples.GeosamplesBadRequestException
import noaa.ncei.ogssd.geosamples.GeosamplesDTO
import noaa.ncei.ogssd.geosamples.repository.FacilityRepository
import noaa.ncei.ogssd.geosamples.repository.IntervalRepository
import noaa.ncei.ogssd.geosamples.repository.SampleRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import noaa.ncei.ogssd.geosamples.domain.Facility

@Slf4j
@Tag(name="geosamples", description="Index to Marine and Lacustrine Geological Samples (IMLGS) API")
@OpenAPIDefinition(
        servers = [ @Server(url="http://localhost:8080/geosamples-api/", description="embedded server"),
                    @Server(url="http://localhost/geosamples-api/", description="development server")]
)
@RestController
@Validated
class ImlgsController {
    @Autowired
    SampleRepository sampleRepository

    @Autowired
    FacilityRepository facilityRepository

    @Autowired
    IntervalRepository intervalRepository

//    @Autowired
//    ObjectMapper objectMapper

//    @Autowired
//    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter

//    @PostConstruct
//    void init() {
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
//    }

    @Operation(summary="Find geosamples. Warning: large response without criteria")
    @CrossOrigin
    @GetMapping("/samples")
    def getSamples(
            @RequestParam(defaultValue="false", value="count_only") boolean countOnly,
            @RequestParam(defaultValue="false", value="full_record") boolean fullRecord,
            @RequestParam(defaultValue="json") String format,
            @Valid GeosamplesDTO searchParams,
            HttpServletResponse response
    ) {
        if (! (format == 'json' || format == 'csv')) {
            log.error("only json and csv accepted for output format")
            throw new GeosamplesBadRequestException("only json and csv accepted for output format")
        }

        if (countOnly) {
            return convertPropertyNamesToLowerCase(sampleRepository.getSamplesCount(searchParams))
        } else if (format == 'csv') {
            printCSV(response, sampleRepository.getRawSamples(searchParams))
            return
        } else if (fullRecord) {
            return sampleRepository.getSamples(searchParams)
        } else {
            return sampleRepository.getDisplayRecords(searchParams)
        }
    }


    @Operation(summary="Find individual geosample specified by ID")
    @CrossOrigin
    @GetMapping("/samples/{id}")
    def getSampleById(@PathVariable String id) {
        // IMLGS id in format of "imlgs0000001"
        if (id.length() != 12 || ! id.startsWith('imlgs')) {
            throw new GeosamplesBadRequestException('invalid IMLGS ID')
        }
        // imlgs uniquely identifies sample
        return sampleRepository.getSampleById(id)
    }


    @Operation(summary="Find all storage methods used in the IMLGS")
    @CrossOrigin
    @GetMapping("/storage_methods")
    def getStorageMethods(@Valid GeosamplesDTO searchParams) {
        if (searchParams.storageMethod) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: storage_method")
        }
        return sampleRepository.getUniqueStorageMethods(searchParams)
    }


    @Operation(summary="Find all physiographic provinces used in the IMLGS")
    @CrossOrigin
    @GetMapping("/physiographic_provinces")
    def getPhysiographicProvinces(@Valid GeosamplesDTO searchParams) {
        if (searchParams.province) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: province")
        }
        return sampleRepository.getUniquePhysiographicProvinces(searchParams)
    }


    @Operation(summary="Find all sampling devices used in the IMLGS")
    @CrossOrigin
    @GetMapping("/devices")
    def getDeviceNames(@Valid GeosamplesDTO searchParams) {
        if (searchParams.device) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: device")
        }
        return sampleRepository.getDeviceNames(searchParams)
    }


    @Operation(summary="Find all lakes referenced in the IMLGS")
    @CrossOrigin
    @GetMapping("/lakes")
    def getLakes(@Valid GeosamplesDTO searchParams) {
        if (searchParams.lake) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: lake")
        }
        return sampleRepository.getLakes(searchParams)
    }


    @Operation(summary="Find all IGSN numbers used in the IMLGS")
    @CrossOrigin
    @GetMapping("/igsn")
    def getIsgnValues(@Valid GeosamplesDTO searchParams) {
        if (searchParams.igsn) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: igsn")
        }
        return sampleRepository.getIgsnValues(searchParams)
    }


    @Operation(summary="Find all cruises referenced in the IMLGS")
    @Hidden
    @CrossOrigin
    @GetMapping("/cruises")
    def getCruiseNames(
        @RequestParam(defaultValue="false", value="name_only") boolean nameOnly,
        @Valid GeosamplesDTO searchParams
    ) {
        if (searchParams.cruise) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: cruise")
        }
        if (nameOnly == true) {
            return sampleRepository.getCruiseNames(searchParams)
        } else {
            return sampleRepository.getCruises(searchParams)
        }
    }


    @Operation(summary="Find individual cruise or leg specified by ID")
    @CrossOrigin
    @GetMapping("/cruises/{id}")
    def getCruiseById(@PathVariable String id) {
        return sampleRepository.getCruiseById(id)
    }


    @Operation(summary="Find all repositories used in the IMLGS")
    @CrossOrigin
    @GetMapping("/repositories")
    def getRepositories(
        @RequestParam(defaultValue="false", value="name_only") boolean nameOnly,
        @Valid GeosamplesDTO searchParams
    ) {
        if (searchParams.repository) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: repository")
        }
        if (nameOnly == true) {
            return convertPropertyNamesToLowerCase(facilityRepository.getRepositoryNames(searchParams))
        } else {
            return facilityRepository.getRepositories(searchParams)
        }
    }


    @Operation(summary="Find repository with specified ID")
    @CrossOrigin
    @GetMapping("/repositories/{id}")
    def getRepositoryById(@PathVariable String id) {
        return facilityRepository.getRepositoryById(id)
    }


    @Operation(summary="List the unique platform names referenced in the IMLGS")
    @CrossOrigin
    @GetMapping("/platforms")
    List getPlatforms(@Valid GeosamplesDTO searchParams) {
        if (searchParams.platform) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: platform")
        }
        return  sampleRepository.getPlatformNames(searchParams)
    }


    @Operation(summary="Find interval records in the IMLGS. Warning: large response without criteria")
    @Hidden
    @CrossOrigin
    @GetMapping("/intervals")
    def getIntervals(
            @RequestParam(defaultValue="false", value="count_only") boolean countOnly,
            @Valid GeosamplesDTO searchParams,
            HttpServletResponse response
    ) {
        if (countOnly == true) {
            return convertPropertyNamesToLowerCase(intervalRepository.getIntervalsCount(searchParams))
        } else {
            return convertPropertyNamesToLowerCase(intervalRepository.getIntervals(searchParams))
        }
    }


    @Operation(summary="Find lithology values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/lithologies")
    List getLithologies(@Valid GeosamplesDTO searchParams) {
        if (searchParams.lithology) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: lithology")
        }
        return intervalRepository.getUniqueLithologyValues(searchParams)
    }


    @Operation(summary="Find texture values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/textures")
    List getTextures(@Valid GeosamplesDTO searchParams) {
        if (searchParams.texture) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: texture")
        }
        return intervalRepository.getUniqueTextureValues(searchParams)
    }


    @Operation(summary="Find mineralogy values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/mineralogies")
    List getMineralogies(@Valid GeosamplesDTO searchParams) {
        if (searchParams.mineralogy) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: mineralogy")
        }
        return intervalRepository.getUniqueMineralogyValues(searchParams)
    }


    @Operation(summary="Find weathering values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/weathering")
    List getWeatheringValues(@Valid GeosamplesDTO searchParams) {
        if (searchParams.weathering) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: weathering")
        }
        return intervalRepository.getUniqueWeatheringValues(searchParams)
    }


    @Operation(summary="Find metamorphism values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/metamorphism")
    List getMetamorphismValues(@Valid GeosamplesDTO searchParams) {
        if (searchParams.metamorphism) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: metamorphism")
        }
        return intervalRepository.getUniqueMetamorphismValues(searchParams)
    }


    @Operation(summary="Find geologic ages referenced in the IMLGS")
    @CrossOrigin
    @GetMapping("/geologic_ages")
    List getGeologicAgeValues(@Valid GeosamplesDTO searchParams) {
        if (searchParams.age) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: age")
        }
        return intervalRepository.getUniqueGeologicAgeValues(searchParams)
    }


    /**
     * helper method to download samples in CSV format
     * TODO move out of Controller and into Service?
     */
    static void  printCSV(HttpServletResponse response, resultset) {
        // TODO embed timestamp in filename?
        String outputFilename = "geosamples_export.csv"
        response.setContentType("text/csv")
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${outputFilename}")

        List csvHeaders = new ArrayList(resultset[0].keySet())
        CSVPrinter csvPrinter = new CSVPrinter(response.writer, CSVFormat.DEFAULT.withHeader(*csvHeaders))
        try {
            List values
            resultset.each { row ->
                csvPrinter.printRecord(row.values())
            }
        } catch (IOException e) {
            log.error(e.message)
        } finally {
            if (csvPrinter) { csvPrinter.close() }
        }
    }


    /*
     * hack to work around problem w/ spring.jackson.property-naming-strategy not working.
     * considerable memory use considerations
     */
    static List<Map> convertPropertyNamesToLowerCase(List<Map> resultSet) {
        List newResultSet = resultSet.collect { row ->
            row.collectEntries { key, value ->
                [(key.toLowerCase()): value]
            }
        }
        return newResultSet
    }

    static Map convertPropertyNamesToLowerCase(Map resultSet) {
        return resultSet.collectEntries { key, value ->  [(key.toLowerCase()): value] }
    }
}
