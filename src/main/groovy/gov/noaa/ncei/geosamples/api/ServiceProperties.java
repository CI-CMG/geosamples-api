package gov.noaa.ncei.geosamples.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("geosamples")
public class ServiceProperties {


  /*
  springdoc.version

      @Value('${ajp.port:8009}')
    int ajpPort

    @Value('${ajp.enabled:true}')
    boolean ajpEnabled

    @Value('${ajp.secret:none}')
    String ajpSecret

    // can be IP address for fully-qualified hostname. Default to binding on all addresses
    @Value('${ajp.server.address:0.0.0.0}')
   */

  @NotNull
  private Integer ajpPort;
  @NotNull
  private Boolean ajpEnabled;
  @NotBlank
  private String ajpSecret;
  @NotBlank
  private String ajpServerAddress;

  @NotBlank
  private String schema;
  @NotBlank
  private String sampleTable;
  @NotBlank
  private String facilityTable;
  @NotBlank
  private String intervalTable;
  @NotBlank
  private String linksTable;
  @NotBlank
  private String cruiseLinksTable;
  @NotBlank
  private String cruiseFacilityTable;
  @NotBlank
  private String cruisePlatformTable;
  @NotBlank
  private String cruiseTable;
  @NotBlank
  private String legTable;
  @NotBlank
  private String platformTable;


  public Integer getAjpPort() {
    return ajpPort;
  }

  public void setAjpPort(Integer ajpPort) {
    this.ajpPort = ajpPort;
  }

  public Boolean getAjpEnabled() {
    return ajpEnabled;
  }

  public void setAjpEnabled(Boolean ajpEnabled) {
    this.ajpEnabled = ajpEnabled;
  }

  public String getAjpSecret() {
    return ajpSecret;
  }

  public void setAjpSecret(String ajpSecret) {
    this.ajpSecret = ajpSecret;
  }

  public String getAjpServerAddress() {
    return ajpServerAddress;
  }

  public void setAjpServerAddress(String ajpServerAddress) {
    this.ajpServerAddress = ajpServerAddress;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getSampleTable() {
    return sampleTable;
  }

  public void setSampleTable(String sampleTable) {
    this.sampleTable = sampleTable;
  }

  public String getFacilityTable() {
    return facilityTable;
  }

  public void setFacilityTable(String facilityTable) {
    this.facilityTable = facilityTable;
  }

  public String getIntervalTable() {
    return intervalTable;
  }

  public void setIntervalTable(String intervalTable) {
    this.intervalTable = intervalTable;
  }

  public String getLinksTable() {
    return linksTable;
  }

  public void setLinksTable(String linksTable) {
    this.linksTable = linksTable;
  }

  public String getCruiseLinksTable() {
    return cruiseLinksTable;
  }

  public void setCruiseLinksTable(String cruiseLinksTable) {
    this.cruiseLinksTable = cruiseLinksTable;
  }

  public String getCruiseFacilityTable() {
    return cruiseFacilityTable;
  }

  public void setCruiseFacilityTable(String cruiseFacilityTable) {
    this.cruiseFacilityTable = cruiseFacilityTable;
  }

  public String getCruisePlatformTable() {
    return cruisePlatformTable;
  }

  public void setCruisePlatformTable(String cruisePlatformTable) {
    this.cruisePlatformTable = cruisePlatformTable;
  }

  public String getCruiseTable() {
    return cruiseTable;
  }

  public void setCruiseTable(String cruiseTable) {
    this.cruiseTable = cruiseTable;
  }

  public String getLegTable() {
    return legTable;
  }

  public void setLegTable(String legTable) {
    this.legTable = legTable;
  }

  public String getPlatformTable() {
    return platformTable;
  }

  public void setPlatformTable(String platformTable) {
    this.platformTable = platformTable;
  }

}
