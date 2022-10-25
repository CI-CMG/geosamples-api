package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
class ProvinceSpecificationFactory implements SpecificationFactory<CuratorsProvinceEntity> {


  @Override
  public Joiner<CuratorsProvinceEntity> getJoins(Root<CuratorsProvinceEntity> r) {
    return new Joiner<CuratorsProvinceEntity>() {

      private From<CuratorsProvinceEntity, CuratorsIntervalEntity> interval = null;
      private From<CuratorsProvinceEntity, CuratorsSampleTsqpEntity> sample = null;
      private From<CuratorsProvinceEntity, CuratorsCruiseEntity> cruise = null;
      private From<CuratorsProvinceEntity, CuratorsLegEntity> leg = null;
      private From<CuratorsProvinceEntity, PlatformMasterEntity> platform = null;
      private From<CuratorsProvinceEntity, CuratorsFacilityEntity> facility = null;

      @Override
      public From<CuratorsProvinceEntity, CuratorsSampleTsqpEntity> joinSample() {
        if (sample == null) {
          sample = r.join(CuratorsProvinceEntity_.SAMPLES);
        }
        return sample;
      }

      @Override
      public From<CuratorsProvinceEntity, CuratorsIntervalEntity> joinInterval() {
        if (interval == null) {
          interval = joinSample().join(CuratorsSampleTsqpEntity_.INTERVALS);
        }
        return interval;
      }

      @Override
      public From<CuratorsProvinceEntity, CuratorsCruiseEntity> joinCruise() {
        if (cruise == null) {
          cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE);
        }
        return cruise;
      }

      @Override
      public From<CuratorsProvinceEntity, CuratorsLegEntity> joinLeg() {
        if (leg == null) {
          leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
        }
        return leg;
      }

      @Override
      public From<CuratorsProvinceEntity, PlatformMasterEntity> joinPlatform() {
        if (platform == null) {
          platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM).join(CuratorsCruisePlatformEntity_.PLATFORM);
        }
        return platform;
      }

      @Override
      public From<CuratorsProvinceEntity, CuratorsFacilityEntity> joinFacility() {
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
