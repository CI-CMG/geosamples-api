package gov.noaa.ncei.geosamples.api.service;

import gov.noaa.ncei.geosamples.api.view.CruiseLinkDetailView;
import gov.noaa.ncei.geosamples.api.view.CruiseLinkView;
import gov.noaa.ncei.geosamples.api.view.CruiseNameView;
import gov.noaa.ncei.geosamples.api.view.CruiseView;
import gov.noaa.ncei.geosamples.api.view.FacilityDetailView;
import gov.noaa.ncei.geosamples.api.view.FacilityDisplayView;
import gov.noaa.ncei.geosamples.api.view.FacilityNameView;
import gov.noaa.ncei.geosamples.api.view.IntervalView;
import gov.noaa.ncei.geosamples.api.view.LinkView;
import gov.noaa.ncei.geosamples.api.view.PlatformNameView;
import gov.noaa.ncei.geosamples.api.view.SampleDetailDisplayView;
import gov.noaa.ncei.geosamples.api.view.SampleDetailView;
import gov.noaa.ncei.geosamples.api.view.SampleDetailViewImpl;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayView;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayViewBase;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayViewImpl;
import gov.noaa.ncei.geosamples.api.view.SampleLinkedDetailView;
import gov.noaa.ncei.geosamples.api.view.SampleLinkedDetailViewImpl;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseLinksEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleLinksEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import java.util.Collections;
import java.util.stream.Collectors;

public final class ViewTransformers {

  public static CruiseNameView toCruiseNameView(CuratorsCruiseEntity entity) {
    CruiseNameView view = new CruiseNameView();
    populateCruiseNameView(entity, view);
    return view;
  }

  public static CruiseView toCruiseView(CuratorsCruiseEntity entity) {
    CruiseView view = new CruiseView();
    populateCruiseView(entity, view);
    return view;
  }

  public static CruiseLinkDetailView toCruiseLinkDetailView(CuratorsCruiseEntity entity) {
    CruiseLinkDetailView view = new CruiseLinkDetailView();
    populateCruiseLinkDetailView(entity, view);
    return view;
  }

  private static CruiseLinkView toCruiseLinkView(CuratorsCruisePlatformEntity entity) {
    CruiseLinkView view = new CruiseLinkView();
    populateCruiseLinkView(entity, view);
    return view;
  }

  private static void populateCruiseLinkView(CuratorsCruisePlatformEntity entity, CruiseLinkView view) {
    view.setId(entity.getCruise().getId());
    view.setCruise(entity.getCruise().getCruiseName());
    view.setLinks(entity.getCruiseLinks().stream().map(ViewTransformers::toLinkView).sorted().collect(Collectors.toList()));
  }

  private static void populateCruiseNameView(CuratorsCruiseEntity entity, CruiseNameView view) {
    view.setId(entity.getId());
    view.setCruise(entity.getCruiseName());
    view.setYear(entity.getYear());
    view.setLegs(entity.getLegs().stream().map(CuratorsLegEntity::getLegName).collect(Collectors.toList()));
  }


  private static void populateCruiseView(CuratorsCruiseEntity entity, CruiseView view) {
    populateCruiseNameView(entity, view);
    view.setFacilities(
        entity.getFacilityMappings().stream()
            .map(CuratorsCruiseFacilityEntity::getFacility)
            .map(fac -> new FacilityNameView(fac.getId(), fac.getFacility(), fac.getFacilityCode(), fac.getDoiLink())).sorted().collect(Collectors.toList()));
    view.setPlatforms(
        entity.getPlatformMappings().stream()
            .map(CuratorsCruisePlatformEntity::getPlatform)
            .map(platform -> new PlatformNameView(platform.getId(), platform.getPlatform())).sorted().collect(Collectors.toList())
    );
  }

  private static void populateCruiseLinkDetailView(CuratorsCruiseEntity entity, CruiseLinkDetailView view) {
    populateCruiseView(entity, view);
    view.setLinks(entity.getPlatformMappings().stream()
        .flatMap(pm -> pm.getCruiseLinks().stream())
        .map(ViewTransformers::toLinkView)
        .sorted()
        .collect(Collectors.toList()));
  }

  public static FacilityDetailView toFacilityDetailView(CuratorsFacilityEntity entity, Integer sampleCount) {
    FacilityDetailView view = new FacilityDetailView();
    populateFacilityDetailView(entity, sampleCount, view);
    return view;
  }

  public static FacilityDisplayView toFacilityDisplayView(CuratorsFacilityEntity entity, Integer sampleCount) {
    FacilityDisplayView view = new FacilityDisplayView();
    populateFacilityDisplayView(entity, sampleCount, view);
    return view;
  }

  public static FacilityNameView toFacilityNameView(CuratorsFacilityEntity entity) {
    FacilityNameView view = new FacilityNameView();
    populateFacilityNameView(entity, view);
    return view;
  }

  private static void populateFacilityDetailView(CuratorsFacilityEntity entity, Integer sampleCount, FacilityDetailView view) {
    populateFacilityDisplayView(entity, sampleCount, view);
    view.setInstCode(entity.getInstCode());
    view.setAddr1(entity.getAddr1());
    view.setAddr2(entity.getAddr2());
    view.setAddr3(entity.getAddr3());
    view.setAddr4(entity.getAddr4());
    view.setContact1(entity.getContact1());
    view.setContact2(entity.getContact2());
    view.setContact3(entity.getContact3());
    view.setEmailLink(entity.getEmailLink());
    view.setUrlLink(entity.getUrlLink());
    view.setFtpLink(entity.getFtpLink());
  }

  private static void populateFacilityDisplayView(CuratorsFacilityEntity entity, Integer sampleCount, FacilityDisplayView view) {
    populateFacilityNameView(entity, view);
    view.setSampleCount(sampleCount);
    view.setFacilityComment(entity.getFacilityComment());
  }

  private static void populateFacilityNameView(CuratorsFacilityEntity entity, FacilityNameView view) {
    view.setId(entity.getId());
    view.setFacility(entity.getFacility());
    view.setFacilityCode(entity.getFacilityCode());
    view.setOtherLink(entity.getDoiLink());
  }

  public static SampleDetailDisplayView toSampleDetailView(CuratorsSampleTsqpEntity entity) {
    SampleDetailDisplayView view = new SampleDetailViewImpl();
    populateSampleDetailDisplayView(entity, view);
    return view;
  }

  public static SampleDisplayView toSampleDisplayView(CuratorsSampleTsqpEntity entity) {
    SampleDisplayView view = new SampleDisplayViewImpl();
    populateSampleDisplayView(entity, view);
    return view;
  }

  public static SampleLinkedDetailView toSampleLinkedDetailView(CuratorsSampleTsqpEntity entity) {
    SampleLinkedDetailView view = new SampleLinkedDetailViewImpl();
    populateSampleLinkedDetailView(entity, view);
    return view;
  }

  public static LinkView toLinkView(CuratorsSampleLinksEntity entity) {
    LinkView view = new LinkView();
    view.setLink(entity.getDatalink());
    view.setLinkLevel(entity.getLinkLevel());
    view.setType(entity.getLinkType());
    view.setSource(entity.getLinkSource());
    return view;
  }

  public static LinkView toLinkView(CuratorsCruiseLinksEntity entity) {
    LinkView view = new LinkView();
    view.setLink(entity.getDatalink());
    view.setLinkLevel(entity.getLinkLevel());
    view.setType(entity.getLinkType());
    view.setSource(entity.getLinkSource());
    return view;
  }

  public static IntervalView toIntervalView(CuratorsIntervalEntity entity, SampleDisplayViewBase sample) {
    IntervalView view = new IntervalView();
    view.setId(entity.getId());
    view.setCruise(entity.getSample().getCruise().getCruiseName());
    view.setFacilityCode(entity.getSample().getCruiseFacility().getFacility().getFacilityCode());
    view.setPlatform(sample.getPlatform());
    view.setSample(sample.getSample());
    view.setDevice(sample.getDevice());
    view.setInterval(entity.getInterval());
    view.setDepthTop(entity.getDepthTop());
    view.setDepthBot(entity.getDepthBot());
    view.setLith1(entity.getLith1() == null ? null : entity.getLith1().getLithology());
    view.setLith2(entity.getLith2() == null ? null : entity.getLith2().getLithology());
    view.setText1(entity.getText1() == null ? null : entity.getText1().getTexture());
    view.setText2(entity.getText2() == null ? null : entity.getText2().getTexture());
    view.setComp1(entity.getComp1() == null ? null : entity.getComp1().getLithology());
    view.setComp2(entity.getComp2() == null ? null : entity.getComp2().getLithology());
    view.setComp3(entity.getComp3() == null ? null : entity.getComp3().getLithology());
    view.setComp4(entity.getComp4() == null ? null : entity.getComp4().getLithology());
    view.setComp5(entity.getComp5() == null ? null : entity.getComp5().getLithology());
    view.setComp6(entity.getComp6() == null ? null : entity.getComp6().getLithology());
    view.setDescription(entity.getDescription());
    view.setAges(entity.getAges() == null ? Collections.emptyList() : entity.getAges().stream().map(CuratorsAgeEntity::getAge).sorted().collect(Collectors.toList()));
    view.setAbsoluteAgeTop(entity.getAbsoluteAgeTop());
    view.setAbsoluteAgeBot(entity.getAbsoluteAgeBot());
    view.setWeight(entity.getWeight());
    view.setRockLith(entity.getRockLith() == null ? null : entity.getRockLith().getRockLith());
    view.setRockMin(entity.getRockMin() == null ? null : entity.getRockMin().getRockMin());
    view.setWeathMeta(entity.getWeathMeta() == null ? null : entity.getWeathMeta().getWeathMeta());
    view.setRemark(entity.getRemark() == null ? null : entity.getRemark().getRemark());
    view.setMunsellCode(entity.getMunsellCode());
    view.setExhaustCode(entity.getExhaustCode());
    view.setPhotoLink(entity.getPhotoLink());
    view.setLake(entity.getLake());
    view.setIntComments(entity.getIntComments());
    view.setIgsn(entity.getIgsn());
    view.setImlgs(sample.getImlgs());
    return view;
  }

  // TODO child interval population should be moved to a dedicated query rather than filtering results which will be slightly more efficient
  private static void populateSampleLinkedDetailView(CuratorsSampleTsqpEntity entity, SampleLinkedDetailView view) {
    populateSampleDisplayViewBase(entity, view);
    populateSampleDetailView(entity, view);
    view.setFacility(entity.getCruiseFacility() == null ? null : toFacilityNameView(entity.getCruiseFacility().getFacility()));
    view.setLinks(entity.getLinks().stream().map(ViewTransformers::toLinkView).sorted().collect(Collectors.toList()));
    view.setIntervals(entity.getIntervals().stream().filter(CuratorsIntervalEntity::isPublish).map(i -> toIntervalView(i, view)).sorted().collect(Collectors.toList()));
    view.setCruise(entity.getCruisePlatform() == null ? null : toCruiseLinkView(entity.getCruisePlatform()));
  }

  private static FacilityNameView toFacilityNameView(CuratorsSampleTsqpEntity entity) {
    if (entity.getCruiseFacility() != null && entity.getCruiseFacility().getFacility() != null) {
      return new FacilityNameView(
          entity.getCruiseFacility().getFacility().getId(),
          entity.getCruiseFacility().getFacility().getFacility(),
          entity.getCruiseFacility().getFacility().getFacilityCode(),
          entity.getCruiseFacility().getFacility().getDoiLink()
      );
    }
    return null;
  }

  private static void populateSampleDisplayView(CuratorsSampleTsqpEntity entity, SampleDisplayView view) {
    populateSampleDisplayViewBase(entity, view);
    view.setFacility(toFacilityNameView(entity));
    view.setCruise(entity.getCruise().getCruiseName());
  }

  private static void populateSampleDisplayViewBase(CuratorsSampleTsqpEntity entity, SampleDisplayViewBase view) {
    view.setImlgs(entity.getImlgs());
    view.setPlatform(entity.getCruisePlatform().getPlatform().getPlatform());
    view.setSample(entity.getSample());
    view.setDevice(entity.getDevice().getDevice());
    view.setBeginDate(entity.getBeginDate());
    view.setLat(entity.getLat());
    view.setLon(entity.getLon());
    view.setWaterDepth(entity.getWaterDepth());
    view.setStorageMeth(entity.getStorageMeth() == null ? null : entity.getStorageMeth().getStorageMeth());
    view.setCoredLength(entity.getCoredLength());
    view.setIgsn(entity.getIgsn());
    view.setLeg(entity.getLeg() == null ? null : entity.getLeg().getLegName());
  }

  private static void populateSampleDetailDisplayView(CuratorsSampleTsqpEntity entity, SampleDetailDisplayView view) {
    populateSampleDisplayView(entity, view);
    populateSampleDetailView(entity, view);
  }

  private static void populateSampleDetailView(CuratorsSampleTsqpEntity entity, SampleDetailView view) {
    view.setShipCode(entity.getCruisePlatform().getPlatform().getIcesCode());
    view.setEndDate(entity.getEndDate());
    view.setEndLat(entity.getEndLat());
    view.setEndLon(entity.getEndLon());
    view.setLatlonOrig(entity.getLatLonOrig());
    view.setEndWaterDepth(entity.getEndWaterDepth());
    view.setCoredLengthMm(entity.getCoredLengthMm());
    view.setCoredDiam(entity.getCoredDiam());
    view.setCoredDiamMm(entity.getCoredDiamMm());
    view.setPi(entity.getPi());
    view.setProvince(entity.getProvince() == null ? null : entity.getProvince().getProvince());
    view.setLake(entity.getLake());
    view.setOtherLink(entity.getOtherLink());
    view.setLastUpdate(entity.getLastUpdate().toString());
    view.setSampleComments(entity.getSampleComments());
    view.setShowSampl(entity.getShowSampl());
    view.setPublish(entity.isPublish() ? "Y" : "N");
  }

  private ViewTransformers() {

  }

}
