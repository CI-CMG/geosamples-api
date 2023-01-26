package gov.noaa.ncei.geosamples.api.view;

import java.util.List;

public interface ViewWithLinks {

  List<LinkView> getLinks();

  void setLinks(List<LinkView> links);
}
