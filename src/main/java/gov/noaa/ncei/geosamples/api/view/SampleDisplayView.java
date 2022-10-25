package gov.noaa.ncei.geosamples.api.view;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SampleDisplayViewImpl.class)
public interface SampleDisplayView {

  String getImlgs();

  void setImlgs(String imlgs);

  String getFacilityCode();

  void setFacilityCode(String facilityCode);

  String getPlatform();

  void setPlatform(String platform);

  String getCruise();

  void setCruise(String cruise);

  String getSample();

  void setSample(String sample);

  String getDevice();

  void setDevice(String device);

  String getBeginDate();

  void setBeginDate(String beginDate);

  Double getLat();

  void setLat(Double lat);

  Double getLon();

  void setLon(Double lon);

  Integer getWaterDepth();

  void setWaterDepth(Integer waterDepth);

  String getStorageMeth();

  void setStorageMeth(String storageMeth);

  Integer getCoredLength();

  void setCoredLength(Integer coredLength);

  String getIgsn();

  void setIgsn(String igsn);

  String getLeg();

  void setLeg(String leg);
}
