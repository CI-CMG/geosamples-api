package gov.noaa.ncei.geosamples.api.view;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SampleDetailViewImpl.class)
public interface SampleDetailView extends SampleDisplayView {

  String getShipCode();

  void setShipCode(String shipCode);

  String getEndDate();

  void setEndDate(String endDate);

  Double getEndLat();

  void setEndLat(Double endLat);

  Double getEndLon();

  void setEndLon(Double endLon);

  String getLatlonOrig();

  void setLatlonOrig(String latlonOrig);

  Integer getEndWaterDepth();

  void setEndWaterDepth(Integer endWaterDepth);

  Integer getCoredLengthMm();

  void setCoredLengthMm(Integer coredLengthMm);

  Integer getCoredDiam();

  void setCoredDiam(Integer coredDiam);

  Integer getCoredDiamMm();

  void setCoredDiamMm(Integer coredDiamMm);

  String getPi();

  void setPi(String pi);

  String getProvince();

  void setProvince(String province);

  String getLake();

  void setLake(String lake);

  String getOtherLink();

  void setOtherLink(String otherLink);

  String getLastUpdate();

  void setLastUpdate(String lastUpdate);

  String getSampleComments();

  void setSampleComments(String sampleComments);

  String getShowSampl();

  void setShowSampl(String showSampl);

  String getPublish();

  void setPublish(String publish);
}
