package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.repository.CuratorsAgeRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class GeologicAgesAttributeService extends AttributeService<CuratorsAgeEntity> {

  @Autowired
  GeologicAgesAttributeService(CuratorsAgeRepository repository, GeologicAgesSpecificationFactory specificationFactory) {
    super(CuratorsAgeEntity_.AGE, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<CuratorsAgeEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> specs.add(cb.isNotNull(j.joinInterval().join(CuratorsIntervalEntity_.AGES).get(CuratorsAgeEntity_.AGE)));
  }

}
