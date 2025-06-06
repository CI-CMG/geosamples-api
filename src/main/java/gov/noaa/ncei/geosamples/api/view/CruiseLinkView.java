package gov.noaa.ncei.geosamples.api.view;

import java.util.ArrayList;
import java.util.List;

public class CruiseLinkView implements ViewWithLinks {

  private Long id;
  private String cruise;
  private List<LinkView> links = new ArrayList<>(0);

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCruise() {
    return cruise;
  }

  public void setCruise(String cruise) {
    this.cruise = cruise;
  }

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
