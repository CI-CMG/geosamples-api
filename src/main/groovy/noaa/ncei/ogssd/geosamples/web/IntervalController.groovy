package noaa.ncei.ogssd.geosamples.web

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.repository.IntervalRepository
import noaa.ncei.ogssd.geosamples.repository.SampleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
@RestController
@RequestMapping("/geosamples-api")
class IntervalController {
    @Autowired
    IntervalRepository intervalRepository

    @CrossOrigin
    @GetMapping("/intervals")
    def getIntervals(
        @RequestParam(required=false, value="count_only") boolean countOnly,
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
            resultSet = sampleRepository.getCount(searchParams)
        } else {
            resultSet = sampleRepository.getRecords(searchParams)
        }
        return resultSet
    }


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
        if (lithology) { searchParams['lithology'] = lithology}
        if (mineralogy) { searchParams['mineralogy'] = mineralogy}
        if (weathering) { searchParams['weathering'] = weathering}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueTextureValues(searchParams)
    }


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
            @RequestParam(required=false) String lithology,
            @RequestParam(required=false) String texture,
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
        if (lithology) { searchParams['lithology'] = lithology}
        if (texture) { searchParams['texture'] = texture}
        if (weathering) { searchParams['weathering'] = weathering}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueMineralogyValues(searchParams)
    }


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
            @RequestParam(required=false) String lithology,
            @RequestParam(required=false) String texture,
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
        if (lithology) { searchParams['lithology'] = lithology}
        if (texture) { searchParams['texture'] = texture}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueWeatheringValues(searchParams)
    }


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
            @RequestParam(required=false) String lithology,
            @RequestParam(required=false) String texture,
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
        if (lithology) { searchParams['lithology'] = lithology}
        if (texture) { searchParams['texture'] = texture}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueMetamorphismValues(searchParams)
    }


    @CrossOrigin
    @GetMapping("/geologicages")
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
            @RequestParam(required=false) String lithology,
            @RequestParam(required=false) String texture,
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
        if (lithology) { searchParams['lithology'] = lithology}
        if (texture) { searchParams['texture'] = texture}
        if (imlgs) { searchParams['imlgs'] = imlgs}

        return intervalRepository.getUniqueGeologicAgeValues(searchParams)
    }
}
