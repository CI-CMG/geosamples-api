package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsRockLithRepository extends JpaRepository<CuratorsRockLithEntity, String>,
    JpaSpecificationExecutor<CuratorsRockLithEntity>, CustomRepository<CuratorsRockLithEntity> {


}
