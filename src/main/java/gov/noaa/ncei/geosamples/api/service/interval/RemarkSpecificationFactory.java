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
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class RemarkSpecificationFactory implements SpecificationFactory<CuratorsRemarkEntity> {

  @Override
  public Joiner<CuratorsRemarkEntity> getJoins(Root<CuratorsRemarkEntity> r) {
    return new Joiner<CuratorsRemarkEntity>() {

      private From<CuratorsRemarkEntity, CuratorsRemarkEntity> remark = r;
      private From<CuratorsRemarkEntity, CuratorsIntervalEntity> remark2Interval = null;
      private From<CuratorsRemarkEntity, CuratorsSampleTsqpEntity> remark2Sample = null;
      private From<CuratorsRemarkEntity, CuratorsCruiseEntity> remark2Cruise = null;
      private From<CuratorsRemarkEntity, CuratorsLegEntity> remark2Leg = null;
      private From<CuratorsRemarkEntity, PlatformMasterEntity> remark2Platform = null;
      private From<CuratorsRemarkEntity, CuratorsFacilityEntity> remark2Facility = null;
      private From<CuratorsRemarkEntity, CuratorsDeviceEntity> remark2device = null;
      private From<CuratorsRemarkEntity, CuratorsStorageMethEntity> remark2StorageMethod = null;
      private From<CuratorsRemarkEntity, CuratorsProvinceEntity> remark2Province = null;

      @Override
      public From<CuratorsRemarkEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (remark2Sample == null) {
          remark2Sample = joinInterval().join(CuratorsIntervalEntity_.SAMPLE);
        }
        return remark2Sample;
      }

      @Override
      public From<CuratorsRemarkEntity, CuratorsIntervalEntity> joinInterval() {
        if (remark2Interval == null) {
          remark2Interval = remark.join(CuratorsRemarkEntity_.INTERVALS);
        }
        return remark2Interval;
      }

      @Override
      public From<CuratorsRemarkEntity, CuratorsCruiseEntity> joinCruise() {
        if (remark2Cruise == null) {
          remark2Cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return remark2Cruise;
      }

      @Override
      public From<CuratorsRemarkEntity, CuratorsDeviceEntity> joinDevice() {
        if (remark2device == null) {
          remark2device = joinSample().join(CuratorsSampleTsqpEntity_.DEVICE, JoinType.LEFT);
        }
        return remark2device;
      }

      @Override
      public From<CuratorsRemarkEntity, CuratorsLegEntity> joinLeg() {
        if (remark2Leg == null) {
          remark2Leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return remark2Leg;
      }

      @Override
      public From<CuratorsRemarkEntity, PlatformMasterEntity> joinPlatform() {
        if (remark2Platform == null) {
          remark2Platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return remark2Platform;
      }

      @Override
      public From<CuratorsRemarkEntity, CuratorsFacilityEntity> joinFacility() {
        if (remark2Facility == null) {
          remark2Facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY).join(CuratorsCruiseFacilityEntity_.FACILITY);
        }
        return remark2Facility;
      }

      @Override
      public boolean isJoinedSample() {
        return remark2Sample != null;
      }

      @Override
      public boolean isJoinedInterval() {
        return remark2Interval != null;
      }

      @Override
      public boolean isJoinedCruise() {
        return remark2Cruise != null;
      }

      @Override
      public From<CuratorsRemarkEntity, CuratorsStorageMethEntity> joinStorageMethod() {
        if (remark2StorageMethod == null) {
          remark2StorageMethod = joinSample().join(CuratorsSampleTsqpEntity_.STORAGE_METH, JoinType.LEFT);
        }
        return remark2StorageMethod;
      }

      @Override
      public From<CuratorsRemarkEntity, CuratorsProvinceEntity> joinProvince() {
        if (remark2Province == null) {
          remark2Province = joinSample().join(CuratorsSampleTsqpEntity_.PROVINCE, JoinType.LEFT);
        }
        return remark2Province;
      }
    };
  }
}
