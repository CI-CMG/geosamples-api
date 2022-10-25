package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsProvinceRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class ProvinceAttributeService extends AttributeService<CuratorsProvinceEntity> {

  @Autowired
  ProvinceAttributeService(CuratorsProvinceRepository repository, ProvinceSpecificationFactory specificationFactory) {
    super(CuratorsProvinceEntity_.PROVINCE, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<CuratorsProvinceEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> specs.add(cb.isNotNull(j.joinSample().get(CuratorsSampleTsqpEntity_.PROVINCE)));
  }

}
