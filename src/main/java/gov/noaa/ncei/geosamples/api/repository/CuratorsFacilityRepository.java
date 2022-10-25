package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsFacilityRepository extends JpaRepository<CuratorsFacilityEntity, Long>,
    JpaSpecificationExecutor<CuratorsFacilityEntity>, CustomRepository<CuratorsFacilityEntity> {

  Optional<CuratorsFacilityEntity> findByFacilityCode(String facilityCode);
}
