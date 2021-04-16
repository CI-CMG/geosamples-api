package noaa.ncei.ogssd.geosamples.web

import Slf4j
import LakeRepository
import SampleRepository
import Autowired
import GetMapping
import RequestMapping
import RequestParam
import RestController

import HttpServletRequest
import HttpServletResponse

@Slf4j
@RestController
@RequestMapping("/geosamples-api")
class LakeController {
    @Autowired
    LakeRepository lakeRepository


    @GetMapping("/lakes")
    def getSamples(
            @RequestParam(required=false, value="count_only") boolean countOnly,
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

        def resultSet
        if (countOnly == true) {
            resultSet = lakeRepository.getCount(searchParams)
        } else {
            resultSet = lakeRepository.getRecords(searchParams)
        }
        return resultSet
    }
}
