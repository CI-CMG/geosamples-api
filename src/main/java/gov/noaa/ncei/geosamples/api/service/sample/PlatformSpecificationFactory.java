package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class PlatformSpecificationFactory implements SpecificationFactory<PlatformMasterEntity> {


  @Override
  public Joiner<PlatformMasterEntity> getJoins(Root<PlatformMasterEntity> r) {
    return new Joiner<PlatformMasterEntity>() {

      private From<PlatformMasterEntity, CuratorsIntervalEntity> interval = null;
      private From<PlatformMasterEntity, CuratorsSampleTsqpEntity> sample = null;
      private From<PlatformMasterEntity, CuratorsCruiseEntity> cruise = null;
      private From<PlatformMasterEntity, CuratorsLegEntity> leg = null;
      private From<PlatformMasterEntity, CuratorsFacilityEntity> facility = null;

      @Override
      public From<PlatformMasterEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (sample == null) {
          sample = r.join(PlatformMasterEntity_.CRUISE_PLATFORMS).join(CuratorsCruisePlatformEntity_.SAMPLES);
        }
        return sample;
      }

      @Override
      public From<PlatformMasterEntity, CuratorsIntervalEntity> joinInterval() {
        if (interval == null) {
          interval = joinSample().join(CuratorsSampleTsqpEntity_.INTERVALS);
        }
        return interval;
      }

      @Override
      public From<PlatformMasterEntity, CuratorsCruiseEntity> joinCruise() {
        if (cruise == null) {
          cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return cruise;
      }

      @Override
      public From<PlatformMasterEntity, CuratorsLegEntity> joinLeg() {
        if (leg == null) {
          leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return leg;
      }

      @Override
      public From<PlatformMasterEntity, PlatformMasterEntity> joinPlatform() {
        return r;
      }

      @Override
      public From<PlatformMasterEntity, CuratorsFacilityEntity> joinFacility() {
        if (facility == null) {
          facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY).join(CuratorsCruiseFacilityEntity_.FACILITY);
        }
        return facility;
      }

      @Override
      public boolean isJoinedSample() {
        return sample != null;
      }

      @Override
      public boolean isJoinedInterval() {
        return interval != null;
      }

      @Override
      public boolean isJoinedCruise() {
        return cruise != null;
      }
    };
  }
}
