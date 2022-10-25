package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CuratorsCruiseRepository extends JpaRepository<CuratorsCruiseEntity, Long>,
    JpaSpecificationExecutor<CuratorsCruiseEntity>, CustomRepository<CuratorsCruiseEntity> {


}
