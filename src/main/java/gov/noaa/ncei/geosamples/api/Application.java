package gov.noaa.ncei.geosamples.api;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl;
import gov.noaa.ncei.geosamples.api.service.BuildInfoService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import io.swagger.v3.oas.models.info.Info;
import java.io.File;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
@EntityScan(basePackageClasses = CuratorsIntervalEntity.class)
public class Application {

  @Autowired
  private BuildInfoService buildInfoService;

  public static void main(String[] args) {
    File svcHome = new ApplicationHome().getDir();
    String path = svcHome.getAbsolutePath();
    System.setProperty("svc.home", path);
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public GroupedOpenApi usersGroup() {
    return GroupedOpenApi.builder().group("imlgs")
        .addOpenApiCustomiser(
            openApi -> openApi.info(
                new Info().title("IMLGS API").description("Read-only access to the Index to Marine and Lacustrine Geological Samples database")
                    .version(buildInfoService.getVersion())))
        .packagesToScan("gov.noaa.ncei.geosamples.api")
        .build();
  }
}
