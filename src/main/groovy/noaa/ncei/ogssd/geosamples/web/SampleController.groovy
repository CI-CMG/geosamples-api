package noaa.ncei.ogssd.geosamples.web

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.GeosamplesBadRequestException
import noaa.ncei.ogssd.geosamples.GeosamplesResourceNotFoundException
import noaa.ncei.ogssd.geosamples.repository.SampleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
@RestController
@RequestMapping("/geosamples-api")
class SampleController {
    @Autowired
    SampleRepository sampleRepository

    /*
    convenient alternative, but you lose the automatic type conversion and the
    request parameter names must match the variable names

    @GetMapping("/samples")
    def getSamples(@RequestParam Map<String,String> allParams) {
        return []
    }
    */

    /*
    could specify default value instead of using required, e.g.
        @RequestParam(defaultValue = "test") String id
     */
    @CrossOrigin
    @GetMapping("/samples")
    def getSamples(
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
        // cruise applies to both cruise and leg columns
        if (cruise) { searchParams["cruise"] = cruise}
        if (device) { searchParams["device"] = device}
        if (startDate) { searchParams['startDate'] = startDate}
        if (minDepth) { searchParams["minDepth"] >= minDepth}
        if (maxDepth) { searchParams["maxDepth"] < maxDepth}
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
    @GetMapping("/samples/{id}")
    def getRepositoryById(@PathVariable String id) {
        // TODO use annotation-based validation
        // IMLGS id in format of "imlgs0000001"
        if (id.length() != 12 || ! id.startsWith('imlgs')) {
            throw new GeosamplesBadRequestException('invalid IMLGS ID')
        }
        return sampleRepository.getRecordById(id)
    }
}
