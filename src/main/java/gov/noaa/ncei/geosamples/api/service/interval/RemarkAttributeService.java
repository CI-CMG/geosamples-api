package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.repository.CuratorsRemarkRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class RemarkAttributeService extends AttributeService<CuratorsRemarkEntity> {

  @Autowired
  RemarkAttributeService(CuratorsRemarkRepository repository, RemarkSpecificationFactory specificationFactory) {
    super(CuratorsRemarkEntity_.REMARK, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<CuratorsRemarkEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> {
      specs.add(cb.isNotNull(j.joinInterval().get(CuratorsIntervalEntity_.REMARK)));
    };
  }
}
