package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.view.SampleDetailDisplayView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
class SampleDetailService extends SampleBaseService<SampleDetailDisplayView> {

  @Autowired
  SampleDetailService(CuratorsSampleTsqpRepository curatorsSampleTsqpRepository, SampleSpecificationFactory specificationFactory) {
    super(curatorsSampleTsqpRepository, specificationFactory);
  }

  @Override
  protected SampleDetailDisplayView toView(CuratorsSampleTsqpEntity entity) {
    return ViewTransformers.toSampleDetailView(entity);
  }



}
