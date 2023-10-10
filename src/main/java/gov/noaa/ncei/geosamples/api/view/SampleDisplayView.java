package gov.noaa.ncei.geosamples.api.view;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SampleDisplayViewImpl.class)
public interface SampleDisplayView extends SampleDisplayViewBase {

  FacilityNameView getFacility();

  void setFacility(FacilityNameView facilityNameView);

  String getCruise();

  void setCruise(String cruise);
}
