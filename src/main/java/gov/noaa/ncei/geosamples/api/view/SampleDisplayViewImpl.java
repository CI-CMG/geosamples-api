package gov.noaa.ncei.geosamples.api.view;


public class SampleDisplayViewImpl implements SampleDisplayView {


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

  @Override
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
}
