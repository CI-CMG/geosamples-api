package gov.noaa.ncei.geosamples.api;

import javax.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("geosamples")
public class ServiceProperties {

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
