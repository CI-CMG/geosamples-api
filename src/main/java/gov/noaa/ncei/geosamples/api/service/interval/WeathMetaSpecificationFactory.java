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
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class WeathMetaSpecificationFactory implements SpecificationFactory<CuratorsWeathMetaEntity> {

  @Override
  public Joiner<CuratorsWeathMetaEntity> getJoins(Root<CuratorsWeathMetaEntity> r) {
    return new Joiner<CuratorsWeathMetaEntity>() {

      private From<CuratorsWeathMetaEntity, CuratorsWeathMetaEntity> weath = r;
      private From<CuratorsWeathMetaEntity, CuratorsIntervalEntity> weath2Interval = null;
      private From<CuratorsWeathMetaEntity, CuratorsSampleTsqpEntity> weath2Sample = null;
      private From<CuratorsWeathMetaEntity, CuratorsCruiseEntity> weath2Cruise = null;
      private From<CuratorsWeathMetaEntity, CuratorsLegEntity> weath2Leg = null;
      private From<CuratorsWeathMetaEntity, PlatformMasterEntity> weath2Platform = null;
      private From<CuratorsWeathMetaEntity, CuratorsFacilityEntity> weath2Facility = null;

      @Override
      public From<CuratorsWeathMetaEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (weath2Sample == null) {
          weath2Sample = joinInterval().join(CuratorsIntervalEntity_.SAMPLE);
        }
        return weath2Sample;
      }

      @Override
      public From<CuratorsWeathMetaEntity, CuratorsIntervalEntity> joinInterval() {
        if (weath2Interval == null) {
          weath2Interval = weath.join(CuratorsWeathMetaEntity_.INTERVALS);
        }
        return weath2Interval;
      }

      @Override
      public From<CuratorsWeathMetaEntity, CuratorsCruiseEntity> joinCruise() {
        if (weath2Cruise == null) {
          weath2Cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return weath2Cruise;
      }

      @Override
      public From<CuratorsWeathMetaEntity, CuratorsLegEntity> joinLeg() {
        if (weath2Leg == null) {
          weath2Leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return weath2Leg;
      }

      @Override
      public From<CuratorsWeathMetaEntity, PlatformMasterEntity> joinPlatform() {
        if(weath2Platform == null) {
          weath2Platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return weath2Platform;
      }

      @Override
      public From<CuratorsWeathMetaEntity, CuratorsFacilityEntity> joinFacility() {
        if(weath2Facility == null) {
          weath2Facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY).join(CuratorsCruiseFacilityEntity_.FACILITY);
        }
        return weath2Facility;
      }

      @Override
      public boolean isJoinedSample() {
        return weath2Sample != null;
      }

      @Override
      public boolean isJoinedInterval() {
        return weath2Interval != null;
      }

      @Override
      public boolean isJoinedCruise() {
        return weath2Cruise != null;
      }
    };
  }
}
