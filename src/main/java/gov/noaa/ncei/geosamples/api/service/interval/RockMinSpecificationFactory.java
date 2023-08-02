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
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class RockMinSpecificationFactory implements SpecificationFactory<CuratorsRockMinEntity> {

  @Override
  public Joiner<CuratorsRockMinEntity> getJoins(Root<CuratorsRockMinEntity> r) {
    return new Joiner<CuratorsRockMinEntity>() {

      private From<CuratorsRockMinEntity, CuratorsIntervalEntity> interval = null;
      private From<CuratorsRockMinEntity, CuratorsSampleTsqpEntity> sample = null;
      private From<CuratorsRockMinEntity, CuratorsCruiseEntity> cruise = null;
      private From<CuratorsRockMinEntity, CuratorsLegEntity> leg = null;
      private From<CuratorsRockMinEntity, PlatformMasterEntity> platform = null;
      private From<CuratorsRockMinEntity, CuratorsFacilityEntity> facility = null;
      private From<CuratorsRockMinEntity, CuratorsDeviceEntity> device = null;
      private From<CuratorsRockMinEntity, CuratorsStorageMethEntity> storageMethod = null;
      private From<CuratorsRockMinEntity, CuratorsProvinceEntity> province = null;

      @Override
      public From<CuratorsRockMinEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (sample == null) {
          sample = joinInterval().join(CuratorsIntervalEntity_.SAMPLE);
        }
        return sample;
      }

      @Override
      public From<CuratorsRockMinEntity, CuratorsIntervalEntity> joinInterval() {
        if (interval == null) {
          interval = r.join(CuratorsRockMinEntity_.INTERVALS);
        }
        return interval;
      }

      @Override
      public From<CuratorsRockMinEntity, CuratorsCruiseEntity> joinCruise() {
        if (cruise == null) {
          cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return cruise;
      }

      @Override
      public From<CuratorsRockMinEntity, CuratorsDeviceEntity> joinDevice() {
        if (device == null) {
          device = joinSample().join(CuratorsSampleTsqpEntity_.DEVICE, JoinType.LEFT);
        }
        return device;
      }

      @Override
      public From<CuratorsRockMinEntity, CuratorsLegEntity> joinLeg() {
        if (leg == null) {
          leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return leg;
      }

      @Override
      public From<CuratorsRockMinEntity, PlatformMasterEntity> joinPlatform() {
        if (platform == null) {
          platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return platform;
      }

      @Override
      public From<CuratorsRockMinEntity, CuratorsFacilityEntity> joinFacility() {
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
      public From<CuratorsRockMinEntity, CuratorsStorageMethEntity> joinStorageMethod() {
        if (storageMethod == null) {
          storageMethod = joinSample().join(CuratorsSampleTsqpEntity_.STORAGE_METH, JoinType.LEFT);
        }
        return storageMethod;
      }

      @Override
      public From<CuratorsRockMinEntity, CuratorsProvinceEntity> joinProvince() {
        if (province == null) {
          province = joinSample().join(CuratorsSampleTsqpEntity_.PROVINCE, JoinType.LEFT);
        }
        return province;
      }
    };
  }
}
