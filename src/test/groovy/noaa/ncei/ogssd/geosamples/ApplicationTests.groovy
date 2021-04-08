package noaa.ncei.ogssd.geosamples

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest
class ApplicationTests {
	@Autowired
	JdbcTemplate jdbcTemplate

	@Test
	void contextLoads() {
		assert jdbcTemplate
	}

}
