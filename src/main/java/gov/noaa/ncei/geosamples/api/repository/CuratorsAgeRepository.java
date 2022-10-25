package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsAgeRepository extends JpaRepository<CuratorsAgeEntity, String>,
    JpaSpecificationExecutor<CuratorsAgeEntity>, CustomRepository<CuratorsAgeEntity> {


}
