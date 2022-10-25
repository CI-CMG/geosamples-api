package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlatformMasterRepository extends JpaRepository<PlatformMasterEntity, Long>,
    JpaSpecificationExecutor<PlatformMasterEntity>, CustomRepository<PlatformMasterEntity> {

  Optional<PlatformMasterEntity> findByPlatform(String platform);
}
