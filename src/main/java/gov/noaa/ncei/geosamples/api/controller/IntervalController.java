package gov.noaa.ncei.geosamples.api.controller;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.service.interval.IntervalService;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.IntervalView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
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
@RequestMapping("/api/intervals")
public class IntervalController {

  private static final String TEXT_CSV_VALUE = "text/csv";

  private final IntervalService intervalService;

  @Autowired
  public IntervalController(IntervalService intervalService) {
    this.intervalService = intervalService;
  }


  
  @GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public CountView getCount(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.count(searchParams);
  }

  
  @GetMapping(path = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedItemsView<IntervalView> getDetail(@ParameterObject @Valid GeosampleSearchParameterObject searchParams) {
    return intervalService.search(searchParams);
  }

  
  @GetMapping(path = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public IntervalView getDetail(@PathVariable("id") Long id) {
    return intervalService.load(id);
  }


  @GetMapping(path = "/csv", produces = TEXT_CSV_VALUE)
  public void getSamplesCsv(@ParameterObject @Valid GeosampleSearchParameterObject searchParams, HttpServletResponse response) {
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=geosamples_export.csv");
    response.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8).toString());
    try (OutputStream outputStream = response.getOutputStream()) {
      intervalService.exportCsv(outputStream, searchParams);
      outputStream.flush();
    } catch (IOException e) {
      throw new IllegalStateException("An error occurred processing response", e);
    }
  }

}
