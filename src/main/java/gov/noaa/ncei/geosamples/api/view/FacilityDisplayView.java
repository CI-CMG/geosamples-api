package gov.noaa.ncei.geosamples.api.view;

public class FacilityDisplayView extends FacilityNameView {

  private Integer sampleCount;
  private String facilityComment;

  public Integer getSampleCount() {
    return sampleCount;
  }

  public void setSampleCount(Integer sampleCount) {
    this.sampleCount = sampleCount;
  }

  public String getFacilityComment() {
    return facilityComment;
  }

  public void setFacilityComment(String facilityComment) {
    this.facilityComment = facilityComment;
  }
}
