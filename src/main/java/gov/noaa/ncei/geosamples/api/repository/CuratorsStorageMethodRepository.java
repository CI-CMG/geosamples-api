package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsStorageMethodRepository extends JpaRepository<CuratorsStorageMethEntity, String>,
    JpaSpecificationExecutor<CuratorsStorageMethEntity>, CustomRepository<CuratorsStorageMethEntity> {


}
