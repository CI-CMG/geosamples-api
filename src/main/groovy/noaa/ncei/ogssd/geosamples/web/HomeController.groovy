package noaa.ncei.ogssd.geosamples.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/geosamples-api")
class HomeController {
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
