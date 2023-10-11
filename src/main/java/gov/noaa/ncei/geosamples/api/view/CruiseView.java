package gov.noaa.ncei.geosamples.api.view;

import java.util.ArrayList;
import java.util.List;

public class CruiseView extends CruiseNameView {

  private List<FacilityNameView> facilities = new ArrayList<>(0);
  private List<PlatformNameView> platforms = new ArrayList<>(0);


  public List<FacilityNameView> getFacilities() {
    return facilities;
  }

  public void setFacilities(List<FacilityNameView> facilities) {
    if (facilities == null) {
      facilities = new ArrayList<>(0);
    }
    this.facilities = facilities;
  }

  public List<PlatformNameView> getPlatforms() {
    return platforms;
  }

  public void setPlatforms(List<PlatformNameView> platforms) {
    if (platforms == null) {
      platforms = new ArrayList<>(0);
    }
    this.platforms = platforms;
  }

}
