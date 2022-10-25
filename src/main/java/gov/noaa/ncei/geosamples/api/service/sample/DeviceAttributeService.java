package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsDeviceRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class DeviceAttributeService extends AttributeService<CuratorsDeviceEntity> {

  @Autowired
  DeviceAttributeService(CuratorsDeviceRepository repository, DeviceSpecificationFactory specificationFactory) {
    super(CuratorsDeviceEntity_.DEVICE, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<CuratorsDeviceEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> {
      specs.add(cb.isNotNull(j.joinSample().get(CuratorsSampleTsqpEntity_.DEVICE)));
    };
  }

}
