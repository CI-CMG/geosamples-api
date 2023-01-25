package gov.noaa.ncei.geosamples.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class AttributesControllerIT {

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
  public void testGetDepthRange() throws Exception {
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
    sample1.setDevice("core, dart");
    sample1.setStorageMeth("refrigerated");
    sample1.setWaterDepth(17);

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");
    sample2.setStorageMeth("frozen");
    sample2.setWaterDepth(15);

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core, dart");
    sample3.setStorageMeth("room temperature, dry");
    sample3.setWaterDepth(25);

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("core, dart");
    sample4.setStorageMeth("room temperature, dry");
    sample4.setWaterDepth(20);

    Sample sample5 = new Sample();
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/depth_range",
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());
    ObjectNode expectedJson = objectMapper.createObjectNode();
    expectedJson.put("min", 15);
    expectedJson.put("max", 25);

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetDepthRangeQuery() throws Exception {
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
    sample1.setDevice("core, dart");
    sample1.setStorageMeth("refrigerated");
    sample1.setWaterDepth(17);

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");
    sample2.setStorageMeth("frozen");
    sample2.setWaterDepth(15);

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core, dart");
    sample3.setStorageMeth("room temperature, dry");
    sample3.setWaterDepth(25);

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("core, dart");
    sample4.setStorageMeth("room temperature, dry");
    sample4.setWaterDepth(20);

    Sample sample5 = new Sample();
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/depth_range")
            .queryParam("cruise", cruiseName2)
            .build().toString(),
        HttpMethod.GET,
        new HttpEntity<>(null),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());

    JsonNode json = objectMapper.readTree(response.getBody());
    ObjectNode expectedJson = objectMapper.createObjectNode();
    expectedJson.put("min", 20);
    expectedJson.put("max", 25);

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetStorageMethods() throws Exception {
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
    sample1.setDevice("core, dart");
    sample1.setStorageMeth("refrigerated");

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");
    sample2.setStorageMeth("frozen");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core, dart");
    sample3.setStorageMeth("room temperature, dry");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("core, dart");
    sample4.setStorageMeth("room temperature, dry");

    Sample sample5 = new Sample();
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/storage_methods",
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
    expectedJson.put("total_items", 3);
    expectedJson.put("items_per_page", 500);
    items.add("frozen");
    items.add("refrigerated");
    items.add("room temperature, dry");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetStorageMethodsQuery() throws Exception {

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
    sample1.setDevice("core, dart");
    sample1.setStorageMeth("refrigerated");

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");
    sample2.setStorageMeth("frozen");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core, dart");
    sample3.setStorageMeth("room temperature, dry");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("core, dart");
    sample4.setStorageMeth("room temperature, dry");

    Sample sample5 = new Sample();
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/storage_methods")
            .queryParam("cruise", cruiseName2)
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
    items.add("room temperature, dry");

    assertEquals(expectedJson, json);

  }


  @Test
  public void testGetProvinces() throws Exception {

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
    sample1.setDevice("core, dart");

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core, dart");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("core, dart");

    Sample sample5 = new Sample();
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/physiographic_provinces",
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
    items.add("axial valley");
    items.add("delta or cone");
    items.add("estuary");
    items.add("fjord");

    assertEquals(expectedJson, json);

  }

  @Test
  public void testGetProvincesQuery() throws Exception {

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
    sample1.setDevice("core, dart");

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core, dart");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("core, dart");

    Sample sample5 = new Sample();
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/physiographic_provinces")
            .queryParam("cruise", cruiseName2)
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
    expectedJson.put("total_items", 2);
    expectedJson.put("items_per_page", 500);
    items.add("estuary");
    items.add("fjord");

    assertEquals(expectedJson, json);

  }

  @Test
  public void testGetDevices() throws Exception {
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

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("probe");

    Sample sample5 = new Sample();
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/devices",
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
    items.add("core");
    items.add("core, dart");
    items.add("probe");
    items.add("trap, sediment");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetDevicesQuery() throws Exception {
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

    Sample sample2 = new Sample();
    sample2.setIgsn("igsn2");
    sample2.setSample("sample2");
    sample2.setPublish("Y");
    sample2.setProvince("delta or cone");
    sample2.setLat(55.5);
    sample2.setLon(66.6);
    sample2.setDevice("core, dart");

    testUtils.insertSample(cruiseName1, year1, leg1, facility1, platform1, sample1);
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

    Sample sample3 = new Sample();
    sample3.setIgsn("igsn3");
    sample3.setSample("sample1");
    sample3.setPublish("Y");
    sample3.setProvince("fjord");
    sample3.setLat(55.5);
    sample3.setLon(66.6);
    sample3.setDevice("core");

    Sample sample4 = new Sample();
    sample4.setIgsn("igsn4");
    sample4.setSample("sample2");
    sample4.setPublish("Y");
    sample4.setProvince("estuary");
    sample4.setLat(55.5);
    sample4.setLon(66.6);
    sample4.setDevice("probe");

    Sample sample5 = new Sample();
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/devices")
            .queryParam("cruise", cruiseName2)
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
    expectedJson.put("total_items", 3);
    expectedJson.put("items_per_page", 500);
    items.add("core");
    items.add("core, dart");
    items.add("probe");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetLakes() throws Exception {
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
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

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
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/lakes",
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
    items.add("blue");
    items.add("dry");
    items.add("red");
    items.add("wet");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetLakesQuery() throws Exception {
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
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

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
    sample5.setIgsn("igsn5");
    sample5.setSample("sample3");
    sample5.setPublish("Y");
    sample5.setLat(55.5);
    sample5.setLon(66.6);
    sample5.setDevice("core, dart");

    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample3);
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/lakes")
            .queryParam("cruise", cruiseName2)
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
    expectedJson.put("total_items", 2);
    expectedJson.put("items_per_page", 500);
    items.add("dry");
    items.add("wet");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetIgsn() throws Exception {
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
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

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
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/igsn",
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
    items.add("igsn1");
    items.add("igsn2");
    items.add("igsn3");
    items.add("igsn4");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetIgsnQuery() throws Exception {
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
    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);

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
    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/igsn")
            .queryParam("cruise", cruiseName2)
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
    expectedJson.put("total_items", 2);
    expectedJson.put("items_per_page", 500);
    items.add("igsn3");
    items.add("igsn4");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetPlatforms() throws Exception {
    String cruiseName1 = "CRUISE_1";
    int year1 = 2020;
    String platform1 = "Sea Biskit";
    String facility1 = "AOML";
    String leg1 = "LEFT-1";
    String leg2 = "RIGHT-1";

    String cruiseName2 = "CRUISE_1";
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
        UriComponentsBuilder.fromPath("/api/platforms")
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
    expectedJson.put("total_items", 2);
    expectedJson.put("items_per_page", 500);
    items.add("Sea Biskit");
    items.add("Susie Q");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetrockLithologies() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/rock_lithologies",
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
    expectedJson.put("total_items", 2);
    expectedJson.put("items_per_page", 500);
    items.add("igneous (extrusive/volcanic), latite");
    items.add("sedimentary (pyroclastic)");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetRockLithologiesQuery() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");

    Interval interval4 = new Interval();
    interval4.setInterval(2);

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
    interval6.setRockLith("igneous (extrusive/volcanic), latite");

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/rock_lithologies")
            .queryParam("cruise", cruiseName2)
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
    items.add("igneous (extrusive/volcanic), latite");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetLithologies() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/lithologies",
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
    items.add("dolomite");
    items.add("evaporite");
    items.add("phosphate");
    items.add("terrigenous");
    items.add("volcanics");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetLithologiesQuery() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/lithologies")
            .queryParam("cruise", cruiseName2)
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
    items.add("phosphate");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetTextures() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/textures",
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
    items.add("ash");
    items.add("crusts");
    items.add("gravel");
    items.add("lapilli");
    items.add("sandy mud or ooze");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetTexturesQuery() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/textures")
            .queryParam("cruise", cruiseName2)
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
    items.add("ash");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetMineralogies() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/mineralogies",
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
    items.add("biotite");
    items.add("garnet");
    items.add("muscovite");
    items.add("xenoliths");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetMineralogiesQuery() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/mineralogies")
            .queryParam("cruise", cruiseName2)
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
    items.add("muscovite");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetWeathering() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/weathering",
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
    expectedJson.put("total_items", 2);
    expectedJson.put("items_per_page", 500);

    items.add("heavy, very");
    items.add("light");

    assertEquals(expectedJson, json);
  }


  @Test
  public void testGetWeatheringQuery() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/weathering")
            .queryParam("cruise", cruiseName2)
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
    expectedJson.put("total_pages", 0);
    expectedJson.put("total_items", 0);
    expectedJson.put("items_per_page", 500);

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetMetamorphism() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        "/api/metamorphism",
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
    expectedJson.put("total_items", 2);
    expectedJson.put("items_per_page", 500);

    items.add("amphibolite");
    items.add("serpentinized");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetMetamorphismQuery() throws Exception {
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

    Interval interval2 = new Interval();
    interval2.setInterval(2);
    interval2.setLith1("volcanics");
    interval2.setText1("lapilli");
    interval2.setRockMin("garnet");
    interval2.setWeathMeta("metamorphism - amphibolite");

    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval1);
    testUtils.insertInterval(cruiseName1, year1, sample1.getSample(), interval2);

    testUtils.insertSample(cruiseName1, year1, leg2, facility1, platform1, sample2);
    Interval interval3 = new Interval();
    interval3.setInterval(1);
    interval3.setLith2("dolomite");
    interval3.setText2("crusts");
    interval3.setRockMin("biotite");
    interval3.setWeathMeta("weathering - heavy, very");

    Interval interval4 = new Interval();
    interval4.setInterval(2);
    interval4.setRockLith("igneous (extrusive/volcanic), latite");

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

    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval5);
    testUtils.insertInterval(cruiseName2, year2, sample3.getSample(), interval6);

    testUtils.insertSample(cruiseName2, year2, leg4, facility2, platform2, sample4);
    testUtils.insertSample(cruiseName2, year2, leg3, facility2, platform2, sample5);

    ResponseEntity<String> response = restClient.exchange(
        UriComponentsBuilder.fromPath("/api/metamorphism")
            .queryParam("cruise", cruiseName2)
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

    items.add("serpentinized");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetGeologicAges() throws Exception {
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
    interval5.setAges(Collections.singletonList("Triassic"));

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
        "/api/geologic_ages",
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

    items.add("Cambrian");
    items.add("Jurassic");
    items.add("Permian");
    items.add("Quaternary");
    items.add("Triassic");

    assertEquals(expectedJson, json);
  }

  @Test
  public void testGetGeologicAgesQuery() throws Exception {
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
        UriComponentsBuilder.fromPath("/api/geologic_ages")
            .queryParam("cruise", cruiseName2)
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

    items.add("Triassic");

    assertEquals(expectedJson, json);
  }

}