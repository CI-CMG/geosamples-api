package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class SampleDisplayService extends SampleBaseService<SampleDisplayView> {

  @Autowired
  SampleDisplayService(CuratorsSampleTsqpRepository curatorsSampleTsqpRepository, SampleSpecificationFactory specificationFactory) {
    super(curatorsSampleTsqpRepository, specificationFactory);
  }

  @Override
  protected SampleDisplayView toView(CuratorsSampleTsqpEntity entity) {
    return ViewTransformers.toSampleDisplayView(entity);
  }

}
