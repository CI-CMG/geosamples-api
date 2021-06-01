package noaa.ncei.ogssd.geosamples.web


import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.servers.Servers
import io.swagger.v3.oas.annotations.tags.Tag
import noaa.ncei.ogssd.geosamples.GeosamplesBadRequestException
import noaa.ncei.ogssd.geosamples.repository.FacilityRepository
import noaa.ncei.ogssd.geosamples.repository.IntervalRepository
import noaa.ncei.ogssd.geosamples.repository.SampleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
@Tag(name="geosamples", description="Index to Marine and Lacustrine Geological Samples (IMLGS) API")
@OpenAPIDefinition(
        servers = [ @Server(url="http://localhost:8080/geosamples-api/", description="embedded server"),
                    @Server(url="https://gisdev.ngdc.noaa.gov/geosamples-api/", description="test server"),
                    @Server(url="http://localhost/geosamples-api/", description="development server")]
)
//TODO not working
//@Servers(@Server(url="https://gisdev.ngdc.noaa.gov/geosamples-api/", description="development server"))

@RestController
//@RequestMapping("/geosamples-api")
class ImlgsController {
    @Autowired
    SampleRepository sampleRepository

    @Autowired
    FacilityRepository facilityRepository

    @Autowired
    IntervalRepository intervalRepository

    /*
    convenient alternative, but you lose the automatic type conversion and the
    request parameter names must match the variable names

    @GetMapping("/samples")
    def getSamples(@RequestParam Map<String,String> allParams) {
        return []
    }
    */

    @Operation(summary="Find geosamples. Warning: large response without criteria")
    @CrossOrigin
    @GetMapping("/samples")
    def getSamples(
        @RequestParam(defaultValue="false", value="count_only") boolean countOnly,
        @RequestParam(defaultValue="false", value="full_record") boolean fullRecord,
        @RequestParam(required=false) String repository,
        @RequestParam(required=false) String bbox,
        @RequestParam(required=false) String platform,
        @RequestParam(required=false) String lake,
        @RequestParam(required=false) String cruise,
        @RequestParam(required=false) String device,
        @RequestParam(required=false, value="start_date") String startDate,
        @RequestParam(required=false, value="min_depth") Float minDepth,
        @RequestParam(required=false, value="max_depth") Float maxDepth,
        @RequestParam(required=false) String igsn,
        @RequestParam(required=false) String lithology,
        @RequestParam(required=false) String texture,
        @RequestParam(required=false) String mineralogy,
        @RequestParam(required=false) String weathering,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        // cruise applies to both cruise and leg columns
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}
        if (igsn) { searchParams["igsn"] = igsn}
        if (lithology) { searchParams['lithology'] = lithology}
        if (texture) { searchParams['texture'] = texture}
        if (mineralogy) { searchParams['mineralogy'] = mineralogy}
        if (weathering) { searchParams['weathering'] = weathering}

        def resultSet
        if (countOnly) {
            resultSet = sampleRepository.getSamplesCount(searchParams)
        } else if (fullRecord) {
            resultSet = sampleRepository.getSamples(searchParams)
        } else {
            resultSet = sampleRepository.getDisplayRecords(searchParams)
        }
        return resultSet
    }


    @Operation(summary="Find individual geosample specified by ID")
    @CrossOrigin
    @GetMapping("/samples/{id}")
    def getSampleById(@PathVariable String id) {
        // TODO use annotation-based validation
        // IMLGS id in format of "imlgs0000001"
        if (id.length() != 12 || ! id.startsWith('imlgs')) {
            throw new GeosamplesBadRequestException('invalid IMLGS ID')
        }
        // imlgs uniquely identifies sample
        return sampleRepository.getSampleById(id)
    }


    @Operation(summary="Find all storage methods used in the IMLGS")
//    @Hidden
    @CrossOrigin
    @GetMapping("/storage_methods")
    def getStorageMethods(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}

        return sampleRepository.getUniqueStorageMethods(searchParams)
    }

    @Operation(summary="Find all physiographic provinces used in the IMLGS")
    @CrossOrigin
    @GetMapping("/physiographic_provinces")
    def getPhysiographicProvinces(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}

        return sampleRepository.getUniquePhysiographicProvinces(searchParams)
    }

    @Operation(summary="Find all sampling devices used in the IMLGS")
    @CrossOrigin
    @GetMapping("/devices")
    def getDeviceNames(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (lake) { searchParams["lake"] = lake}
        if (cruise) { searchParams["cruise"] = cruise}
        if (platform) { searchParams["platform"] = platform}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}

        return sampleRepository.getDeviceNames(searchParams)
    }


    @Operation(summary="Find all lakes referenced in the IMLGS")
    @CrossOrigin
    @GetMapping("/lakes")
    def getLakes(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}

        return sampleRepository.getLakes(searchParams)
    }


    @Operation(summary="Find all IGSN numbers used in the IMLGS")
    @CrossOrigin
    @GetMapping("/igsn")
    def getIsgnValues(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}

        return sampleRepository.getIgsnValues(searchParams)
    }


    @Operation(summary="Find all cruises referenced in the IMLGS")
    @Hidden
    @CrossOrigin
    @GetMapping("/cruises")
    def getCruiseNames(
            @RequestParam(defaultValue="false", value="name_only") boolean nameOnly,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}

        def resultSet
        if (nameOnly == true) {
            resultSet = sampleRepository.getCruiseNames(searchParams)
        } else {
            resultSet = sampleRepository.getCruises(searchParams)
        }
        return resultSet
    }


    @Operation(summary="Find all repositories used in the IMLGS")
    @CrossOrigin
    @GetMapping("/repositories")
    def getRepositories(
            @RequestParam(defaultValue="false", value="name_only") boolean nameOnly,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facilitate transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}

        def resultSet
        if (nameOnly == true) {
            resultSet = facilityRepository.getRepositoryNames(searchParams)
        } else {
            resultSet = facilityRepository.getRepositories(searchParams)
        }
        return resultSet
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
    def getPlatforms(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (lake) { searchParams["lake"] = lake}
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}

        return  sampleRepository.getPlatformNames(searchParams)
    }


    @Operation(summary="Find interval records in the IMLGS. Warning: large response without criteria")
    @Hidden
    @CrossOrigin
    @GetMapping("/intervals")
    def getIntervals(
            @RequestParam(defaultValue="false", value="count_only") boolean countOnly,
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            @RequestParam(required=false) String imlgs,
            @RequestParam(required=false) String igsn,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}
        if (imlgs) { searchParams["imlgs"] = imlgs}
        if (igsn) { searchParams["igsn"] = igsn}

        def resultSet
        if (countOnly == true) {
            resultSet = intervalRepository.getIntervalsCount(searchParams)
        } else {
            resultSet = intervalRepository.getIntervals(searchParams)
        }
        return resultSet
    }


    @Operation(summary="Find lithology values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/lithologies")
    def getLithologies(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            @RequestParam(required=false) String igsn,
            @RequestParam(required=false) String lithology,
            @RequestParam(required=false) String texture,
            @RequestParam(required=false) String mineralogy,
            @RequestParam(required=false) String weathering,
            @RequestParam(required=false) String imlgs,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        // cruise applies to both cruise and leg columns
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}
        if (igsn) { searchParams["igsn"] = igsn}
        if (texture) { searchParams['texture'] = texture}
        if (mineralogy) { searchParams['mineralogy'] = mineralogy}
        if (weathering) { searchParams['weathering'] = weathering}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueLithologyValues(searchParams)
    }

    @Operation(summary="Find texture values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/textures")
    def getTextures(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            @RequestParam(required=false) String igsn,
            @RequestParam(required=false) String imlgs,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        // cruise applies to both cruise and leg columns
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}
        if (igsn) { searchParams["igsn"] = igsn}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueTextureValues(searchParams)
    }

    @Operation(summary="Find mineralogy values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/mineralogies")
    def getMineralogies(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            @RequestParam(required=false) String igsn,
            @RequestParam(required=false) String imlgs,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        // cruise applies to both cruise and leg columns
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}
        if (igsn) { searchParams["igsn"] = igsn}
        if (lithology) { searchParams['lithology'] = lithology}
        if (texture) { searchParams['texture'] = texture}
        if (weathering) { searchParams['weathering'] = weathering}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueMineralogyValues(searchParams)
    }

    @Operation(summary="Find weathering values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/weathering")
    def getWeatheringValues(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            @RequestParam(required=false) String igsn,
            @RequestParam(required=false) String imlgs,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        // cruise applies to both cruise and leg columns
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}
        if (igsn) { searchParams["igsn"] = igsn}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueWeatheringValues(searchParams)
    }

    @Operation(summary="Find metamorphism values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/metamorphism")
    def getMetamorphismValues(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            @RequestParam(required=false) String igsn,
            @RequestParam(required=false) String imlgs,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        // cruise applies to both cruise and leg columns
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}
        if (igsn) { searchParams["igsn"] = igsn}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueMetamorphismValues(searchParams)
    }

    @Operation(summary="Find geologic ages referenced in the IMLGS")
    @CrossOrigin
    @GetMapping("/geologic_ages")
    def getGeologicAgeValues(
            @RequestParam(required=false) String repository,
            @RequestParam(required=false) String bbox,
            @RequestParam(required=false) String platform,
            @RequestParam(required=false) String lake,
            @RequestParam(required=false) String cruise,
            @RequestParam(required=false) String device,
            @RequestParam(required=false, value="start_date") String startDate,
            @RequestParam(required=false, value="min_depth") Float minDepth,
            @RequestParam(required=false, value="max_depth") Float maxDepth,
            @RequestParam(required=false) String igsn,
            @RequestParam(required=false) String imlgs,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // put request parameters into a collection to facility transfer to repository
        // TODO validate params?
        Map<String,Object> searchParams = [:]
        if (repository) { searchParams["repository"] = repository}
        // format: minx,miny,maxx,maxy
        if (bbox) { searchParams["bbox"] = bbox}
        if (platform) { searchParams["platform"] = platform}
        if (lake) { searchParams["lake"] = lake}
        // cruise applies to both cruise and leg columns
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}
        if (igsn) { searchParams["igsn"] = igsn}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueGeologicAgeValues(searchParams)
    }

}
