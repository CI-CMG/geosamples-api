package gov.noaa.ncei.geosamples.api.controller

import gov.noaa.ncei.geosamples.api.error.GeosamplesBadRequestException
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject
import gov.noaa.ncei.geosamples.api.repository.FacilityRepository
import gov.noaa.ncei.geosamples.api.repository.IntervalRepository
import gov.noaa.ncei.geosamples.api.repository.SampleRepository
import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@Tag(name="geosamples", description="retrieve Geosample records and related information ")
@OpenAPIDefinition(
        servers = [@Server(url="https://www.ngdc.noaa.gov/imlgs/api", description="public server")]
)
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api")
class ImlgsController {
    @Autowired
    SampleRepository sampleRepository

    @Autowired
    FacilityRepository facilityRepository

    @Autowired
    IntervalRepository intervalRepository


    @Operation(summary = "List Geosamples. Warning: without criteria to constrain results, response will be large")
//    @ApiResponses(value = [
//        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(mediaType = "application/json")),
//        @ApiResponse(responseCode = "400", description = "Invalid username supplied", content = @Content),
//        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)])
    @CrossOrigin
    @GetMapping("/samples")
    def getSamples (
            @RequestParam(defaultValue="false", value="count_only") boolean countOnly,
            @RequestParam(defaultValue="false", value="full_record") boolean fullRecord,
            @RequestParam(defaultValue="json") String format,
            @Valid GeosampleSearchParameterObject searchParams,
            HttpServletResponse response) {

        // TODO replace w/ declarative validation. Perhaps enum? see https://www.baeldung.com/spring-enum-request-param
        if (! (format == 'json' || format == 'csv')) {
            log.error("only json and csv accepted for output format")
            throw new GeosamplesBadRequestException("only json and csv accepted for output format")
        }
        println('inside getSamples with ' + searchParams.toString())
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
        // TODO use declarative validation, e.g. @Size(min=12, max = 12, message="invalid IMLGS Id") but
        // still throw custom exception?
        // IMLGS id in format of "imlgs0000001"
        if (id.length() != 12 || ! id.startsWith('imlgs')) {
            throw new GeosamplesBadRequestException('invalid IMLGS ID')
        }
        // imlgs uniquely identifies sample
        return sampleRepository.getSampleById(id)
    }


    @Operation(summary="Find the min/max water_depth of geosamples matching given search paramters")
    @CrossOrigin
    @GetMapping("/depth_range")
    def getDepthRange(@Valid GeosampleSearchParameterObject searchParams) {
        return sampleRepository.getDepthRange(searchParams)
    }


    @Operation(summary="Find all storage methods used in the IMLGS")
    @CrossOrigin
    @GetMapping("/storage_methods")
    def getStorageMethods(@Valid GeosampleSearchParameterObject searchParams) {
        if (searchParams.storageMethod) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: storage_method")
        }
        return sampleRepository.getUniqueStorageMethods(searchParams)
    }


    @Operation(summary="Find all physiographic provinces used in the IMLGS")
    @CrossOrigin
    @GetMapping("/physiographic_provinces")
    def getPhysiographicProvinces(@Valid GeosampleSearchParameterObject searchParams) {
        if (searchParams.province) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: province")
        }
        return sampleRepository.getUniquePhysiographicProvinces(searchParams)
    }


    @Operation(summary="Find all sampling devices used in the IMLGS")
    @CrossOrigin
    @GetMapping("/devices")
    def getDeviceNames(@Valid GeosampleSearchParameterObject searchParams) {
        if (searchParams.device) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: device")
        }
        return sampleRepository.getDeviceNames(searchParams)
    }


    @Operation(summary="Find all lakes referenced in the IMLGS")
    @CrossOrigin
    @GetMapping("/lakes")
    def getLakes(@Valid GeosampleSearchParameterObject searchParams) {
        if (searchParams.lake) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: lake")
        }
        return sampleRepository.getLakes(searchParams)
    }


    @Operation(summary="Find all IGSN numbers used in the IMLGS")
    @CrossOrigin
    @GetMapping("/igsn")
    def getIsgnValues(@Valid GeosampleSearchParameterObject searchParams) {
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
            @Valid GeosampleSearchParameterObject searchParams
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
        log.debug('getting cruise(s) by id...')
        return sampleRepository.getCruiseById(id)
    }


    @Operation(summary="Find individual cruise or leg specified by ID and platform")
    @CrossOrigin
    @GetMapping("/cruises/{id}/{platform}")
    def getCruiseById(@PathVariable String id, @PathVariable String platform) {
        log.debug('getting cruise by Id and platform')
        // returns a list of 1 or more cruises
        return sampleRepository.getCruiseByIdAndPlatform(id, platform)
    }

    @Operation(summary="Find all repositories used in the IMLGS")
    @CrossOrigin
    @GetMapping("/repositories")
    def getRepositories(
            @RequestParam(defaultValue="false", value="name_only") boolean nameOnly,
            @Valid GeosampleSearchParameterObject searchParams
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
    List getPlatforms(@Valid GeosampleSearchParameterObject searchParams) {
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
            @RequestParam(defaultValue="json") String format,
            @Valid GeosampleSearchParameterObject searchParams,
            HttpServletResponse response
    ) {
        // TODO replace w/ declarative validation. Perhaps enum? see https://www.baeldung.com/spring-enum-request-param
        if (! (format == 'json' || format == 'csv')) {
            log.error("only json and csv accepted for output format")
            throw new GeosamplesBadRequestException("only json and csv accepted for output format")
        }

        if (countOnly == true) {
            return convertPropertyNamesToLowerCase(intervalRepository.getIntervalsCount(searchParams))
        } else if (format == 'csv') {
            printCSV(response, intervalRepository.getIntervals(searchParams))
            return
        } else {
            return convertPropertyNamesToLowerCase(intervalRepository.getIntervals(searchParams))
        }
    }


    @Operation(summary="Find lithology values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/lithologies")
    List getLithologies(@Valid GeosampleSearchParameterObject searchParams) {
        if (searchParams.lithology) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: lithology")
        }
        return intervalRepository.getUniqueLithologyValues(searchParams)
    }


    @Operation(summary="Find texture values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/textures")
    List getTextures(@Valid GeosampleSearchParameterObject searchParams) {
        if (searchParams.texture) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: texture")
        }
        return intervalRepository.getUniqueTextureValues(searchParams)
    }


    @Operation(summary="Find mineralogy values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/mineralogies")
    List getMineralogies(@Valid GeosampleSearchParameterObject searchParams) {
        if (searchParams.mineralogy) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: mineralogy")
        }
        return intervalRepository.getUniqueMineralogyValues(searchParams)
    }


    @Operation(summary="Find weathering values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/weathering")
    List getWeatheringValues(@Valid GeosampleSearchParameterObject searchParams) {
        if (searchParams.weathering) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: weathering")
        }
        return intervalRepository.getUniqueWeatheringValues(searchParams)
    }


    @Operation(summary="Find metamorphism values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/metamorphism")
    List getMetamorphismValues(@Valid GeosampleSearchParameterObject searchParams) {
        if (searchParams.metamorphism) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: metamorphism")
        }
        return intervalRepository.getUniqueMetamorphismValues(searchParams)
    }


    @Operation(summary="Find geologic ages referenced in the IMLGS")
    @CrossOrigin
    @GetMapping("/geologic_ages")
    List getGeologicAgeValues(@Valid GeosampleSearchParameterObject searchParams) {
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
