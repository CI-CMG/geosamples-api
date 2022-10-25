package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsIntervalRepository extends JpaRepository<CuratorsIntervalEntity, Long>,
    JpaSpecificationExecutor<CuratorsIntervalEntity>, CustomRepository<CuratorsIntervalEntity> {


}
