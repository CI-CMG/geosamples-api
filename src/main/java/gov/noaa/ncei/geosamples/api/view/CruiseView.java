package gov.noaa.ncei.geosamples.api.view;

import java.util.ArrayList;
import java.util.List;

public class CruiseView extends CruiseNameView {

  private List<String> facilityCodes = new ArrayList<>(0);
  private List<String> platforms = new ArrayList<>(0);


  public List<String> getFacilityCodes() {
    return facilityCodes;
  }

  public void setFacilityCodes(List<String> facilityCodes) {
    if (facilityCodes == null) {
      facilityCodes = new ArrayList<>(0);
    }
    this.facilityCodes = facilityCodes;
  }

  public List<String> getPlatforms() {
    return platforms;
  }

  public void setPlatforms(List<String> platforms) {
    if (platforms == null) {
      platforms = new ArrayList<>(0);
    }
    this.platforms = platforms;
  }

}
