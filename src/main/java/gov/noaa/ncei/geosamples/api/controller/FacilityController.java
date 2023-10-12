package gov.noaa.ncei.geosamples.api.controller;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.service.facility.FacilityService;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.FacilityDetailView;
import gov.noaa.ncei.geosamples.api.view.FacilityDisplayView;
import gov.noaa.ncei.geosamples.api.view.FacilityNameView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/repositories")
@Tag(name = "Repositories", description = "API endpoints to query for repositories / facilities.")
public class FacilityController {

  private final FacilityService facilityService;

  @Autowired
  public FacilityController(FacilityService facilityService) {
    this.facilityService = facilityService;
  }

  
  @GetMapping(path = "/name", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<FacilityNameView> getName(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return facilityService.names(searchParams);
  }

  
  @GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public CountView getCount(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return facilityService.count(searchParams);
  }

  
  @GetMapping(path = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<FacilityDisplayView> getDisplay(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return facilityService.search(searchParams);
  }


  
  @GetMapping(path = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public FacilityDetailView getDetail(@PathVariable("id") Long id) {
    return facilityService.load(id);
  }


}
