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
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class StorageMethodSpecificationFactory implements SpecificationFactory<CuratorsStorageMethEntity> {

  @Override
  public Joiner<CuratorsStorageMethEntity> getJoins(Root<CuratorsStorageMethEntity> r) {
    return new Joiner<CuratorsStorageMethEntity>() {

      private From<CuratorsStorageMethEntity, CuratorsIntervalEntity> interval = null;
      private From<CuratorsStorageMethEntity, CuratorsSampleTsqpEntity> sample = null;
      private From<CuratorsStorageMethEntity, CuratorsCruiseEntity> cruise = null;
      private From<CuratorsStorageMethEntity, CuratorsLegEntity> leg = null;
      private From<CuratorsStorageMethEntity, PlatformMasterEntity> platform = null;
      private From<CuratorsStorageMethEntity, CuratorsFacilityEntity> facility = null;

      @Override
      public From<CuratorsStorageMethEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (sample == null) {
          sample = r.join(CuratorsStorageMethEntity_.SAMPLES);
        }
        return sample;
      }

      @Override
      public From<CuratorsStorageMethEntity, CuratorsIntervalEntity> joinInterval() {
        if (interval == null) {
          interval = joinSample().join(CuratorsSampleTsqpEntity_.INTERVALS);
        }
        return interval;
      }

      @Override
      public From<CuratorsStorageMethEntity, CuratorsCruiseEntity> joinCruise() {
        if (cruise == null) {
          cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return cruise;
      }

      @Override
      public From<CuratorsStorageMethEntity, CuratorsLegEntity> joinLeg() {
        if (leg == null) {
          leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return leg;
      }

      @Override
      public From<CuratorsStorageMethEntity, PlatformMasterEntity> joinPlatform() {
        if (platform == null) {
          platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return platform;
      }

      @Override
      public From<CuratorsStorageMethEntity, CuratorsFacilityEntity> joinFacility() {
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
