package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsLithologyRepository extends JpaRepository<CuratorsLithologyEntity, String>,
    JpaSpecificationExecutor<CuratorsLithologyEntity>, CustomRepository<CuratorsLithologyEntity>, CustomLithologyRepository {


}
