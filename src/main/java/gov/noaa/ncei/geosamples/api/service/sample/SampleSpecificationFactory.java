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
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class SampleSpecificationFactory implements SpecificationFactory<CuratorsSampleTsqpEntity> {

  @Override
  public Joiner<CuratorsSampleTsqpEntity> getJoins(Root<CuratorsSampleTsqpEntity> r) {
    return new Joiner<CuratorsSampleTsqpEntity>() {

      private From<CuratorsSampleTsqpEntity, CuratorsSampleTsqpEntity> sample = r;
      private From<CuratorsSampleTsqpEntity, CuratorsIntervalEntity> sample2interval = null;
      private From<CuratorsSampleTsqpEntity, CuratorsCruiseEntity> sample2cruise = null;
      private From<CuratorsSampleTsqpEntity, PlatformMasterEntity> sample2Platform = null;
      private From<CuratorsSampleTsqpEntity, CuratorsFacilityEntity> sample2Facility = null;
      private From<CuratorsSampleTsqpEntity, CuratorsLegEntity> sample2Leg = null;



      @Override
      public From<CuratorsSampleTsqpEntity, CuratorsSampleTsqpEntity> joinSample() {
        return sample;
      }

      @Override
      public From<CuratorsSampleTsqpEntity, CuratorsIntervalEntity> joinInterval() {
        if (sample2interval == null) {
          sample2interval = sample.join(CuratorsSampleTsqpEntity_.INTERVALS);
        }
        return sample2interval;
      }

      @Override
      public From<CuratorsSampleTsqpEntity, CuratorsCruiseEntity> joinCruise() {
        if (sample2cruise == null) {
          sample2cruise = sample.join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return sample2cruise;
      }

      @Override
      public From<CuratorsSampleTsqpEntity, CuratorsLegEntity> joinLeg() {
        if (sample2Leg == null) {
          sample2Leg = sample.join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return sample2Leg;
      }

      @Override
      public From<CuratorsSampleTsqpEntity, PlatformMasterEntity> joinPlatform() {
        if(sample2Platform == null) {
          sample2Platform = sample.join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return sample2Platform;
      }

      @Override
      public From<CuratorsSampleTsqpEntity, CuratorsFacilityEntity> joinFacility() {
        if(sample2Facility == null) {
          sample2Facility = sample.join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY).join(CuratorsCruiseFacilityEntity_.FACILITY);
        }
        return sample2Facility;
      }

      @Override
      public boolean isJoinedSample() {
        return sample != null;
      }

      @Override
      public boolean isJoinedInterval() {
        return sample2interval != null;
      }

      @Override
      public boolean isJoinedCruise() {
        return sample2cruise != null;
      }
    };
  }
}
