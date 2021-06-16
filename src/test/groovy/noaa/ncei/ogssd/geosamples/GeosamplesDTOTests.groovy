package noaa.ncei.ogssd.geosamples

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GeosamplesDTOTests {

    @Test
    void bboxParameter() {
        GeosamplesDTO dto = new GeosamplesDTO()
        String inputString = '-132.6601,37.4925,-124.1554,43.6249'
        dto.setBbox(inputString)
        assert dto
        assert inputString == dto.bbox
        assert dto.xmin == -132.6601
    }

}