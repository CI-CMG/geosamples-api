package gov.noaa.ncei.geosamples.api

import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

	static void main(String[] args) {git
		SpringApplication.run(Application, args)
	}

	@Bean
	public GroupedOpenApi usersGroup(@Value('${springdoc.version}') String appVersion) {
		return GroupedOpenApi.builder().group("imlgs")
				.addOpenApiCustomiser(openApi -> openApi.info(new Info().title("IMLGS API").description('Read-only access to the Index to Marine and Lacustrine Geological Samples database').version(appVersion)))
				.packagesToScan("gov.noaa.ncei.geosamples.api")
				.build();
	}
}
