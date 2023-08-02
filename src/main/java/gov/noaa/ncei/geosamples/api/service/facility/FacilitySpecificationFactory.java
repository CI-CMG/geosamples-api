package gov.noaa.ncei.geosamples.api.service.facility;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class FacilitySpecificationFactory implements SpecificationFactory<CuratorsFacilityEntity> {

  @Override
  public Joiner<CuratorsFacilityEntity> getJoins(Root<CuratorsFacilityEntity> r) {
    return new Joiner<CuratorsFacilityEntity>() {

      private From<CuratorsFacilityEntity, CuratorsIntervalEntity> interval = null;
      private From<CuratorsFacilityEntity, CuratorsSampleTsqpEntity> sample = null;
      private From<CuratorsFacilityEntity, CuratorsCruiseEntity> cruise = null;
      private From<CuratorsFacilityEntity, CuratorsLegEntity> leg = null;
      private From<CuratorsFacilityEntity, PlatformMasterEntity> platform = null;
      private From<CuratorsFacilityEntity, CuratorsDeviceEntity> facility2Device = null;
      private From<CuratorsFacilityEntity, CuratorsStorageMethEntity> facility2StorageMethod = null;
      private From<CuratorsFacilityEntity, CuratorsProvinceEntity> facility2Province = null;

      @Override
      public From<CuratorsFacilityEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (sample == null) {
          sample = r.join(CuratorsFacilityEntity_.CRUISE_FACILITIES).join(CuratorsCruiseFacilityEntity_.SAMPLES);
        }
        return sample;
      }

      @Override
      public From<CuratorsFacilityEntity, CuratorsIntervalEntity> joinInterval() {
        if (interval == null) {
          interval = joinSample().join(CuratorsSampleTsqpEntity_.INTERVALS);
        }
        return interval;
      }

      @Override
      public From<CuratorsFacilityEntity, CuratorsCruiseEntity> joinCruise() {
        if (cruise == null) {
          cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return cruise;
      }

      @Override
      public From<CuratorsFacilityEntity, CuratorsDeviceEntity> joinDevice() {
        if (facility2Device == null) {
          facility2Device = joinSample().join(CuratorsSampleTsqpEntity_.DEVICE, JoinType.LEFT);
        }
        return facility2Device;
      }

      @Override
      public From<CuratorsFacilityEntity, CuratorsLegEntity> joinLeg() {
        if (leg == null) {
          leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return leg;
      }

      @Override
      public From<CuratorsFacilityEntity, PlatformMasterEntity> joinPlatform() {
        if (platform == null) {
          platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return platform;
      }

      @Override
      public From<CuratorsFacilityEntity, CuratorsFacilityEntity> joinFacility() {
        return r;
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
      public From<CuratorsFacilityEntity, CuratorsStorageMethEntity> joinStorageMethod() {
        if (facility2StorageMethod == null) {
          facility2StorageMethod = joinSample().join(CuratorsSampleTsqpEntity_.STORAGE_METH, JoinType.LEFT);
        }
        return facility2StorageMethod;
      }

      @Override
      public From<CuratorsFacilityEntity, CuratorsProvinceEntity> joinProvince() {
        if (facility2Province == null) {
          facility2Province = joinSample().join(CuratorsSampleTsqpEntity_.PROVINCE, JoinType.LEFT);
        }
        return facility2Province;
      }
    };
  }
}
