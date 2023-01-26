package gov.noaa.ncei.geosamples.api.view;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(as = SampleLinkedDetailViewImpl.class)
public interface SampleLinkedDetailView extends SampleDisplayViewBase, SampleDetailView, ViewWithLinks {

  FacilityNameView getFacility();

  void setFacility(FacilityNameView facility);

  List<IntervalView> getIntervals();

  void setIntervals(List<IntervalView> intervals);

  CruiseLinkView getCruise();

  void setCruise(CruiseLinkView cruise);
}
