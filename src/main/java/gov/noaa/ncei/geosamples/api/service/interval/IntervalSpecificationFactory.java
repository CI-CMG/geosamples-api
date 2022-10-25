package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
public class IntervalSpecificationFactory implements SpecificationFactory<CuratorsIntervalEntity> {

  @Override
  public Joiner<CuratorsIntervalEntity> getJoins(Root<CuratorsIntervalEntity> r) {
    return new Joiner<CuratorsIntervalEntity>() {

      private From<CuratorsIntervalEntity, CuratorsIntervalEntity> interval = r;
      private From<CuratorsIntervalEntity, CuratorsSampleTsqpEntity> interval2Sample = null;
      private From<CuratorsIntervalEntity, CuratorsCruiseEntity> interval2Cruise = null;
      private From<CuratorsIntervalEntity, CuratorsLegEntity> interval2Leg = null;
      private From<CuratorsIntervalEntity, PlatformMasterEntity> interval2Platform = null;
      private From<CuratorsIntervalEntity, CuratorsFacilityEntity> interval2Facility = null;

      @Override
      public From<CuratorsIntervalEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (interval2Sample == null) {
          interval2Sample = interval.join(CuratorsIntervalEntity_.SAMPLE);
        }
        return interval2Sample;
      }

      @Override
      public From<CuratorsIntervalEntity, CuratorsIntervalEntity> joinInterval() {
        return interval;
      }

      @Override
      public From<CuratorsIntervalEntity, CuratorsCruiseEntity> joinCruise() {
        if (interval2Cruise == null) {
          interval2Cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return interval2Cruise;
      }

      @Override
      public From<CuratorsIntervalEntity, CuratorsLegEntity> joinLeg() {
        if (interval2Leg == null) {
          interval2Leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return interval2Leg;
      }

      @Override
      public From<CuratorsIntervalEntity, PlatformMasterEntity> joinPlatform() {
        if (interval2Platform == null) {
          interval2Platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return interval2Platform;
      }

      @Override
      public From<CuratorsIntervalEntity, CuratorsFacilityEntity> joinFacility() {
        if (interval2Facility == null) {
          interval2Facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY).join(CuratorsCruiseFacilityEntity_.FACILITY);
        }
        return interval2Facility;
      }

      @Override
      public boolean isJoinedSample() {
        return interval2Sample != null;
      }

      @Override
      public boolean isJoinedInterval() {
        return interval != null;
      }

      @Override
      public boolean isJoinedCruise() {
        return interval2Cruise != null;
      }
    };
  }
}
