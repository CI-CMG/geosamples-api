package gov.noaa.ncei.geosamples.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gov.noaa.ncei.geosamples.api.TestUtils;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
class IntervalControllerIT {

  @Autowired
  private TestRestTemplate restClient;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TestUtils testUtils;

  @Value("classpath:csv/interval1.csv")
  private Resource interval1Csv;

  @BeforeEach
  public void beforeEach() {
    testUtils.cleanDb();
  }

  @AfterEach
  public void afterEach() {
    testUtils.cleanDb();
  }

  @Test
  public void testGetIntervalsJson() throws Exception {
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
    interval1.setAge("Quaternary");

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");
    interval2.setAge("Jurassic");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");
    interval3.setAge("Permian");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");
    interval4.setAge("Cambrian");

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
    interval6.setAge("Triassic");

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    String imlgs = testUtils.getImlgs(cruiseName1, year1, sample1.getSample());
    String imlgs2 = testUtils.getImlgs(cruiseName1, year1, sample2.getSample());

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/intervals/detail")
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
    expectedJson.put("total_items", 4);
    expectedJson.put("items_per_page", 500);

    ObjectNode interval = objectMapper.createObjectNode();
    interval.put("id", testUtils.getIntervalId(imlgs, 1));
    interval.put("facility_code", "AOML");
    interval.put("platform", "Sea Biskit");
    interval.put("cruise", "CRUISE_1");
    interval.put("sample", "sample1");
    interval.put("interval", 1);
    interval.put("lith1", "terrigenous");
    interval.put("lith2", "evaporite");
    interval.put("rock_lith", "sedimentary (pyroclastic)");
    interval.put("text1", "gravel");
    interval.put("text2", "sandy mud or ooze");
    interval.put("rock_min", "xenoliths");
    interval.put("weath_meta", "weathering - light");
    interval.put("age", "Quaternary");
    interval.put("device", "trap, sediment");
    interval.put("imlgs", imlgs);
    items.add(interval);

    interval = objectMapper.createObjectNode();
    interval.put("id", testUtils.getIntervalId(imlgs, 2));
    interval.put("facility_code", "AOML");
    interval.put("platform", "Sea Biskit");
    interval.put("cruise", "CRUISE_1");
    interval.put("sample", "sample1");
    interval.put("interval", 2);
    interval.put("lith1", "volcanics");
    interval.put("text1", "lapilli");
    interval.put("rock_min", "garnet");
    interval.put("weath_meta", "metamorphism - amphibolite");
    interval.put("age", "Jurassic");
    interval.put("device", "trap, sediment");
    interval.put("imlgs", imlgs);
    items.add(interval);

    interval = objectMapper.createObjectNode();
    interval.put("id", testUtils.getIntervalId(imlgs2, 1));
    interval.put("facility_code", "AOML");
    interval.put("platform", "Sea Biskit");
    interval.put("cruise", "CRUISE_1");
    interval.put("sample", "sample2");
    interval.put("interval", 1);
    interval.put("lith2", "dolomite");
    interval.put("text2", "crusts");
    interval.put("rock_min", "biotite");
    interval.put("weath_meta", "weathering - heavy, very");
    interval.put("age", "Permian");
    interval.put("device", "core, dart");
    interval.put("imlgs", imlgs2);
    items.add(interval);

    interval = objectMapper.createObjectNode();
    interval.put("id", testUtils.getIntervalId(imlgs2, 2));
    interval.put("facility_code", "AOML");
    interval.put("platform", "Sea Biskit");
    interval.put("cruise", "CRUISE_1");
    interval.put("sample", "sample2");
    interval.put("interval", 2);
    interval.put("rock_lith", "igneous (extrusive/volcanic), latite");
    interval.put("age", "Cambrian");
    interval.put("device", "core, dart");
    interval.put("imlgs", imlgs2);
    items.add(interval);

    assertEquals(objectMapper.readTree(expectedJson.toString()), json);
  }

  @Test
  public void testGetIntervalsCount() throws Exception {
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
    interval1.setAge("Quaternary");

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");
    interval2.setAge("Jurassic");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");
    interval3.setAge("Permian");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");
    interval4.setAge("Cambrian");

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
    interval6.setAge("Triassic");

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/intervals/count")
            .queryParam("cruise", cruiseName1)
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());

    ObjectNode intervals = objectMapper.createObjectNode();
    intervals.put("count", 4);

    assertEquals(intervals, json);
  }

  @Test
  public void testLoad() throws Exception {
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
    interval1.setAge("Quaternary");

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");
    interval2.setAge("Jurassic");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");
    interval3.setAge("Permian");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");
    interval4.setAge("Cambrian");

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
    interval6.setAge("Triassic");

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    String imlgs = testUtils.getImlgs(cruiseName1, year1, sample1.getSample());
    Long id = testUtils.getIntervalId(imlgs, 1);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/intervals/detail")
            .pathSegment(Long.toString(id))
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());


    ObjectNode interval = objectMapper.createObjectNode();
    interval.put("id", id);
    interval.put("facility_code", "AOML");
    interval.put("platform", "Sea Biskit");
    interval.put("cruise", "CRUISE_1");
    interval.put("sample", "sample1");
    interval.put("interval", 1);
    interval.put("lith1", "terrigenous");
    interval.put("lith2", "evaporite");
    interval.put("rock_lith", "sedimentary (pyroclastic)");
    interval.put("text1", "gravel");
    interval.put("text2", "sandy mud or ooze");
    interval.put("rock_min", "xenoliths");
    interval.put("weath_meta", "weathering - light");
    interval.put("age", "Quaternary");
    interval.put("device", "trap, sediment");
    interval.put("imlgs", imlgs);


    assertEquals(objectMapper.readTree(interval.toString()), json);
  }

  @Test
  public void testGetIntervalsCsv() throws Exception {
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
    interval1.setAge("Quaternary");
    interval1.setDepthTop(4);
    interval1.setDepthBot(5);
    interval1.setComp1("calcareous, spines");
    interval1.setComp2("siliceous, sponge spicules");
    interval1.setComp3("iron or iron oxide");
    interval1.setComp4("fish teeth");
    interval1.setComp5("zeolites");
    interval1.setComp6("chert or porcelanite");
    interval1.setDescription("foo");
    interval1.setAbsoluteAgeTop("ageTop");
    interval1.setAbsoluteAgeBot("ageBot");
    interval1.setWeight(44.4);
    interval1.setRemark("no glass");
    interval1.setMunsellCode("10R 6/4");
    interval1.setExhaustCode("X");
    interval1.setPhotoLink("photo");
    interval1.setLake("wet");
    interval1.setIntComments("bar");
    interval1.setIgsn("igsn1");

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");
    interval2.setAge("Jurassic");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");
    interval3.setAge("Permian");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");
    interval4.setAge("Cambrian");

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
    interval6.setAge("Triassic");

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    String imlgs = testUtils.getImlgs(cruiseName1, year1, sample1.getSample());
    String imlgs2 = testUtils.getImlgs(cruiseName1, year1, sample2.getSample());

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/intervals/csv")
            .queryParam("cruise", cruiseName1)
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    assertEquals("attachment; filename=geosamples_export.csv", response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
    assertEquals("text/csv;charset=UTF-8", response.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0));

    String expectedCsv;
    try (InputStream inputStream = interval1Csv.getInputStream()) {
      expectedCsv = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    String imlgs1 = testUtils.getImlgs(cruiseName1, year1, sample1.getSample());


    expectedCsv = expectedCsv
        .replace("@id1@", testUtils.getIntervalId(imlgs1, 1).toString())
        .replace("@id2@", testUtils.getIntervalId(imlgs1, 2).toString())
        .replace("@id3@", testUtils.getIntervalId(imlgs2, 1).toString())
        .replace("@id4@", testUtils.getIntervalId(imlgs2, 2).toString())
        .replace("@imlgs1@", imlgs1)
        .replace("@imlgs2@", imlgs1)
        .replace("@imlgs3@", imlgs2)
        .replace("@imlgs4@", imlgs2)
        .replaceAll("\\n", "\r\n");

    assertEquals(expectedCsv, response.getBody());

  }

}