package noaa.ncei.ogssd.geosamples.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping("/geosamples-api")
    public String home() {
        return "index";
    }
}
