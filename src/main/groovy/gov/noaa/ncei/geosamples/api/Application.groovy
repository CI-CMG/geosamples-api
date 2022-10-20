package gov.noaa.ncei.geosamples.api

import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

	@Autowired
	BuildInfoService buildInfoService;

	static void main(String[] args) {
		SpringApplication.run(Application, args)
	}

	@Bean
	public GroupedOpenApi usersGroup() {
		return GroupedOpenApi.builder().group("imlgs")
				.addOpenApiCustomiser(
						openApi -> openApi.info(new Info().title("IMLGS API").description('Read-only access to the Index to Marine and Lacustrine Geological Samples database')
						.version(buildInfoService.getVersion())))
				.packagesToScan("gov.noaa.ncei.geosamples.api")
				.build();
	}
}
