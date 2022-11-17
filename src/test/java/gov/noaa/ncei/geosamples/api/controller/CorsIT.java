package gov.noaa.ncei.geosamples.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
class CorsIT {

  @Autowired
  private TestRestTemplate restClient;

  @LocalServerPort
  private int randomServerPort;

  @Test
  public void testRoot() throws Exception {

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Access-Control-Request-Method", "GET");
    headers.add("Origin", "https://example.com");

    ResponseEntity<String> response = restClient.exchange(
        "/",
        HttpMethod.OPTIONS,
        new HttpEntity<>(headers),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());
    assertEquals(new HashSet<>(Arrays.asList(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.OPTIONS)), response.getHeaders().getAllow());

  }

  @Test
  public void testRootNoSlash() throws Exception {

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Access-Control-Request-Method", "GET");
    headers.add("Origin", "https://example.com");

    ResponseEntity<String> response = restClient.exchange(
        "http://localhost:" + randomServerPort + "/imlgs",
        HttpMethod.OPTIONS,
        new HttpEntity<>(headers),
        String.class
    );
    assertEquals(200, response.getStatusCode().value());
    assertEquals(new HashSet<>(Arrays.asList(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.OPTIONS)), response.getHeaders().getAllow());

  }

}