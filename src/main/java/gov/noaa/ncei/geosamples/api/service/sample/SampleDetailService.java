package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.service.sample.SampleDetailService.SampleDetailDto;
import gov.noaa.ncei.geosamples.api.service.sample.SampleDisplayService.SampleDisplayDto;
import gov.noaa.ncei.geosamples.api.view.FacilityNameView;
import gov.noaa.ncei.geosamples.api.view.SampleDetailDisplayView;
import gov.noaa.ncei.geosamples.api.view.SampleDetailViewImpl;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import java.time.Instant;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
class SampleDetailService extends SampleBaseService<SampleDetailDisplayView, SampleDetailDto> {

  @Autowired
  SampleDetailService(CuratorsSampleTsqpRepository curatorsSampleTsqpRepository, SampleSpecificationFactory specificationFactory) {
    super(SampleDetailDto.class, curatorsSampleTsqpRepository, specificationFactory);
  }

  @Override
  protected SampleDetailDisplayView toView(CuratorsSampleTsqpEntity entity) {
    return ViewTransformers.toSampleDetailView(entity);
  }

  @Override
  protected SampleDetailDisplayView toView(SampleDetailDto dto) {
    return dto.toView();
  }

  @Override
  protected Selection<?>[] dtoConstructorSelect(Root<CuratorsSampleTsqpEntity> r, CriteriaBuilder cb, Joiner<CuratorsSampleTsqpEntity> j) {
    return SampleDetailDto.select(r, cb, j);
  }


  public static class SampleDetailDto extends SampleDisplayDto {

    private final String shipCode;
    private final String endDate;
    private final Double endLat;
    private final Double endLon;
    private final String latlonOrig;
    private final Integer endWaterDepth;
    private final Integer coredLengthMm;
    private final Integer coredDiam;
    private final Integer coredDiamMm;
    private final String pi;
    private final String province;
    private final String lake;
    private final String otherLink;
    private final Instant lastUpdate;
    private final String sampleComments;
    private final String showSampl;
    private final String publish;


    /*
    Expected arguments are:
    java.lang.String, imlgs
    java.lang.String, facilityCode
    java.lang.String, platform
    java.lang.String, cruise
    java.lang.String, sample
    java.lang.String, device
    java.lang.String, beginDate
    double, lat
    double, lon
    int, waterDepth
    java.lang.String, storageMeth
    int, coredLength igsn
    java.lang.String, leg
    java.lang.String, shipCode
    java.lang.String, endDate
    double, endLat
    double, endLon
    java.lang.String, latlonOrig
    int, endWaterDepth
    int, coredLengthMm
    int, coredDiam
    int, coredDiamMm
    java.lang.String, pi
    java.lang.String, province
    java.lang.String, lake
    java.lang.String, otherLink
    java.time.Instant, lastUpdate
    java.lang.String, sampleComments
    java.lang.String, showSampl
    java.lang.String publish

     */

//    public SampleDetailDto(
//        String imlgs,
//        String facilityCode,
//        String platform,
//        String cruise,
//        String sample,
//        String device,
//        String beginDate,
//        double lat,
//        double lon,
//        int waterDepth,
//        String storageMeth,
//        int coredLength,
//        String igsn,
//        String leg,
//
//        String shipCode,
//        String endDate,
//        double endLat,
//        double endLon,
//        String latlonOrig,
//        int endWaterDepth,
//        int coredLengthMm,
//        int coredDiam,
//        int coredDiamMm,
//        String pi,
//        String province,
//        String lake,
//        String otherLink,
//        Instant lastUpdate,
//        String sampleComments,
//        String showSampl,
//        String publish) {
//      super(imlgs, facilityCode, platform, cruise, sample, device, beginDate, lat, lon, waterDepth, storageMeth, coredLength, igsn, leg);
//      this.shipCode = shipCode;
//      this.endDate = endDate;
//      this.endLat = endLat;
//      this.endLon = endLon;
//      this.latlonOrig = latlonOrig;
//      this.endWaterDepth = endWaterDepth;
//      this.coredLengthMm = coredLengthMm;
//      this.coredDiam = coredDiam;
//      this.coredDiamMm = coredDiamMm;
//      this.pi = pi;
//      this.province = province;
//      this.lake = lake;
//      this.otherLink = otherLink;
//      this.lastUpdate = lastUpdate;
//      this.sampleComments = sampleComments;
//      this.showSampl = showSampl;
//      this.publish = publish;
//    }

    public SampleDetailDto(
        String imlgs,
        Long facilityId,
        String facility,
        String facilityCode,
        String facilityOtherLink,
        String platform,
        String cruise,
        String sample,
        String device,
        String beginDate,
        Double lat,
        Double lon,
        Integer waterDepth,
        String storageMeth,
        Integer coredLength,
        String igsn,
        String leg,
        String shipCode,
        String endDate,
        Double endLat,
        Double endLon,
        String latlonOrig,
        Integer endWaterDepth,
        Integer coredLengthMm,
        Integer coredDiam,
        Integer coredDiamMm,
        String pi,
        String province,
        String lake,
        String otherLink,
        Instant lastUpdate,
        String sampleComments,
        String showSampl,
        String publish
    ) {
      super(imlgs, facilityId, facility, facilityCode, facilityOtherLink, platform, cruise, sample, device, beginDate, lat, lon, waterDepth, storageMeth, coredLength, igsn, leg);
      this.shipCode = shipCode;
      this.endDate = endDate;
      this.endLat = endLat;
      this.endLon = endLon;
      this.latlonOrig = latlonOrig;
      this.endWaterDepth = endWaterDepth;
      this.coredLengthMm = coredLengthMm;
      this.coredDiam = coredDiam;
      this.coredDiamMm = coredDiamMm;
      this.pi = pi;
      this.province = province;
      this.lake = lake;
      this.otherLink = otherLink;
      this.lastUpdate = lastUpdate;
      this.sampleComments = sampleComments;
      this.showSampl = showSampl;
      this.publish = publish;
    }

    /*
    String shipCode,
        String endDate,
        Double endLat,
        Double endLon,
        String latlonOrig,
        Integer endWaterDepth,
        Integer coredLengthMm,
        Integer coredDiam,
        Integer coredDiamMm,
        String pi,
        String province,
        String lake,
        String otherLink,
        Instant lastUpdate,
        String sampleComments,
        String showSampl,
        String publish
     */

    static Selection<?>[] select(Root<CuratorsSampleTsqpEntity> r, CriteriaBuilder cb, Joiner<CuratorsSampleTsqpEntity> j) {
      Selection<?>[] selectParent = SampleDisplayDto.select(r, cb, j);
      Selection<?>[] select = new Selection<?>[]{
          j.joinPlatform().get(PlatformMasterEntity_.ICES_CODE),
          r.get(CuratorsSampleTsqpEntity_.END_DATE),
          r.get(CuratorsSampleTsqpEntity_.END_LAT),
          r.get(CuratorsSampleTsqpEntity_.END_LON),
          r.get(CuratorsSampleTsqpEntity_.LAT_LON_ORIG),
          r.get(CuratorsSampleTsqpEntity_.END_WATER_DEPTH),
          r.get(CuratorsSampleTsqpEntity_.CORED_LENGTH_MM),
          r.get(CuratorsSampleTsqpEntity_.CORED_DIAM),
          r.get(CuratorsSampleTsqpEntity_.CORED_DIAM_MM),
          r.get(CuratorsSampleTsqpEntity_.PI),
          j.joinProvince().get(CuratorsProvinceEntity_.PROVINCE),
          r.get(CuratorsSampleTsqpEntity_.LAKE),
          r.get(CuratorsSampleTsqpEntity_.OTHER_LINK),
          r.get(CuratorsSampleTsqpEntity_.LAST_UPDATE),
          r.get(CuratorsSampleTsqpEntity_.SAMPLE_COMMENTS),
          r.get(CuratorsSampleTsqpEntity_.SHOW_SAMPL),
          r.get(CuratorsSampleTsqpEntity_.PUBLISH)
      };
      int length = selectParent.length + select.length;
      Selection<?>[] result = new Selection<?>[length];
      for (int i = 0; i < selectParent.length; i++) {
        result[i] = selectParent[i];
      }
      for (int i = 0; i < select.length; i++) {
        result[i + selectParent.length] = select[i];
      }
      return result;
    }

    public SampleDetailDisplayView toView() {
      SampleDisplayView displayView = super.toView();
      SampleDetailViewImpl view = new SampleDetailViewImpl();
      view.setImlgs(displayView.getImlgs());
      view.setFacility(displayView.getFacility());
      view.setPlatform(displayView.getPlatform());
      view.setCruise(displayView.getCruise());
      view.setSample(displayView.getSample());
      view.setDevice(displayView.getDevice());
      view.setBeginDate(displayView.getBeginDate());
      view.setLat(displayView.getLat());
      view.setLon(displayView.getLon());
      view.setWaterDepth(displayView.getWaterDepth());
      view.setStorageMeth(displayView.getStorageMeth());
      view.setCoredLength(displayView.getCoredLength());
      view.setIgsn(displayView.getIgsn());
      view.setLeg(displayView.getLeg());
      view.setShipCode(shipCode);
      view.setEndDate(endDate);
      view.setEndLat(endLat);
      view.setEndLon(endLon);
      view.setLatlonOrig(latlonOrig);
      view.setEndWaterDepth(endWaterDepth);
      view.setCoredLengthMm(coredLengthMm);
      view.setCoredDiam(coredDiam);
      view.setCoredDiamMm(coredDiamMm);
      view.setPi(pi);
      view.setProvince(province);
      view.setLake(lake);
      view.setOtherLink(otherLink);
      view.setLastUpdate(lastUpdate == null ? null : lastUpdate.toString());
      view.setSampleComments(sampleComments);
      view.setShowSampl(showSampl);
      view.setPublish(publish);
      return view;
    }
  }
}
