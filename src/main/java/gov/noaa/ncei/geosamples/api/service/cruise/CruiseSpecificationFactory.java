package gov.noaa.ncei.geosamples.api.service.cruise;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class CruiseSpecificationFactory implements SpecificationFactory<CuratorsCruiseEntity> {

  @Override
  public Joiner<CuratorsCruiseEntity> getJoins(Root<CuratorsCruiseEntity> r) {
    return new Joiner<CuratorsCruiseEntity>() {

      private From<CuratorsCruiseEntity, CuratorsCruiseEntity> cruise = r;
      private From<CuratorsCruiseEntity, CuratorsFacilityEntity> cruise2Facility = null;
      private From<CuratorsCruiseEntity, PlatformMasterEntity> cruise2Platform = null;
      private From<CuratorsCruiseEntity, CuratorsSampleTsqpEntity> cruise2Sample = null;
      private From<CuratorsCruiseEntity, CuratorsIntervalEntity> cruise2Interval = null;
      private From<CuratorsCruiseEntity, CuratorsLegEntity> cruise2Leg = null;


      @Override
      public From<CuratorsCruiseEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (cruise2Sample == null) {
          cruise2Sample = cruise.join(CuratorsCruiseEntity_.SAMPLES);
        }
        return cruise2Sample;
      }

      @Override
      public From<CuratorsCruiseEntity, CuratorsIntervalEntity> joinInterval() {
        if (cruise2Interval == null) {
          cruise2Interval = joinSample().join(CuratorsSampleTsqpEntity_.INTERVALS);
        }
        return cruise2Interval;
      }

      @Override
      public From<CuratorsCruiseEntity, CuratorsCruiseEntity> joinCruise() {
        return cruise;
      }

      @Override
      public From<CuratorsCruiseEntity, CuratorsLegEntity> joinLeg() {
        if (cruise2Leg == null) {
          cruise2Leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return cruise2Leg;
      }

      @Override
      public From<CuratorsCruiseEntity, PlatformMasterEntity> joinPlatform() {
        if (cruise2Platform == null) {
          cruise2Platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return cruise2Platform;
      }

      @Override
      public From<CuratorsCruiseEntity, CuratorsFacilityEntity> joinFacility() {
        if (cruise2Facility == null) {
          cruise2Facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY).join(CuratorsCruiseFacilityEntity_.FACILITY);
        }
        return cruise2Facility;
      }

      @Override
      public boolean isJoinedSample() {
        return cruise2Sample != null;
      }

      @Override
      public boolean isJoinedInterval() {
        return cruise2Interval != null;
      }

      @Override
      public boolean isJoinedCruise() {
        return cruise != null;
      }
    };
  }
}
