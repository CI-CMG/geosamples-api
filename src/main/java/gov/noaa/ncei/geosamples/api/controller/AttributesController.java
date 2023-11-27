package gov.noaa.ncei.geosamples.api.controller;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.service.interval.IntervalService;
import gov.noaa.ncei.geosamples.api.service.sample.SampleService;
import gov.noaa.ncei.geosamples.api.view.MinMaxView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Sample Attribute Descriptors", description = "API endpoints to list attributes from across all samples.")
public class AttributesController {

  private final SampleService sampleService;
  private final IntervalService intervalService;

  @Autowired
  public AttributesController(SampleService sampleService, IntervalService intervalService) {
    this.sampleService = sampleService;
    this.intervalService = intervalService;
  }


  
  @GetMapping(path = "/depth_range", produces = MediaType.APPLICATION_JSON_VALUE)
  public MinMaxView getDepthRange(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.getDepthRange(searchParams);
  }

  
  @GetMapping(path = "/storage_methods", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getStorageMethods(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.searchStorageMeth(searchParams);
  }

  
  @GetMapping(path = "/physiographic_provinces", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getProvinces(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.searchProvince(searchParams);
  }

  
  @GetMapping(path = "/devices", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getDevices(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.searchDevice(searchParams);
  }

  
  @GetMapping(path = "/lakes", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getLakes(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.searchLake(searchParams);
  }


  
  @GetMapping(path = "/igsn", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getIgsn(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.searchIgsn(searchParams);
  }

  
  @GetMapping(path = "/lithologic_compositions", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getLithologicCompositions(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.searchLithologies(searchParams);
  }

  
  @GetMapping(path = "/rock_lithologies", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getRockLithologies(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.searchRockLithologies(searchParams);
  }

  
  @GetMapping(path = "/textures", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getTextures(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.searchTextures(searchParams);
  }

  
  @GetMapping(path = "/mineralogies", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getMineralogies(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.searchMineralogies(searchParams);
  }

  
  @GetMapping(path = "/weathering", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getWeath(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.searchWeathering(searchParams);
  }

  
  @GetMapping(path = "/metamorphism", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getMeta(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.searchMetamorphism(searchParams);
  }

  @GetMapping(path = "/geologic_ages", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getAges(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.searchAges(searchParams);
  }

  @GetMapping(path = "/platforms", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getPlatform(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.searchPlatform(searchParams);
  }

  @GetMapping(path = "/remarks", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<String> getRemarks(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.searchRemarks(searchParams);
  }


}
