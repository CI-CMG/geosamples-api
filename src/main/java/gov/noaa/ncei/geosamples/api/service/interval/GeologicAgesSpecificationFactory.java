package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity_;
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
class GeologicAgesSpecificationFactory implements SpecificationFactory<CuratorsAgeEntity> {


  @Override
  public Joiner<CuratorsAgeEntity> getJoins(Root<CuratorsAgeEntity> r) {
    return new Joiner<CuratorsAgeEntity>() {

      private From<CuratorsAgeEntity, CuratorsAgeEntity> age = r;
      private From<CuratorsAgeEntity, CuratorsIntervalEntity> age2Interval = null;
      private From<CuratorsAgeEntity, CuratorsSampleTsqpEntity> age2Sample = null;
      private From<CuratorsAgeEntity, CuratorsCruiseEntity> age2Cruise = null;
      private From<CuratorsAgeEntity, CuratorsLegEntity> age2Leg = null;
      private From<CuratorsAgeEntity, PlatformMasterEntity> age2Platform = null;
      private From<CuratorsAgeEntity, CuratorsFacilityEntity> age2Facility = null;

      @Override
      public From<CuratorsAgeEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (age2Sample == null) {
          age2Sample = joinInterval().join(CuratorsIntervalEntity_.SAMPLE);
        }
        return age2Sample;
      }

      @Override
      public From<CuratorsAgeEntity, CuratorsIntervalEntity> joinInterval() {
        if (age2Interval == null) {
          age2Interval = age.join(CuratorsAgeEntity_.INTERVALS);
        }
        return age2Interval;
      }

      @Override
      public From<CuratorsAgeEntity, CuratorsCruiseEntity> joinCruise() {
        if (age2Cruise == null) {
          age2Cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return age2Cruise;
      }

      @Override
      public From<CuratorsAgeEntity, CuratorsLegEntity> joinLeg() {
        if (age2Leg == null) {
          age2Leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return age2Leg;
      }

      @Override
      public From<CuratorsAgeEntity, PlatformMasterEntity> joinPlatform() {
        if(age2Platform == null) {
          age2Platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return age2Platform;
      }

      @Override
      public From<CuratorsAgeEntity, CuratorsFacilityEntity> joinFacility() {
        if(age2Facility == null) {
          age2Facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY).join(CuratorsCruiseFacilityEntity_.FACILITY);
        }
        return age2Facility;
      }

      @Override
      public boolean isJoinedSample() {
        return age2Sample != null;
      }

      @Override
      public boolean isJoinedInterval() {
        return age2Interval != null;
      }

      @Override
      public boolean isJoinedCruise() {
        return age2Cruise != null;
      }
    };
  }
}
