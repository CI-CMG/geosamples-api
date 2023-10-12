package gov.noaa.ncei.geosamples.api.view;

import java.util.Comparator;
import java.util.Objects;

public class FacilityNameView implements CsvColumnObject, Comparable<FacilityNameView> {

  private Long id;
  private String facility;
  private String facilityCode;
  private String otherLink;

  public FacilityNameView() {

  }

  public FacilityNameView(Long id, String facility, String facilityCode, String otherLink) {
    this.id = id;
    this.facility = facility;
    this.facilityCode = facilityCode;
    this.otherLink = otherLink;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFacility() {
    return facility;
  }

  public void setFacility(String facility) {
    this.facility = facility;
  }

  public String getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
  }

  public String getOtherLink() {
    return otherLink;
  }

  public void setOtherLink(String otherLink) {
    this.otherLink = otherLink;
  }

  @Override
  public Object asCsvColumn() {
    return facilityCode;
  }

  @Override
  public int compareTo(FacilityNameView o) {
    return Comparator.nullsLast(Comparator.comparing(FacilityNameView::getFacilityCode, String::compareToIgnoreCase)).compare(this, o);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FacilityNameView that = (FacilityNameView) o;
    return Objects.equals(id, that.id) && Objects.equals(facility, that.facility) && Objects.equals(facilityCode,
        that.facilityCode) && Objects.equals(otherLink, that.otherLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, facility, facilityCode, otherLink);
  }
}
