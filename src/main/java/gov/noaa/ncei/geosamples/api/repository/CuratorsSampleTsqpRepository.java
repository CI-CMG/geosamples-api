package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CuratorsSampleTsqpRepository extends JpaRepository<CuratorsSampleTsqpEntity, String>,
    JpaSpecificationExecutor<CuratorsSampleTsqpEntity>, CustomRepository<CuratorsSampleTsqpEntity>  {

  @Query("select count(S) from CuratorsSampleTsqpEntity S join S.cruiseFacility CF join CF.facility F where F.id = :facilityId")
  Integer countFacilitySamples(@Param("facilityId") Long facilityId);

}
