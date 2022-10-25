package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.repository.CuratorsRockMinRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class MineralogiesAttributeService extends AttributeService<CuratorsRockMinEntity> {

  @Autowired
  MineralogiesAttributeService(CuratorsRockMinRepository repository, RockMinSpecificationFactory specificationFactory) {
    super(CuratorsRockMinEntity_.ROCK_MIN, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<CuratorsRockMinEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> specs.add(cb.isNotNull(j.joinInterval().get(CuratorsIntervalEntity_.ROCK_MIN)));
  }

}
