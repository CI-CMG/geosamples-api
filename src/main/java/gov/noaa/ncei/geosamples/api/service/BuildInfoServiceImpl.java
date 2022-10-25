package gov.noaa.ncei.geosamples.api.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service("buildInfoService")
public class BuildInfoServiceImpl implements BuildInfoService {

  private final String version;

  @Autowired
  public BuildInfoServiceImpl(@Value("classpath:geosamples-api/build.properties") Resource buildPropsResource) throws IOException {
    Properties properties = new Properties();
    try (InputStream in = buildPropsResource.getInputStream()) {
      properties.load(in);
    }
    version = properties.getProperty("version");
  }

  @Override
  public String getVersion() {
    return version;
  }
}
