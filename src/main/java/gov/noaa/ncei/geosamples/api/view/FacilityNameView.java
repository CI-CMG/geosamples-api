package gov.noaa.ncei.geosamples.api.view;

public class FacilityNameView implements CsvColumnObject {

  private Long id;
  private String facility;
  private String facilityCode;

  public FacilityNameView() {

  }

  public FacilityNameView(Long id, String facility, String facilityCode) {
    this.id = id;
    this.facility = facility;
    this.facilityCode = facilityCode;
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

  @Override
  public Object asCsvColumn() {
    return facilityCode;
  }
}
