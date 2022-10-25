package gov.noaa.ncei.geosamples.api.view;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(as = SampleLinkedDetailViewImpl.class)
public interface SampleLinkedDetailView extends SampleDetailView {

  String getFacility();

  void setFacility(String facility);

  List<LinkView> getLinks();

  void setLinks(List<LinkView> links);

  List<IntervalView> getIntervals();

  void setIntervals(List<IntervalView> intervals);

  List<LinkView> getCruiseLinks();

  void setCruiseLinks(List<LinkView> cruiseLinks);
}
