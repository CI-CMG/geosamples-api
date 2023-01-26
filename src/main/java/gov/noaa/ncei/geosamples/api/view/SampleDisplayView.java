package gov.noaa.ncei.geosamples.api.view;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SampleDisplayViewImpl.class)
public interface SampleDisplayView extends SampleDisplayViewBase {

  String getFacilityCode();

  void setFacilityCode(String facilityCode);

  String getCruise();

  void setCruise(String cruise);
}
