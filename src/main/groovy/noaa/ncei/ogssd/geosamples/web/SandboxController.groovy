package noaa.ncei.ogssd.geosamples.web

import groovy.util.logging.Slf4j
import noaa.ncei.ogssd.geosamples.DemoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
@RestController
class SandboxController {
    @Autowired
    DemoRepository demoRepository

    @CrossOrigin
    @GetMapping("/sandbox")
    def sandbox(HttpServletRequest request, HttpServletResponse response) {
        println('inside sandbox...')
        demoRepository.getRecords()
        demoRepository.databaseSpecificMethod()

    }

}
