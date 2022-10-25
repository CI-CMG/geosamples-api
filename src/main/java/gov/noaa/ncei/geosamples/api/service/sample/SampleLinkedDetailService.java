package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.view.SampleLinkedDetailView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class SampleLinkedDetailService extends SampleBaseService<SampleLinkedDetailView> {

  @Autowired
  SampleLinkedDetailService(CuratorsSampleTsqpRepository curatorsSampleTsqpRepository, SampleSpecificationFactory specificationFactory) {
    super(curatorsSampleTsqpRepository, specificationFactory);
  }

  @Override
  protected SampleLinkedDetailView toView(CuratorsSampleTsqpEntity entity) {
    return ViewTransformers.toSampleLinkedDetailView(entity);
  }

}
