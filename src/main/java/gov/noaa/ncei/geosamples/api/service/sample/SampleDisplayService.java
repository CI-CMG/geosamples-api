package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.service.sample.SampleDisplayService.SampleDisplayDto;
import gov.noaa.ncei.geosamples.api.view.FacilityNameView;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayView;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayViewImpl;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class SampleDisplayService extends SampleBaseService<SampleDisplayView, SampleDisplayDto> {

  @Autowired
  SampleDisplayService(CuratorsSampleTsqpRepository curatorsSampleTsqpRepository, SampleSpecificationFactory specificationFactory) {
    super(SampleDisplayDto.class, curatorsSampleTsqpRepository, specificationFactory);
  }

  @Override
  protected SampleDisplayView toView(CuratorsSampleTsqpEntity entity) {
    return ViewTransformers.toSampleDisplayView(entity);
  }

  @Override
  protected SampleDisplayView toView(SampleDisplayDto dto) {
    return dto.toView();
  }

  @Override
  protected Selection<?>[] dtoConstructorSelect(Root<CuratorsSampleTsqpEntity> r, CriteriaBuilder cb, Joiner<CuratorsSampleTsqpEntity> j) {
    return SampleDisplayDto.select(r, cb, j);
  }

  public static class SampleDisplayDto {
    private final String imlgs;
    private final FacilityNameView facility;
    private final String platform;
    private final String cruise;
    private final String sample;
    private final String device;
    private final String beginDate;
    private final Double lat;
    private final Double lon;
    private final Integer waterDepth;
    private final String storageMeth;
    private final Integer coredLength;
    private final String igsn;
    private final String leg;

    public SampleDisplayDto(String imlgs, Long facilityId, String facility, String facilityCode, String otherLink, String platform, String cruise, String sample, String device, String beginDate,
        Double lat,
        Double lon, Integer waterDepth, String storageMeth, Integer coredLength, String igsn, String leg) {
      this.imlgs = imlgs;
      this.facility = new FacilityNameView(facilityId, facility, facilityCode, otherLink);
      this.platform = platform;
      this.cruise = cruise;
      this.sample = sample;
      this.device = device;
      this.beginDate = beginDate;
      this.lat = lat;
      this.lon = lon;
      this.waterDepth = waterDepth;
      this.storageMeth = storageMeth;
      this.coredLength = coredLength;
      this.igsn = igsn;
      this.leg = leg;
    }

    static Selection<?>[] select(Root<CuratorsSampleTsqpEntity> r, CriteriaBuilder cb, Joiner<CuratorsSampleTsqpEntity> j) {
      return new Selection<?>[]{
          r.get(CuratorsSampleTsqpEntity_.IMLGS),
          j.joinFacility().get(CuratorsFacilityEntity_.ID),
          j.joinFacility().get(CuratorsFacilityEntity_.FACILITY),
          j.joinFacility().get(CuratorsFacilityEntity_.FACILITY_CODE),
          j.joinFacility().get(CuratorsFacilityEntity_.DOI_LINK),
          j.joinPlatform().get(PlatformMasterEntity_.PLATFORM),
          j.joinCruise().get(CuratorsCruiseEntity_.CRUISE_NAME),
          r.get(CuratorsSampleTsqpEntity_.SAMPLE),
          j.joinDevice().get(CuratorsDeviceEntity_.DEVICE),
          r.get(CuratorsSampleTsqpEntity_.BEGIN_DATE),
          r.get(CuratorsSampleTsqpEntity_.LAT),
          r.get(CuratorsSampleTsqpEntity_.LON),
          r.get(CuratorsSampleTsqpEntity_.WATER_DEPTH),
          j.joinStorageMethod().get(CuratorsStorageMethEntity_.STORAGE_METH),
          r.get(CuratorsSampleTsqpEntity_.CORED_LENGTH),
          r.get(CuratorsSampleTsqpEntity_.IGSN),
          j.joinLeg().get(CuratorsLegEntity_.LEG_NAME)
      };
    }

    public SampleDisplayView toView() {
      SampleDisplayViewImpl view = new SampleDisplayViewImpl();
      view.setImlgs(imlgs);
      view.setFacility(facility);
      view.setPlatform(platform);
      view.setCruise(cruise);
      view.setSample(sample);
      view.setDevice(device);
      view.setBeginDate(beginDate);
      view.setLat(lat);
      view.setLon(lon);
      view.setWaterDepth(waterDepth);
      view.setStorageMeth(storageMeth);
      view.setCoredLength(coredLength);
      view.setIgsn(igsn);
      view.setLeg(leg);
      return view;
    }
  }

}
