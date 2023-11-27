package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import org.springframework.data.domain.Page;

public interface CustomLithologyRepository {

  Page<String> getLithologies(
      GeosampleSearchParameterObject searchParameters,
      int page, int pageSize,
      SpecificationFactory<CuratorsIntervalEntity> specFactory);

}
