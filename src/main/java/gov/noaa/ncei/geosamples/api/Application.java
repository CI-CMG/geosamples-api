package gov.noaa.ncei.geosamples.api;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
@EntityScan(basePackageClasses = CuratorsIntervalEntity.class)
public class Application {

  public static void main(String[] args) {
    File svcHome = new ApplicationHome().getDir();
    String path = svcHome.getAbsolutePath();
    System.setProperty("svc.home", path);
    SpringApplication.run(Application.class, args);
  }

}
