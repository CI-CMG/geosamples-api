package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.service.sample.SampleDetailService.SampleDetailDto;
import gov.noaa.ncei.geosamples.api.view.SampleLinkedDetailView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class SampleLinkedDetailService extends SampleBaseService<SampleLinkedDetailView, SampleDetailDto> {

  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;

  @Autowired
  SampleLinkedDetailService(CuratorsSampleTsqpRepository curatorsSampleTsqpRepository, SampleSpecificationFactory specificationFactory) {
    super(SampleDetailDto.class, curatorsSampleTsqpRepository, specificationFactory);
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
  }

  @Override
  protected SampleLinkedDetailView toView(CuratorsSampleTsqpEntity entity) {
    return ViewTransformers.toSampleLinkedDetailView(entity);
  }

  @Override
  protected SampleLinkedDetailView toView(SampleDetailDto dto) {
    return toView(curatorsSampleTsqpRepository.findById(dto.toView().getImlgs()).orElseThrow(() -> new IllegalStateException("Unable to load sample")));
  }

  @Override
  protected Selection<?>[] dtoConstructorSelect(Root<CuratorsSampleTsqpEntity> r, CriteriaBuilder cb, Joiner<CuratorsSampleTsqpEntity> j) {
    return SampleDetailDto.select(r, cb, j);
  }

}
