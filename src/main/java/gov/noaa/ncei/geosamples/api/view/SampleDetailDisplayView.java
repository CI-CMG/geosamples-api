package gov.noaa.ncei.geosamples.api.view;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SampleDetailViewImpl.class)
public interface SampleDetailDisplayView extends SampleDisplayView, SampleDetailView {}
