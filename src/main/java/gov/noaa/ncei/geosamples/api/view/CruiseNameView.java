package gov.noaa.ncei.geosamples.api.view;

import java.util.ArrayList;
import java.util.List;

public class CruiseNameView {

  private Long id;
  private String cruise;
  private Short year;
  private List<String> legs = new ArrayList<>(0);

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Short getYear() {
    return year;
  }

  public void setYear(Short year) {
    this.year = year;
  }

  public String getCruise() {
    return cruise;
  }

  public void setCruise(String cruise) {
    this.cruise = cruise;
  }

  public List<String> getLegs() {
    return legs;
  }

  public void setLegs(List<String> legs) {
    if (legs == null) {
      legs = new ArrayList<>(0);
    }
    this.legs = legs;
  }
}
