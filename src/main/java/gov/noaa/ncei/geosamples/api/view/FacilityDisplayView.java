package gov.noaa.ncei.geosamples.api.view;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    FacilityDisplayView that = (FacilityDisplayView) o;
    return Objects.equals(sampleCount, that.sampleCount) && Objects.equals(facilityComment, that.facilityComment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), sampleCount, facilityComment);
  }
}
