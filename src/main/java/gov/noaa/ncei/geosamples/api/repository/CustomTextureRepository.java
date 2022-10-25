package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import org.springframework.data.domain.Page;

public interface CustomTextureRepository {

  Page<String> getTextures(
      GeosampleSearchParameterObject searchParameters,
      int page, int pageSize,
      SpecificationFactory<CuratorsTextureEntity> specFactory1,
      SpecificationFactory<CuratorsTextureEntity> specFactory2);

}
