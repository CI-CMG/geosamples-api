package gov.noaa.ncei.geosamples.api.view;

import java.util.ArrayList;
import java.util.List;

public class SampleLinkedDetailViewImpl implements SampleLinkedDetailView {


  private String imlgs;
  private String facilityCode;
  private String platform;
  private String cruise;
  private String sample;
  private String device;
  private String beginDate;
  private Double lat;
  private Double lon;
  private Integer waterDepth;
  private String storageMeth;
  private Integer coredLength;
  private String igsn;
  private String leg;
  private String facility;
  private String shipCode;
  private String endDate;
  private Double endLat;
  private Double endLon;
  private String latlonOrig;
  private Integer endWaterDepth;
  private Integer coredLengthMm;
  private Integer coredDiam;
  private Integer coredDiamMm;
  private String pi;
  private String province;
  private String lake;
  private String otherLink;
  private String lastUpdate;
  private String sampleComments;
  private String showSampl;
  private String publish;
  private List<LinkView> links = new ArrayList<>(0);
  private List<IntervalView> intervals = new ArrayList<>(0);
  private List<LinkView> cruiseLinks = new ArrayList<>(0);

  @Override
  public String getImlgs() {
    return imlgs;
  }

  @Override
  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }

  @Override
  public String getFacilityCode() {
    return facilityCode;
  }

  @Override
  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
  }

  @Override
  public String getPlatform() {
    return platform;
  }

  @Override
  public void setPlatform(String platform) {
    this.platform = platform;
  }

  @Override
  public String getCruise() {
    return cruise;
  }

  @Override
  public void setCruise(String cruise) {
    this.cruise = cruise;
  }

  @Override
  public String getSample() {
    return sample;
  }

  @Override
  public void setSample(String sample) {
    this.sample = sample;
  }

  @Override
  public String getDevice() {
    return device;
  }

  @Override
  public void setDevice(String device) {
    this.device = device;
  }

  @Override
  public String getBeginDate() {
    return beginDate;
  }

  @Override
  public void setBeginDate(String beginDate) {
    this.beginDate = beginDate;
  }

  @Override
  public Double getLat() {
    return lat;
  }

  @Override
  public void setLat(Double lat) {
    this.lat = lat;
  }

  @Override
  public Double getLon() {
    return lon;
  }

  @Override
  public void setLon(Double lon) {
    this.lon = lon;
  }

  @Override
  public Integer getWaterDepth() {
    return waterDepth;
  }

  @Override
  public void setWaterDepth(Integer waterDepth) {
    this.waterDepth = waterDepth;
  }

  @Override
  public String getStorageMeth() {
    return storageMeth;
  }

  public void setStorageMeth(String storageMeth) {
    this.storageMeth = storageMeth;
  }

  @Override
  public Integer getCoredLength() {
    return coredLength;
  }

  @Override
  public void setCoredLength(Integer coredLength) {
    this.coredLength = coredLength;
  }

  @Override
  public String getIgsn() {
    return igsn;
  }

  @Override
  public void setIgsn(String igsn) {
    this.igsn = igsn;
  }

  @Override
  public String getLeg() {
    return leg;
  }

  @Override
  public void setLeg(String leg) {
    this.leg = leg;
  }

  @Override
  public String getFacility() {
    return facility;
  }

  @Override
  public void setFacility(String facility) {
    this.facility = facility;
  }

  @Override
  public String getShipCode() {
    return shipCode;
  }

  @Override
  public void setShipCode(String shipCode) {
    this.shipCode = shipCode;
  }

  @Override
  public String getEndDate() {
    return endDate;
  }

  @Override
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  @Override
  public Double getEndLat() {
    return endLat;
  }

  @Override
  public void setEndLat(Double endLat) {
    this.endLat = endLat;
  }

  @Override
  public Double getEndLon() {
    return endLon;
  }

  @Override
  public void setEndLon(Double endLon) {
    this.endLon = endLon;
  }

  @Override
  public String getLatlonOrig() {
    return latlonOrig;
  }

  @Override
  public void setLatlonOrig(String latlonOrig) {
    this.latlonOrig = latlonOrig;
  }

  @Override
  public Integer getEndWaterDepth() {
    return endWaterDepth;
  }

  @Override
  public void setEndWaterDepth(Integer endWaterDepth) {
    this.endWaterDepth = endWaterDepth;
  }

  @Override
  public Integer getCoredLengthMm() {
    return coredLengthMm;
  }

  @Override
  public void setCoredLengthMm(Integer coredLengthMm) {
    this.coredLengthMm = coredLengthMm;
  }

  @Override
  public Integer getCoredDiam() {
    return coredDiam;
  }

  @Override
  public void setCoredDiam(Integer coredDiam) {
    this.coredDiam = coredDiam;
  }

  @Override
  public Integer getCoredDiamMm() {
    return coredDiamMm;
  }

  @Override
  public void setCoredDiamMm(Integer coredDiamMm) {
    this.coredDiamMm = coredDiamMm;
  }

  @Override
  public String getPi() {
    return pi;
  }

  @Override
  public void setPi(String pi) {
    this.pi = pi;
  }

  @Override
  public String getProvince() {
    return province;
  }

  @Override
  public void setProvince(String province) {
    this.province = province;
  }

  @Override
  public String getLake() {
    return lake;
  }

  @Override
  public void setLake(String lake) {
    this.lake = lake;
  }

  @Override
  public String getOtherLink() {
    return otherLink;
  }

  @Override
  public void setOtherLink(String otherLink) {
    this.otherLink = otherLink;
  }

  @Override
  public String getLastUpdate() {
    return lastUpdate;
  }

  @Override
  public void setLastUpdate(String lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  @Override
  public String getSampleComments() {
    return sampleComments;
  }

  @Override
  public void setSampleComments(String sampleComments) {
    this.sampleComments = sampleComments;
  }

  @Override
  public String getShowSampl() {
    return showSampl;
  }

  @Override
  public void setShowSampl(String showSampl) {
    this.showSampl = showSampl;
  }

  @Override
  public String getPublish() {
    return publish;
  }

  @Override
  public void setPublish(String publish) {
    this.publish = publish;
  }


  @Override
  public List<LinkView> getLinks() {
    return links;
  }

  @Override
  public void setLinks(List<LinkView> links) {
    if (links == null) {
      links = new ArrayList<>(0);
    }
    this.links = links;
  }

  @Override
  public List<IntervalView> getIntervals() {
    return intervals;
  }

  @Override
  public void setIntervals(List<IntervalView> intervals) {
    if (intervals == null) {
      intervals = new ArrayList<>(0);
    }
    this.intervals = intervals;
  }

  @Override
  public List<LinkView> getCruiseLinks() {
    return cruiseLinks;
  }

  @Override
  public void setCruiseLinks(List<LinkView> cruiseLinks) {
    if (cruiseLinks == null) {
      cruiseLinks = new ArrayList<>(0);
    }
    this.cruiseLinks = cruiseLinks;
  }
}
