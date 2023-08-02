package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class RockLithSpecificationFactory implements SpecificationFactory<CuratorsRockLithEntity> {

  @Override
  public Joiner<CuratorsRockLithEntity> getJoins(Root<CuratorsRockLithEntity> r) {
    return new Joiner<CuratorsRockLithEntity>() {

      private From<CuratorsRockLithEntity, CuratorsIntervalEntity> interval = null;
      private From<CuratorsRockLithEntity, CuratorsSampleTsqpEntity> sample = null;
      private From<CuratorsRockLithEntity, CuratorsCruiseEntity> cruise = null;
      private From<CuratorsRockLithEntity, CuratorsLegEntity> leg = null;
      private From<CuratorsRockLithEntity, PlatformMasterEntity> platform = null;
      private From<CuratorsRockLithEntity, CuratorsFacilityEntity> facility = null;
      private From<CuratorsRockLithEntity, CuratorsDeviceEntity> device = null;
      private From<CuratorsRockLithEntity, CuratorsStorageMethEntity> storageMethod = null;
      private From<CuratorsRockLithEntity, CuratorsProvinceEntity> province = null;

      @Override
      public From<CuratorsRockLithEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (sample == null) {
          sample = joinInterval().join(CuratorsIntervalEntity_.SAMPLE);
        }
        return sample;
      }

      @Override
      public From<CuratorsRockLithEntity, CuratorsIntervalEntity> joinInterval() {
        if (interval == null) {
          interval = r.join(CuratorsRockLithEntity_.INTERVALS);
        }
        return interval;
      }

      @Override
      public From<CuratorsRockLithEntity, CuratorsCruiseEntity> joinCruise() {
        if (cruise == null) {
          cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return cruise;
      }

      @Override
      public From<CuratorsRockLithEntity, CuratorsDeviceEntity> joinDevice() {
        if (device == null) {
          device = joinSample().join(CuratorsSampleTsqpEntity_.DEVICE, JoinType.LEFT);
        }
        return device;
      }

      @Override
      public From<CuratorsRockLithEntity, CuratorsLegEntity> joinLeg() {
        if (leg == null) {
          leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return leg;
      }

      @Override
      public From<CuratorsRockLithEntity, PlatformMasterEntity> joinPlatform() {
        if (platform == null) {
          platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return platform;
      }

      @Override
      public From<CuratorsRockLithEntity, CuratorsFacilityEntity> joinFacility() {
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

      @Override
      public From<CuratorsRockLithEntity, CuratorsStorageMethEntity> joinStorageMethod() {
        if (storageMethod == null) {
          storageMethod = joinSample().join(CuratorsSampleTsqpEntity_.STORAGE_METH, JoinType.LEFT);
        }
        return storageMethod;
      }

      @Override
      public From<CuratorsRockLithEntity, CuratorsProvinceEntity> joinProvince() {
        if (province == null) {
          province = joinSample().join(CuratorsSampleTsqpEntity_.PROVINCE, JoinType.LEFT);
        }
        return province;
      }
    };
  }
}
