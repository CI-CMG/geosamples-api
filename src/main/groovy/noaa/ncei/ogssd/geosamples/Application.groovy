package noaa.ncei.ogssd.geosamples

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@SpringBootApplication
class Application {

	static void main(String[] args) {
		SpringApplication.run(Application, args)
	}

//	@Bean
//	MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(Jackson2ObjectMapperBuilder builder) {
//		builder.propertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
//		return new MappingJackson2HttpMessageConverter(builder.build());
//	}
}
