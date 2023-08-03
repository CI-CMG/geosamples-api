package gov.noaa.ncei.geosamples.api.view;

import gov.noaa.ncei.geosamples.api.service.csv.CsvColumn;
import java.util.Objects;

public class SampleDetailViewImpl implements SampleDetailDisplayView {
  
  @CsvColumn(order = 28, column = "imlgs")
  private String imlgs;
  @CsvColumn(order = 0, column = "facility_code")
  private String facilityCode;
  @CsvColumn(order = 1, column = "platform")
  private String platform;
  @CsvColumn(order = 2, column = "cruise")
  private String cruise;
  @CsvColumn(order = 3, column = "sample")
  private String sample;
  @CsvColumn(order = 4, column = "device")
  private String device;
  @CsvColumn(order = 5, column = "begin_date")
  private String beginDate;
  @CsvColumn(order = 7, column = "lat")
  private Double lat;
  @CsvColumn(order = 9, column = "lon")
  private Double lon;
  @CsvColumn(order = 12, column = "water_depth")
  private Integer waterDepth;
  @CsvColumn(order = 14, column = "storage_meth")
  private String storageMeth;
  @CsvColumn(order = 15, column = "cored_length")
  private Integer coredLength;
  @CsvColumn(order = 24, column = "igsn")
  private String igsn;
  @CsvColumn(order = 25, column = "leg")
  private String leg;
  private String shipCode;
  @CsvColumn(order = 6, column = "end_date")
  private String endDate;
  @CsvColumn(order = 8, column = "end_lat")
  private Double endLat;
  @CsvColumn(order = 10, column = "end_lon")
  private Double endLon;
  @CsvColumn(order = 11, column = "latlon_orig")
  private String latlonOrig;
  @CsvColumn(order = 13, column = "end_water_depth")
  private Integer endWaterDepth;
  @CsvColumn(order = 16, column = "cored_length_mm")
  private Integer coredLengthMm;
  @CsvColumn(order = 17, column = "cored_diam")
  private Integer coredDiam;
  @CsvColumn(order = 18, column = "cored_diam_mm")
  private Integer coredDiamMm;
  @CsvColumn(order = 19, column = "pi")
  private String pi;
  @CsvColumn(order = 20, column = "province")
  private String province;
  @CsvColumn(order = 21, column = "lake")
  private String lake;
  @CsvColumn(order = 22, column = "other_link")
  private String otherLink;
  @CsvColumn(order = 23, column = "last_update")
  private String lastUpdate;
  @CsvColumn(order = 26, column = "sample_comments")
  private String sampleComments;
  @CsvColumn(order = 27, column = "show_sampl")
  private String showSampl;
  @CsvColumn(order = 29, column = "publish")
  private String publish;

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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SampleDetailViewImpl that = (SampleDetailViewImpl) o;
    return Objects.equals(imlgs, that.imlgs) && Objects.equals(facilityCode, that.facilityCode) && Objects.equals(
        platform, that.platform) && Objects.equals(cruise, that.cruise) && Objects.equals(sample, that.sample)
        && Objects.equals(device, that.device) && Objects.equals(beginDate, that.beginDate) && Objects.equals(lat,
        that.lat) && Objects.equals(lon, that.lon) && Objects.equals(waterDepth, that.waterDepth) && Objects.equals(
        storageMeth, that.storageMeth) && Objects.equals(coredLength, that.coredLength) && Objects.equals(igsn, that.igsn)
        && Objects.equals(leg, that.leg) && Objects.equals(shipCode, that.shipCode) && Objects.equals(endDate,
        that.endDate) && Objects.equals(endLat, that.endLat) && Objects.equals(endLon, that.endLon) && Objects.equals(
        latlonOrig, that.latlonOrig) && Objects.equals(endWaterDepth, that.endWaterDepth) && Objects.equals(coredLengthMm,
        that.coredLengthMm) && Objects.equals(coredDiam, that.coredDiam) && Objects.equals(coredDiamMm, that.coredDiamMm)
        && Objects.equals(pi, that.pi) && Objects.equals(province, that.province) && Objects.equals(lake, that.lake)
        && Objects.equals(otherLink, that.otherLink) && Objects.equals(lastUpdate, that.lastUpdate) && Objects.equals(
        sampleComments, that.sampleComments) && Objects.equals(showSampl, that.showSampl) && Objects.equals(publish, that.publish);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imlgs, facilityCode, platform, cruise, sample, device, beginDate, lat, lon, waterDepth, storageMeth, coredLength, igsn, leg,
        shipCode, endDate, endLat, endLon, latlonOrig, endWaterDepth, coredLengthMm, coredDiam, coredDiamMm, pi, province, lake, otherLink,
        lastUpdate,
        sampleComments, showSampl, publish);
  }

  @Override
  public String toString() {
    return "SampleDetailViewImpl{" +
        "imlgs='" + imlgs + '\'' +
        ", facilityCode='" + facilityCode + '\'' +
        ", platform='" + platform + '\'' +
        ", cruise='" + cruise + '\'' +
        ", sample='" + sample + '\'' +
        ", device='" + device + '\'' +
        ", beginDate='" + beginDate + '\'' +
        ", lat=" + lat +
        ", lon=" + lon +
        ", waterDepth=" + waterDepth +
        ", storageMeth='" + storageMeth + '\'' +
        ", coredLength=" + coredLength +
        ", igsn='" + igsn + '\'' +
        ", leg='" + leg + '\'' +
        ", shipCode='" + shipCode + '\'' +
        ", endDate='" + endDate + '\'' +
        ", endLat=" + endLat +
        ", endLon=" + endLon +
        ", latlonOrig='" + latlonOrig + '\'' +
        ", endWaterDepth=" + endWaterDepth +
        ", coredLengthMm=" + coredLengthMm +
        ", coredDiam=" + coredDiam +
        ", coredDiamMm=" + coredDiamMm +
        ", pi='" + pi + '\'' +
        ", province='" + province + '\'' +
        ", lake='" + lake + '\'' +
        ", otherLink='" + otherLink + '\'' +
        ", lastUpdate='" + lastUpdate + '\'' +
        ", sampleComments='" + sampleComments + '\'' +
        ", showSampl='" + showSampl + '\'' +
        ", publish='" + publish + '\'' +
        '}';
  }
}
