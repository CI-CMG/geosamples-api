package gov.noaa.ncei.geosamples.api.controller;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.service.cruise.CruiseService;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.CruiseLinkView;
import gov.noaa.ncei.geosamples.api.view.CruiseNameView;
import gov.noaa.ncei.geosamples.api.view.CruiseView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import javax.validation.Valid;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/cruises")
public class CruiseController {

  private final CruiseService cruiseService;

  @Autowired
  public CruiseController(CruiseService cruiseService) {
    this.cruiseService = cruiseService;
  }

  @CrossOrigin
  @GetMapping(path = "/name", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<CruiseNameView> getDisplay(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return cruiseService.names(searchParams);
  }

  @CrossOrigin
  @GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public CountView getCount(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return cruiseService.count(searchParams);
  }

  @CrossOrigin
  @GetMapping(path = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<CruiseView> getDetail(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return cruiseService.search(searchParams);
  }


  @CrossOrigin
  @GetMapping(path = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public CruiseLinkView getSamplesDetail(@PathVariable("id") Long id) {
    return cruiseService.load(id);
  }


}
