package gov.noaa.ncei.geosamples.api.service.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.noaa.ncei.geosamples.api.TestUtils;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactoryTestUtils;
import gov.noaa.ncei.geosamples.api.view.CruiseNameView;
import gov.noaa.ncei.geosamples.api.view.IntervalView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.geosamples.api.view.SampleDetailDisplayView;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayView;
import gov.noaa.ncei.geosamples.api.view.SampleLinkedDetailView;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
class SampleServiceIT {

  private static final TypeReference<PagedItemsView<SampleDisplayView>> DISPLAY_PAGE = new TypeReference<PagedItemsView<SampleDisplayView>>() {
  };
  private static final TypeReference<PagedItemsView<SampleDetailDisplayView>> DETAIL_PAGE = new TypeReference<PagedItemsView<SampleDetailDisplayView>>() {
  };

  private static final TypeReference<PagedItemsView<CruiseNameView>> CRUISE_NAME = new TypeReference<PagedItemsView<CruiseNameView>>() {
  };

  private static final TypeReference<PagedItemsView<IntervalView>> INTERVAL = new TypeReference<PagedItemsView<IntervalView>>() {
  };


  @Autowired
  private SpecificationFactoryTestUtils specTestUtils;


  @Autowired
  private TestUtils testUtils;

  @Autowired
  private TestRestTemplate restClient;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void beforeEach() {
    testUtils.cleanDb();
    specTestUtils.loadData();
  }

  @AfterEach
  public void afterEach() {
    testUtils.cleanDb();
  }

  @Test
  public void testLoad() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("cruise", "CRUISE_2").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    assertEquals(2, response.getItems().size());

    String imlgs = response.getItems().stream().filter(s -> s.getSample().equals("CRUISE_2_S2")).findFirst().map(SampleDisplayView::getImlgs)
        .orElse(null);

    httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").pathSegment(imlgs).build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    SampleLinkedDetailView detailView = objectMapper.readValue(httpResponse.getBody(), SampleLinkedDetailView.class);
    assertEquals("CRUISE_2", detailView.getCruise().getCruise());
    assertEquals("CRUISE_2_S2", detailView.getSample());

    System.out.println(detailView.getIntervals());

    assertEquals(1, detailView.getIntervals().size());
    assertEquals(1, detailView.getIntervals().get(0).getInterval());
  }

  @Test
  public void testFindAll() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        "/api/samples/detail",
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1",
        "CRUISE_1_S2",
        "CRUISE_1_S3",
        "CRUISE_2_S1",
        "CRUISE_2_S2",
        "CRUISE_3_S1",
        "CRUISE_3_S1"
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));

  }

  @Test
  public void findByRepository() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("repository", "OER").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1",
        "CRUISE_1_S2",
        "CRUISE_1_S3"
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByBbox() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("bbox", "0.5,19.0,6.0,26.0").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_3_S1"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByPlatform() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("platform", "Explorer").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1",
        "CRUISE_1_S2",
        "CRUISE_1_S3",
        "CRUISE_2_S1"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByLake() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("lake", "placid").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_2_S2"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByDevice() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("device", "probe").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_3_S1"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByStartDate() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("start_date", "20220101").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S2"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByMinDepth() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("min_depth", "50").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_3_S1"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByMaxDepth() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("max_depth", "65").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1",
        "CRUISE_1_S2",
        "CRUISE_1_S3",
        "CRUISE_2_S1",
        "CRUISE_2_S2",
        "CRUISE_3_S1"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByIgsn() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("igsn", "cruise2Sample1").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_2_S1"
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByIgsnNotPublic() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("igsn", "cruise2Sample3").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    assertEquals(0, response.getItems().size());
  }

  @Test
  public void findByIgsnCruiseNotPublic() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("igsn", "cruise5Sample1").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);


    assertEquals(0, response.getItems().size());
  }

  @Test
  public void findByLithology() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("lithology", "zeolites").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1",
        "CRUISE_1_S3"
// CRUISE_1_S2 interval is not public
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByTexture() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("texture", "crusts").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S2",
        "CRUISE_2_S1"
// CRUISE_1_S2 interval is not public
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByMineralogy() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("mineralogy", "muscovite").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_2_S1",
        "CRUISE_2_S2"
// CRUISE_1_S2 interval is not public
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByWeathering() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("weathering", "moderate").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1"
// CRUISE_1_S2 interval is not public
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByMetamorphism() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("metamorphism", "hydrothermal").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_2_S1"
// CRUISE_1_S2 interval is not public
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByStorageMethod() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("storage_method", "room temperature, dry").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_3_S1"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByProvince() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("province", "lagoon").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S2"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByAge() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("age", "jurassic").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1"
//        "CRUISE_2_S3" //not published
//        "CRUISE_5_S1" //parent cruise not published
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByImlgs() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("cruise", "CRUISE_2").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    assertEquals(2, response.getItems().size());

    String imlgs = response.getItems().stream().filter(s -> s.getSample().equals("CRUISE_2_S2")).findFirst().map(SampleDisplayView::getImlgs)
        .orElse(null);


    httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("imlgs", imlgs).build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_2_S2"
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByCruiseId() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/cruises/name").queryParam("cruise", "CRUISE_2").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<CruiseNameView> response1 = objectMapper.readValue(httpResponse.getBody(), CRUISE_NAME);

    assertEquals(1, response1.getItems().size());

    Long cruiseId = response1.getItems().stream().findFirst().map(CruiseNameView::getId).orElse(null);


    httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("cruise_id", cruiseId).build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response2 = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_2_S1",
        "CRUISE_2_S2"
    );

    assertEquals(expectedSamples, response2.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByCruiseYear() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("cruise_year", "2020").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1",
        "CRUISE_1_S2",
        "CRUISE_1_S3",
        "CRUISE_2_S1",
        "CRUISE_2_S2"
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByPlatformId() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("platform_id", "22").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1",
        "CRUISE_1_S2",
        "CRUISE_1_S3",
        "CRUISE_2_S1"
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByFacilityId() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("facility_id", "4").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1",
        "CRUISE_1_S2",
        "CRUISE_1_S3"
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByIntervalId() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/intervals/detail").queryParam("igsn", "cruise2Sample1").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<IntervalView> response1 = objectMapper.readValue(httpResponse.getBody(), INTERVAL);

    assertEquals(1, response1.getItems().size());

    Long intervalId = response1.getItems().stream().findFirst().map(IntervalView::getId).orElse(null);


    httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("interval_id", intervalId).build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response2 = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_2_S1"
    );

    assertEquals(expectedSamples, response2.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void findByFacilityLeg() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail").queryParam("leg", "CRUISE_1_L2").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S2",
        "CRUISE_1_S3"
    );

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

  @Test
  public void testPagination() throws Exception {

    ResponseEntity<String> httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail")
            .queryParam("page", "1").queryParam("items_per_page", "4").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    PagedItemsView<SampleDetailDisplayView> response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    List<String> expectedSamples = Arrays.asList(
        "CRUISE_1_S1",
        "CRUISE_1_S2",
        "CRUISE_1_S3",
        "CRUISE_2_S1"
//        "CRUISE_2_S2",
//        "CRUISE_3_S1",
//        "CRUISE_3_S1"
    );

    assertEquals(4, response.getItemsPerPage());
    assertEquals(7L, response.getTotalItems());
    assertEquals(1, response.getPage());
    assertEquals(2, response.getTotalPages());

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));

    httpResponse = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail")
            .queryParam("page", "2").queryParam("items_per_page", "4").build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, httpResponse.getStatusCode().value());

    response = objectMapper.readValue(httpResponse.getBody(), DETAIL_PAGE);

    expectedSamples = Arrays.asList(
        "CRUISE_2_S2",
        "CRUISE_3_S1",
        "CRUISE_3_S1"
    );

    assertEquals(4, response.getItemsPerPage());
    assertEquals(7L, response.getTotalItems());
    assertEquals(2, response.getPage());
    assertEquals(2, response.getTotalPages());

    assertEquals(expectedSamples, response.getItems().stream().map(SampleDetailDisplayView::getSample).collect(Collectors.toList()));
  }

}