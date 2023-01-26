package gov.noaa.ncei.geosamples.api.view;

import java.util.ArrayList;
import java.util.List;

public class CruiseLinkDetailView extends CruiseView implements ViewWithLinks {

  private List<LinkView> links = new ArrayList<>(0);

  @Override
  public List<LinkView> getLinks() {
    return links;
  }

  @Override
  public void setLinks(List<LinkView> links) {
    if (links == null) {
      links = new ArrayList<>(0);
    }
    this.links = links;
  }
}
