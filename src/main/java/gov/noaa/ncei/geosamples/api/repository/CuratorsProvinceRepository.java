package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsProvinceRepository extends JpaRepository<CuratorsProvinceEntity, String>,
    JpaSpecificationExecutor<CuratorsProvinceEntity>, CustomRepository<CuratorsProvinceEntity> {


}
