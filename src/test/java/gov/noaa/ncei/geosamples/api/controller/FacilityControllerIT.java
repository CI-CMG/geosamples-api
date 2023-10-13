package gov.noaa.ncei.geosamples.api.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gov.noaa.ncei.geosamples.api.TestUtils;
import java.util.Collections;
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
class FacilityControllerIT {

  private static final String AOML_COMMENT = "<p><a href=\"http://www.aoml.noaa.gov/\" target=\"_top\">Atlantic Oceanographic and Meteorological Laboratories (AOML)</a>\n"
      + "<br>(http://www.aoml.noaa.gov/)\n"
      + "<br>Charles Featherstone\n"
      + "<br>NOAA - Atlantic Oceanographic and Meteorological Laboratories\n"
      + "<br>4301 Rickenbacker Causeway<br>Miami, FL  33149\n"
      + "<br>(305)361-4401\n"
      + "<br>(305)361-4392 (fax)\n"
      + "<br><a href=\"mailto:charles.featherstone@noaa.gov\">charles.featherstone@noaa.gov</a>\n"
      + "</p>\n"
      + "<br>\n"
      + "<br>\n"
      + "<cite>Cite as: NOAA Atlantic Oceanographic and Meteorological Laboratories (AOML): Archive of data and information about the former AOML geosample collection. NOAA National Centers for Environmental Information. <a href=\"https://dx.doi.org/10.7289/V5VM498W\">doi:10.7289/V5VM498W</a> [access date]<cite> \n"
      + "<br>\n"
      + "<br>\n"
      + "<p>AOML no longer maintains a core repository/samples are no longer available.</p>";

  @Autowired
  private TestRestTemplate restClient;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TestUtils testUtils;


  @BeforeEach
  public void beforeEach() {
    testUtils.cleanDb();
  }

  @AfterEach
  public void afterEach() {
    testUtils.cleanDb();
  }

  @Test
  public void testGetRepositories() throws Exception {
    String cruiseName1 = "CRUISE_1";
    int year1 = 2020;
    String platform1 = "Sea Biskit";
    String facility1 = "AOML";
    String leg1 = "LEFT-1";
    String leg2 = "RIGHT-1";

    String cruiseName2 = "CRUISE_2";
    int year2 = 2021;
    String platform2 = "Susie Q";
    String facility2 = "USGSMP";
    String leg3 = "LEFT-2";
    String leg4 = null;

    testUtils.createBasicCruise(cruiseName1, year1, platform1, facility1, leg1, leg2);
    testUtils.createBasicCruise(cruiseName2, year2, platform2, facility2, leg3, leg4);

    Sample sample1 = new Sample();
    sample1.setIgsn("igsn1");
    sample1.setSample("sample1");
    sample1.setPublish("Y");
    sample1.setProvince("axial valley");
    sample1.setLat(55.5);
    sample1.setLon(66.6);
    sample1.setDevice("trap, sediment");
    sample1.setLake("blue");

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");
    sample2.setLake("red");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSampleLink(
        cruiseName1,
        year1,
        sample1.getSample(),
        "data link 1",
        "link level 1",
        "link source 1",
        "link type 1",
        "Y");

    testUtils.insertCruiseLink(
        cruiseName1,
        year1,
        platform1,
        "data link 2",
        "link level 2",
        "link source 2",
        "link type 2",
        "Y",
        leg1);

    Interval interval1 = new Interval();
    interval1.setInterval(1);
    interval1.setLith1("terrigenous");
    interval1.setLith2("evaporite");
    interval1.setRockLith("sedimentary (pyroclastic)");
    interval1.setText1("gravel");
    interval1.setText2("sandy mud or ooze");
    interval1.setRockMin("xenoliths");
    interval1.setWeathMeta("weathering - light");
    interval1.setAges(Collections.singletonList("Quaternary"));

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");
    interval2.setAges(Collections.singletonList("Jurassic"));

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");
    interval3.setAges(Collections.singletonList("Permian"));

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");
    interval4.setAges(Collections.singletonList("Cambrian"));

    testUtils.insertInterval(cruiseName1, year1, sample2.getSample(), interval3);
    testUtils.insertInterval(cruiseName1, year1, sample2.getSample(), interval4);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core");
    sample3.setLake("wet");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("probe");
    sample4.setLake("dry");

    Sample sample5 = new Sample();
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);

    Interval interval5 = new Interval();
    interval5.setInterval(1);

    Interval interval6 = new Interval();
    interval6.setInterval(2);
    interval6.setLith1("phosphate");
    interval6.setText1("ash");
    interval6.setRockMin("muscovite");
    interval6.setWeathMeta("metamorphism - serpentinized");
    interval6.setAges(Collections.singletonList("Triassic"));

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/repositories/summary")
            .queryParam("cruise", cruiseName1)
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());

    ObjectNode expectedJson = objectMapper.createObjectNode();
    ArrayNode items = objectMapper.createArrayNode();
    expectedJson.replace("items", items);
    expectedJson.put("page", 1);
    expectedJson.put("total_pages", 1);
    expectedJson.put("total_items", 1);
    expectedJson.put("items_per_page", 500);

    ObjectNode facility = objectMapper.createObjectNode();
    facility.put("id", testUtils.getFacilityId(facility1));
    facility.put("facility", "NOAA-Atlantic Oceanographic and Meteorol. Lab");
    facility.put("facility_code", "AOML");
    facility.put("other_link", "https://dx.doi.org/doi:10.7289/V5VM498W");
    facility.put("sample_count", 2);
    facility.put("facility_comment", AOML_COMMENT);
    items.add(facility);

    assertEquals(objectMapper.readTree(expectedJson.toString()), json);

  }

  @Test
  public void testCount() throws Exception {
    String cruiseName1 = "CRUISE_1";
    int year1 = 2020;
    String platform1 = "Sea Biskit";
    String facility1 = "AOML";
    String leg1 = "LEFT-1";
    String leg2 = "RIGHT-1";

    String cruiseName2 = "CRUISE_2";
    int year2 = 2021;
    String platform2 = "Susie Q";
    String facility2 = "USGSMP";
    String leg3 = "LEFT-2";
    String leg4 = null;

    testUtils.createBasicCruise(cruiseName1, year1, platform1, facility1, leg1, leg2);
    testUtils.createBasicCruise(cruiseName2, year2, platform2, facility2, leg3, leg4);

    Sample sample1 = new Sample();
    sample1.setIgsn("igsn1");
    sample1.setSample("sample1");
    sample1.setPublish("Y");
    sample1.setProvince("axial valley");
    sample1.setLat(55.5);
    sample1.setLon(66.6);
    sample1.setDevice("trap, sediment");
    sample1.setLake("blue");

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");
    sample2.setLake("red");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSampleLink(
        cruiseName1,
        year1,
        sample1.getSample(),
        "data link 1",
        "link level 1",
        "link source 1",
        "link type 1",
        "Y");

    testUtils.insertCruiseLink(
        cruiseName1,
        year1,
        platform1,
        "data link 2",
        "link level 2",
        "link source 2",
        "link type 2",
        "Y",
        leg1);

    Interval interval1 = new Interval();
    interval1.setInterval(1);
    interval1.setLith1("terrigenous");
    interval1.setLith2("evaporite");
    interval1.setRockLith("sedimentary (pyroclastic)");
    interval1.setText1("gravel");
    interval1.setText2("sandy mud or ooze");
    interval1.setRockMin("xenoliths");
    interval1.setWeathMeta("weathering - light");
    interval1.setAges(Collections.singletonList("Quaternary"));

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");
    interval2.setAges(Collections.singletonList("Jurassic"));

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");
    interval3.setAges(Collections.singletonList("Permian"));

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");
    interval4.setAges(Collections.singletonList("Cambrian"));

    testUtils.insertInterval(cruiseName1, year1, sample2.getSample(), interval3);
    testUtils.insertInterval(cruiseName1, year1, sample2.getSample(), interval4);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core");
    sample3.setLake("wet");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("probe");
    sample4.setLake("dry");

    Sample sample5 = new Sample();
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);

    Interval interval5 = new Interval();
    interval5.setInterval(1);

    Interval interval6 = new Interval();
    interval6.setInterval(2);
    interval6.setLith1("phosphate");
    interval6.setText1("ash");
    interval6.setRockMin("muscovite");
    interval6.setWeathMeta("metamorphism - serpentinized");
    interval6.setAges(Collections.singletonList("Triassic"));

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/repositories/count")
            .queryParam("cruise", cruiseName1)
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());

    ObjectNode intervals = objectMapper.createObjectNode();
    intervals.put("count", 1);

    assertEquals(intervals, json);

  }

  @Test
  public void testGetRepositoriesNameOnly() throws Exception {
    String cruiseName1 = "CRUISE_1";
    int year1 = 2020;
    String platform1 = "Sea Biskit";
    String facility1 = "AOML";
    String leg1 = "LEFT-1";
    String leg2 = "RIGHT-1";

    String cruiseName2 = "CRUISE_2";
    int year2 = 2021;
    String platform2 = "Susie Q";
    String facility2 = "USGSMP";
    String leg3 = "LEFT-2";
    String leg4 = null;

    testUtils.createBasicCruise(cruiseName1, year1, platform1, facility1, leg1, leg2);
    testUtils.createBasicCruise(cruiseName2, year2, platform2, facility2, leg3, leg4);

    Sample sample1 = new Sample();
    sample1.setIgsn("igsn1");
    sample1.setSample("sample1");
    sample1.setPublish("Y");
    sample1.setProvince("axial valley");
    sample1.setLat(55.5);
    sample1.setLon(66.6);
    sample1.setDevice("trap, sediment");
    sample1.setLake("blue");

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");
    sample2.setLake("red");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSampleLink(
        cruiseName1,
        year1,
        sample1.getSample(),
        "data link 1",
        "link level 1",
        "link source 1",
        "link type 1",
        "Y");

    testUtils.insertCruiseLink(
        cruiseName1,
        year1,
        platform1,
        "data link 2",
        "link level 2",
        "link source 2",
        "link type 2",
        "Y",
        leg1);

    Interval interval1 = new Interval();
    interval1.setInterval(1);
    interval1.setLith1("terrigenous");
    interval1.setLith2("evaporite");
    interval1.setRockLith("sedimentary (pyroclastic)");
    interval1.setText1("gravel");
    interval1.setText2("sandy mud or ooze");
    interval1.setRockMin("xenoliths");
    interval1.setWeathMeta("weathering - light");
    interval1.setAges(Collections.singletonList("Quaternary"));

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");
    interval2.setAges(Collections.singletonList("Jurassic"));

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");
    interval3.setAges(Collections.singletonList("Permian"));

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");
    interval4.setAges(Collections.singletonList("Cambrian"));

    testUtils.insertInterval(cruiseName1, year1, sample2.getSample(), interval3);
    testUtils.insertInterval(cruiseName1, year1, sample2.getSample(), interval4);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core");
    sample3.setLake("wet");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("probe");
    sample4.setLake("dry");

    Sample sample5 = new Sample();
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);

    Interval interval5 = new Interval();
    interval5.setInterval(1);

    Interval interval6 = new Interval();
    interval6.setInterval(2);
    interval6.setLith1("phosphate");
    interval6.setText1("ash");
    interval6.setRockMin("muscovite");
    interval6.setWeathMeta("metamorphism - serpentinized");
    interval6.setAges(Collections.singletonList("Triassic"));

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/repositories/name")
            .queryParam("cruise", cruiseName1)
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());

    ObjectNode expectedJson = objectMapper.createObjectNode();
    ArrayNode items = objectMapper.createArrayNode();
    expectedJson.replace("items", items);
    expectedJson.put("page", 1);
    expectedJson.put("total_pages", 1);
    expectedJson.put("total_items", 1);
    expectedJson.put("items_per_page", 500);

    ObjectNode facility = objectMapper.createObjectNode();
    facility.put("id", testUtils.getFacilityId(facility1));
    facility.put("facility", "NOAA-Atlantic Oceanographic and Meteorol. Lab");
    facility.put("facility_code", "AOML");
    facility.put("other_link", "https://dx.doi.org/doi:10.7289/V5VM498W");
    items.add(facility);

    assertEquals(objectMapper.readTree(expectedJson.toString()), json);
  }

  @Test
  public void testGetRepositoriesById() throws Exception {
    String cruiseName1 = "CRUISE_1";
    int year1 = 2020;
    String platform1 = "Sea Biskit";
    String facility1 = "AOML";
    String leg1 = "LEFT-1";
    String leg2 = "RIGHT-1";

    String cruiseName2 = "CRUISE_2";
    int year2 = 2021;
    String platform2 = "Susie Q";
    String facility2 = "USGSMP";
    String leg3 = "LEFT-2";
    String leg4 = null;

    testUtils.createBasicCruise(cruiseName1, year1, platform1, facility1, leg1, leg2);
    testUtils.createBasicCruise(cruiseName2, year2, platform2, facility2, leg3, leg4);

    Sample sample1 = new Sample();
    sample1.setIgsn("igsn1");
    sample1.setSample("sample1");
    sample1.setPublish("Y");
    sample1.setProvince("axial valley");
    sample1.setLat(55.5);
    sample1.setLon(66.6);
    sample1.setDevice("trap, sediment");
    sample1.setLake("blue");

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");
    sample2.setLake("red");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSampleLink(
        cruiseName1,
        year1,
        sample1.getSample(),
        "data link 1",
        "link level 1",
        "link source 1",
        "link type 1",
        "Y");

    testUtils.insertCruiseLink(
        cruiseName1,
        year1,
        platform1,
        "data link 2",
        "link level 2",
        "link source 2",
        "link type 2",
        "Y",
        leg1);

    Interval interval1 = new Interval();
    interval1.setInterval(1);
    interval1.setLith1("terrigenous");
    interval1.setLith2("evaporite");
    interval1.setRockLith("sedimentary (pyroclastic)");
    interval1.setText1("gravel");
    interval1.setText2("sandy mud or ooze");
    interval1.setRockMin("xenoliths");
    interval1.setWeathMeta("weathering - light");
    interval1.setAges(Collections.singletonList("Quaternary"));

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");
    interval2.setAges(Collections.singletonList("Jurassic"));

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");
    interval3.setAges(Collections.singletonList("Permian"));

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");
    interval4.setAges(Collections.singletonList("Cambrian"));

    testUtils.insertInterval(cruiseName1, year1, sample2.getSample(), interval3);
    testUtils.insertInterval(cruiseName1, year1, sample2.getSample(), interval4);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core");
    sample3.setLake("wet");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("probe");
    sample4.setLake("dry");

    Sample sample5 = new Sample();
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);

    Interval interval5 = new Interval();
    interval5.setInterval(1);

    Interval interval6 = new Interval();
    interval6.setInterval(2);
    interval6.setLith1("phosphate");
    interval6.setText1("ash");
    interval6.setRockMin("muscovite");
    interval6.setWeathMeta("metamorphism - serpentinized");
    interval6.setAges(Collections.singletonList("Triassic"));

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    Long id = testUtils.getFacilityId(facility1);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/repositories/detail")
            .pathSegment(id.toString())
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());

    ObjectNode facility = objectMapper.createObjectNode();
    facility.put("id", testUtils.getFacilityId(facility1));
    facility.put("facility", "NOAA-Atlantic Oceanographic and Meteorol. Lab");
    facility.put("facility_code", "AOML");
    facility.put("sample_count", 2);
    facility.put("facility_comment", AOML_COMMENT);
    facility.put("inst_code", "03");
    facility.put("addr1", "NOAA-AOML");
    facility.put("addr2", "4301 Rickenbacker Causeway");
    facility.put("addr3", "Miami, FL 33149");
    facility.put("addr4", "USA");
    facility.put("contact1", "Charles Featherstone 1 305 361-4401");
    facility.put("contact2", "The AOML core repository is closed");
    facility.put("contact3", "no samples are available");
    facility.put("email_link", "charles.featherstone@noaa.gov");
    facility.put("url_link", "http://www.aoml.noaa.gov/");
    facility.put("other_link", "https://dx.doi.org/doi:10.7289/V5VM498W");

    assertEquals(objectMapper.readTree(facility.toString()), json);
  }

}