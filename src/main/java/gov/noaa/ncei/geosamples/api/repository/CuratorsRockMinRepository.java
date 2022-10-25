package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsRockMinRepository extends JpaRepository<CuratorsRockMinEntity, String>,
    JpaSpecificationExecutor<CuratorsRockMinEntity>, CustomRepository<CuratorsRockMinEntity> {


}
