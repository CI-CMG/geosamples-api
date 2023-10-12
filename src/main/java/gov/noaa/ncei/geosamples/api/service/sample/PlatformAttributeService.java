package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.repository.PlatformMasterRepository;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class PlatformAttributeService extends AttributeService<PlatformMasterEntity> {

  @Autowired
  PlatformAttributeService(PlatformMasterRepository repository, PlatformSpecificationFactory specificationFactory) {
    super(PlatformMasterEntity_.PLATFORM, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<PlatformMasterEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> {
      specs.add(cb.isNotNull(j.joinPlatform().get(PlatformMasterEntity_.PLATFORM)));
      specs.add(cb.equal(j.joinSample().get(CuratorsSampleTsqpEntity_.PUBLISH), "Y"));
    };
  }

}
