package noaa.ncei.ogssd.geosamples.web


import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.servers.Servers
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.models.security.SecurityScheme
import noaa.ncei.ogssd.geosamples.GeosamplesBadRequestException
import noaa.ncei.ogssd.geosamples.GeosamplesDTO
import noaa.ncei.ogssd.geosamples.repository.FacilityRepository
import noaa.ncei.ogssd.geosamples.repository.IntervalRepository
import noaa.ncei.ogssd.geosamples.repository.SampleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Slf4j
@Tag(name="geosamples", description="Index to Marine and Lacustrine Geological Samples (IMLGS) API")
@OpenAPIDefinition(
        servers = [ @Server(url="http://localhost:8080/geosamples-api/", description="embedded server"),
                    @Server(url="https://gisdev.ngdc.noaa.gov/geosamples-api/", description="test server"),
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


    @Operation(summary="Find geosamples. Warning: large response without criteria")
    @CrossOrigin
    @GetMapping("/samples")
    def getSamples(
            @RequestParam(defaultValue="false", value="count_only") boolean countOnly,
            @RequestParam(defaultValue="false", value="full_record") boolean fullRecord,
            @Valid GeosamplesDTO searchParams,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
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
        // TODO
        log.warn("select cruise by Id not yet implemented")
        return [:]
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
            return facilityRepository.getRepositoryNames(searchParams)
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
    def getPlatforms(@Valid GeosamplesDTO searchParams) {
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
            return intervalRepository.getIntervalsCount(searchParams)
        } else {
            return intervalRepository.getIntervals(searchParams)
        }
    }


    @Operation(summary="Find lithology values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/lithologies")
    def getLithologies(@Valid GeosamplesDTO searchParams) {
        if (searchParams.lithology) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: lithology")
        }
        return intervalRepository.getUniqueLithologyValues(searchParams)
    }


    @Operation(summary="Find texture values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/textures")
    def getTextures(@Valid GeosamplesDTO searchParams) {
        if (searchParams.texture) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: texture")
        }
        return intervalRepository.getUniqueTextureValues(searchParams)
    }


    @Operation(summary="Find mineralogy values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/mineralogies")
    def getMineralogies(@Valid GeosamplesDTO searchParams) {
        if (searchParams.mineralogy) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: mineralogy")
        }
        return intervalRepository.getUniqueMineralogyValues(searchParams)
    }


    @Operation(summary="Find weathering values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/weathering")
    def getWeatheringValues(@Valid GeosamplesDTO searchParams) {
        if (searchParams.weathering) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: weathering")
        }
        return intervalRepository.getUniqueWeatheringValues(searchParams)
    }


    @Operation(summary="Find metamorphism values used in the IMLGS")
    @CrossOrigin
    @GetMapping("/metamorphism")
    def getMetamorphismValues(@Valid GeosamplesDTO searchParams) {
        if (searchParams.metamorphism) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: metamorphism")
        }
        return intervalRepository.getUniqueMetamorphismValues(searchParams)
    }


    @Operation(summary="Find geologic ages referenced in the IMLGS")
    @CrossOrigin
    @GetMapping("/geologic_ages")
    def getGeologicAgeValues(@Valid GeosamplesDTO searchParams) {
        if (searchParams.age) {
            throw new GeosamplesBadRequestException("resource does not support request parameter: age")
        }
        return intervalRepository.getUniqueGeologicAgeValues(searchParams)
    }
}
