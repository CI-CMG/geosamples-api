package gov.noaa.ncei.geosamples.api.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gov.noaa.ncei.geosamples.api.TestUtils;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
class SampleControllerIT {

  @Autowired
  private TestRestTemplate restClient;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TestUtils testUtils;

  @Value("classpath:csv/sample1.csv")
  private Resource sample1Csv;

  @BeforeEach
  public void beforeEach() {
    testUtils.cleanDb();
  }

  @AfterEach
  public void afterEach() {
    testUtils.cleanDb();
  }

  @Test
  public void testGetSamplesJson() throws Exception {
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
        UriComponentsBuilder.fromPath("/api/samples/summary")
            .queryParam("order", "imlgs:asc")
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
    expectedJson.put("total_items", 5);
    expectedJson.put("items_per_page", 500);

    ObjectNode facility = objectMapper.createObjectNode();
    facility.put("id", testUtils.getFacilityId("AOML"));
    facility.put("facility", "NOAA-Atlantic Oceanographic and Meteorol. Lab");
    facility.put("facility_code", "AOML");
    facility.put("other_link", "https://dx.doi.org/doi:10.7289/V5VM498W");

    ObjectNode sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName1, year1, sample1.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_1");
    sample.put("leg", "LEFT-1");
    sample.put("platform", "Sea Biskit");
    sample.put("igsn", "igsn1");
    sample.put("sample", "sample1");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "trap, sediment");

    items.add(sample);

    sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName1, year1, sample2.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_1");
    sample.put("leg", "RIGHT-1");
    sample.put("platform", "Sea Biskit");
    sample.put("igsn", "igsn2");
    sample.put("sample", "sample2");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "core, dart");

    items.add(sample);

    facility = objectMapper.createObjectNode();
    facility.put("id", testUtils.getFacilityId("USGSMP"));
    facility.put("facility", "USGS Pacific Coastal and Marine Science Center");
    facility.put("facility_code", "USGSMP");
    facility.put("other_link", "https://dx.doi.org/doi:10.7289/V55T3HGJ");

    sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName2, year2, sample3.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_2");
    sample.put("leg", "LEFT-2");
    sample.put("platform", "Susie Q");
    sample.put("igsn", "igsn3");
    sample.put("sample", "sample1");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "core");

    items.add(sample);

    sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName2, year2, sample4.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_2");
    sample.put("platform", "Susie Q");
    sample.put("igsn", "igsn4");
    sample.put("sample", "sample2");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "probe");

    items.add(sample);


    sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName2, year2, sample5.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_2");
    sample.put("leg", "LEFT-2");
    sample.put("platform", "Susie Q");
    sample.put("sample", "sample3");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "core, dart");

    items.add(sample);


    assertEquals(objectMapper.readTree(expectedJson.toString()), objectMapper.readTree(json.toString()));
  }

  @Test
  public void testGetSamplesCount() throws Exception {
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
        UriComponentsBuilder.fromPath("/api/samples/count")
            .queryParam("order", "imlgs:asc")
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());
    ObjectNode expectedJson = objectMapper.createObjectNode();
    expectedJson.put("count", 5);

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetSamplesFull() throws Exception {
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
        UriComponentsBuilder.fromPath("/api/samples/detail")
            .queryParam("order", "imlgs:asc")
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());
    ObjectNode expectedJson = objectMapper.createObjectNode();
    List<JsonNode> items = new ArrayList<>();


    ObjectNode facility = objectMapper.createObjectNode();
    facility.put("id", testUtils.getFacilityId("AOML"));
    facility.put("facility", "NOAA-Atlantic Oceanographic and Meteorol. Lab");
    facility.put("facility_code", "AOML");
    facility.put("other_link", "https://dx.doi.org/doi:10.7289/V5VM498W");

    ObjectNode sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName1, year1, sample1.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_1");
    sample.put("leg", "LEFT-1");
    sample.put("platform", "Sea Biskit");
    sample.put("igsn", "igsn1");
    sample.put("sample", "sample1");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "trap, sediment");
    sample.put("publish", "Y");
    sample.put("lake", "blue");
    sample.put("province", "axial valley");
    sample.put("last_update", testUtils.getLastUpdate(sample.get("imlgs").asText()));

    items.add(sample);

    sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName1, year1, sample2.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_1");
    sample.put("leg", "RIGHT-1");
    sample.put("platform", "Sea Biskit");
    sample.put("igsn", "igsn2");
    sample.put("sample", "sample2");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "core, dart");
    sample.put("publish", "Y");
    sample.put("lake", "red");
    sample.put("province", "delta or cone");
    sample.put("last_update", testUtils.getLastUpdate(sample.get("imlgs").asText()));

    items.add(sample);

    facility = objectMapper.createObjectNode();
    facility.put("id", testUtils.getFacilityId("USGSMP"));
    facility.put("facility", "USGS Pacific Coastal and Marine Science Center");
    facility.put("facility_code", "USGSMP");
    facility.put("other_link", "https://dx.doi.org/doi:10.7289/V55T3HGJ");

    sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName2, year2, sample3.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_2");
    sample.put("leg", "LEFT-2");
    sample.put("platform", "Susie Q");
    sample.put("igsn", "igsn3");
    sample.put("sample", "sample1");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "core");
    sample.put("publish", "Y");
    sample.put("lake", "wet");
    sample.put("province", "fjord");
    sample.put("last_update", testUtils.getLastUpdate(sample.get("imlgs").asText()));

    items.add(sample);

    sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName2, year2, sample4.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_2");
    sample.put("platform", "Susie Q");
    sample.put("igsn", "igsn4");
    sample.put("sample", "sample2");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "probe");
    sample.put("publish", "Y");
    sample.put("lake", "dry");
    sample.put("province", "estuary");
    sample.put("last_update", testUtils.getLastUpdate(sample.get("imlgs").asText()));

    items.add(sample);

    sample = objectMapper.createObjectNode();
    sample.put("imlgs", testUtils.getImlgs(cruiseName2, year2, sample5.getSample()));
    sample.replace("facility", facility);
    sample.put("cruise", "CRUISE_2");
    sample.put("leg", "LEFT-2");
    sample.put("platform", "Susie Q");
    sample.put("sample", "sample3");
    sample.put("lat", 55.5);
    sample.put("lon", 66.6);
    sample.put("device", "core, dart");
    sample.put("publish", "Y");
    sample.put("last_update", testUtils.getLastUpdate(sample.get("imlgs").asText()));

    items.add(sample);

    Collections.sort(items, (Comparator.comparing(o -> o.get("imlgs").asText())));

    ArrayNode itemsJson = objectMapper.createArrayNode();
    itemsJson.addAll(items);
    expectedJson.replace("items", itemsJson);
    expectedJson.put("page", 1);
    expectedJson.put("total_pages", 1);
    expectedJson.put("total_items", 5);
    expectedJson.put("items_per_page", 500);


    assertEquals(objectMapper.readTree(expectedJson.toString()), objectMapper.readTree(json.toString()));
  }

  @Test
  public void testGetSamplesById() throws Exception {
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

    List<String> ages1 = new ArrayList<>();
    ages1.add("Jurassic");
    ages1.add("Triassic");
    Interval interval1 = new Interval();
    interval1.setInterval(1);
    interval1.setLith1("terrigenous");
    interval1.setLith2("evaporite");
    interval1.setRockLith("sedimentary (pyroclastic)");
    interval1.setText1("gravel");
    interval1.setText2("sandy mud or ooze");
    interval1.setRockMin("xenoliths");
    interval1.setWeathMeta("weathering - light");
    interval1.setAges(ages1);

    List<String> ages2 = new ArrayList<>();
    ages2.add("Jurassic");
    ages2.add("Precambrian");
    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");
    interval2.setAges(ages2);

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

    String imlgs = testUtils.getImlgs(cruiseName1, year1, sample1.getSample());

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/samples/detail")
            .pathSegment(imlgs)
            .queryParam("order", "imlgs:asc")
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    ObjectNode expectedJson = objectMapper.createObjectNode();
    expectedJson.put("imlgs", imlgs);
    expectedJson.put("platform", "Sea Biskit");
    expectedJson.put("sample", "sample1");
    expectedJson.put("device", "trap, sediment");
    expectedJson.put("lat", 55.5);
    expectedJson.put("lon", 66.6);
    expectedJson.put("igsn", "igsn1");
    expectedJson.put("leg", "LEFT-1");
    expectedJson.put("province", "axial valley");
    expectedJson.put("lake", "blue");
    expectedJson.put("last_update", testUtils.getLastUpdate(imlgs));
    expectedJson.put("publish", "Y");

    ArrayNode sampleLinks = objectMapper.createArrayNode();
    ObjectNode sampleLink = objectMapper.createObjectNode();
    sampleLink.put("link", "data link 1");
    sampleLink.put("link_level", "link level 1");
    sampleLink.put("source", "link source 1");
    sampleLink.put("type", "link type 1");
    sampleLinks.add(sampleLink);

    expectedJson.replace("links", sampleLinks);

    ArrayNode intervals = objectMapper.createArrayNode();
    ObjectNode interval = objectMapper.createObjectNode();
    interval.put("id", testUtils.getIntervalId(imlgs, 1));
    interval.put("facility_code", "AOML");
    interval.put("platform", "Sea Biskit");
    interval.put("cruise", "CRUISE_1");
    interval.put("sample", "sample1");
    interval.put("device", "trap, sediment");
    interval.put("interval", 1);
    interval.put("lith1", "terrigenous");
    interval.put("lith2", "evaporite");
    interval.put("text1", "gravel");
    interval.put("text2", "sandy mud or ooze");
    ArrayNode ages = objectMapper.createArrayNode();
    ages1.forEach(ages::add);
    interval.put("ages", ages);
    interval.put("rock_lith", "sedimentary (pyroclastic)");
    interval.put("rock_min", "xenoliths");
    interval.put("weath_meta", "weathering - light");

    interval.put("imlgs", imlgs);
    intervals.add(interval);

    interval = objectMapper.createObjectNode();
    interval.put("id", testUtils.getIntervalId(imlgs, 2));
    interval.put("facility_code", "AOML");
    interval.put("platform", "Sea Biskit");
    interval.put("cruise", "CRUISE_1");
    interval.put("sample", "sample1");
    interval.put("device", "trap, sediment");
    interval.put("interval", 2);
    interval.put("lith1", "volcanics");
    interval.put("text1", "lapilli");
    interval.put("rock_min", "garnet");
    interval.put("weath_meta", "metamorphism - amphibolite");

    ages = objectMapper.createArrayNode();
    ages2.forEach(ages::add);
    interval.put("ages", ages);
    interval.put("imlgs", imlgs);
    intervals.add(interval);

    expectedJson.replace("intervals", intervals);

    ObjectNode cruise = objectMapper.createObjectNode();
    cruise.put("id", testUtils.getCruiseId("CRUISE_1", 2020));
    cruise.put("cruise", "CRUISE_1");

    ArrayNode cruiseLinks = objectMapper.createArrayNode();
    ObjectNode cruiseLink = objectMapper.createObjectNode();
    cruiseLink.put("link", "data link 2");
    cruiseLink.put("link_level", "link level 2");
    cruiseLink.put("source", "link source 2");
    cruiseLink.put("type", "link type 2");
    cruiseLinks.add(cruiseLink);

    cruise.replace("links", cruiseLinks);
    expectedJson.replace("cruise", cruise);

    ObjectNode facility = objectMapper.createObjectNode();
    facility.put("id", testUtils.getFacilityId("AOML"));
    facility.put("facility_code", "AOML");
    facility.put("other_link", "https://dx.doi.org/doi:10.7289/V5VM498W");
    facility.put("facility", "NOAA-Atlantic Oceanographic and Meteorol. Lab");
    expectedJson.replace("facility", facility);

    //need to reserialize before comparing due to int to long node differences
    assertEquals(objectMapper.readTree(expectedJson.toString()), objectMapper.readTree(response.getBody()));

  }

  @Test
  public void testGetSamplesCsv() throws Exception {
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
    sample1.setBeginDate("20200401");
    sample1.setEndDate("20200501");
    sample1.setEndLat(56.5);
    sample1.setEndLon(67.6);
    sample1.setLatLonOrig("N");
    sample1.setWaterDepth(40);
    sample1.setEndWaterDepth(50);
    sample1.setStorageMeth("frozen");
    sample1.setCoredLength(23);
    sample1.setCoredLengthMm(11);
    sample1.setCoredDiam(12);
    sample1.setCoredDiamMm(5);
    sample1.setPi("bob");
    sample1.setOtherLink("other");
    sample1.setSampleComments("test1");
    sample1.setShowSampl("show");

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
        UriComponentsBuilder.fromPath("/api/samples/csv")
            .queryParam("order", "imlgs:asc")
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    assertEquals("attachment; filename=geosamples_export.csv", response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
    assertEquals("text/csv;charset=UTF-8", response.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0));

    String expectedCsv;
    try (InputStream inputStream = sample1Csv.getInputStream()) {
      expectedCsv = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    String imlgs1 = testUtils.getImlgs(cruiseName1, year1, sample1.getSample());
    String imlgs2 = testUtils.getImlgs(cruiseName1, year1, sample2.getSample());
    String imlgs3 = testUtils.getImlgs(cruiseName2, year2, sample3.getSample());
    String imlgs4 = testUtils.getImlgs(cruiseName2, year2, sample5.getSample());
    String imlgs5 = testUtils.getImlgs(cruiseName2, year2, sample4.getSample());

    expectedCsv = expectedCsv
        .replace("@last_update1@", testUtils.getLastUpdate(imlgs1))
        .replace("@last_update2@", testUtils.getLastUpdate(imlgs2))
        .replace("@last_update3@", testUtils.getLastUpdate(imlgs3))
        .replace("@last_update4@", testUtils.getLastUpdate(imlgs4))
        .replace("@last_update5@", testUtils.getLastUpdate(imlgs5))
        .replace("@imlgs1@", imlgs1)
        .replace("@imlgs2@", imlgs2)
        .replace("@imlgs3@", imlgs3)
        .replace("@imlgs4@", imlgs4)
        .replace("@imlgs5@", imlgs5)
        .replaceAll("\\n", "\r\n");

    assertEquals(expectedCsv, response.getBody());

  }

}