package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.repository.CuratorsRockLithRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class RockLithAttributeService extends AttributeService<CuratorsRockLithEntity> {

  @Autowired
  RockLithAttributeService(CuratorsRockLithRepository repository, RockLithSpecificationFactory specificationFactory) {
    super(CuratorsRockLithEntity_.ROCK_LITH, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<CuratorsRockLithEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> specs.add(cb.isNotNull(j.joinInterval().get(CuratorsIntervalEntity_.ROCK_LITH)));
  }

}
