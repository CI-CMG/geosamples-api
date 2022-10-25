package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsDeviceRepository extends JpaRepository<CuratorsDeviceEntity, String>,
    JpaSpecificationExecutor<CuratorsDeviceEntity>, CustomRepository<CuratorsDeviceEntity> {


}
