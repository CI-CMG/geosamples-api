package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class LakeAttributeService extends AttributeService<CuratorsSampleTsqpEntity> {

  @Autowired
  LakeAttributeService(CuratorsSampleTsqpRepository repository, SampleSpecificationFactory specificationFactory) {
    super(CuratorsSampleTsqpEntity_.LAKE, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<CuratorsSampleTsqpEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> specs.add(cb.isNotNull(r.get(CuratorsSampleTsqpEntity_.LAKE)));
  }

}
