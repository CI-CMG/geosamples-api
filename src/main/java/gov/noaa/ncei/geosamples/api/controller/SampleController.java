package gov.noaa.ncei.geosamples.api.controller;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.service.sample.SampleService;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.geosamples.api.view.SampleDetailDisplayView;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayView;
import gov.noaa.ncei.geosamples.api.view.SampleLinkedDetailView;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/samples")
@Tag(name = "Samples", description = "API endpoints to query for sample data.")
public class SampleController {

  private static final String TEXT_CSV_VALUE = "text/csv";

  private final SampleService sampleService;

  @Autowired
  public SampleController(SampleService sampleService) {
    this.sampleService = sampleService;
  }


  
  @GetMapping(path = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<SampleDisplayView> getSamplesDisplay(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.searchDisplay(searchParams);
  }

  
  @GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public CountView getSamplesCount(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.count(searchParams);
  }

  
  @GetMapping(path = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<SampleDetailDisplayView> getSamplesDetail(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return sampleService.searchDetail(searchParams);
  }


  
  @GetMapping(path = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public SampleLinkedDetailView getSamplesDetail(@PathVariable("id") String id) {
    return sampleService.load(id);
  }

  
  @GetMapping(path = "/csv", produces = TEXT_CSV_VALUE)
  public void getSamplesCsv(@ParameterObject @Valid GeosampleSearchParameterObject searchParams, HttpServletResponse response) {
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=geosamples_export.csv");
    response.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8).toString());
    try (OutputStream outputStream = response.getOutputStream()) {
      sampleService.exportCsv(outputStream, searchParams);
      outputStream.flush();
    } catch (IOException e) {
      throw new IllegalStateException("An error occurred processing response", e);
    }
  }

}
