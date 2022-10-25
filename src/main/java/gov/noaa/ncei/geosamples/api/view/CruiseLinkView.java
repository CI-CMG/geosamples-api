package gov.noaa.ncei.geosamples.api.view;

import java.util.ArrayList;
import java.util.List;

public class CruiseLinkView extends CruiseView {

  private List<LinkView> links = new ArrayList<>(0);


  public List<LinkView> getLinks() {
    return links;
  }

  public void setLinks(List<LinkView> links) {
    if (links == null) {
      links = new ArrayList<>(0);
    }
    this.links = links;
  }
}
