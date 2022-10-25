package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsStorageMethodRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class StorageMethodAttributeService extends AttributeService<CuratorsStorageMethEntity> {

  @Autowired
  StorageMethodAttributeService(CuratorsStorageMethodRepository repository, StorageMethodSpecificationFactory specificationFactory) {
    super(CuratorsStorageMethEntity_.STORAGE_METH, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<CuratorsStorageMethEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> specs.add(cb.isNotNull(j.joinSample().get(CuratorsSampleTsqpEntity_.STORAGE_METH)));
  }

}
