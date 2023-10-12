package gov.noaa.ncei.geosamples.api.model;

import gov.noaa.ncei.geosamples.api.view.PagingParameters;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.locationtech.jts.geom.Geometry;

public class GeosampleSearchParameterObject implements PagingParameters {

  private static final List<String> DEFAULT_SORT = Arrays.asList(
      "sample:asc",
      "platform:asc",
      "facility_code:asc",
      "cruise:asc",
      "leg:asc"
  );


  private String repository;
  @Parameter(name = "bbox", description = "Comma separated list of lat / lon values representing a box to search within: xmin,ymin,xmax,ymax")
  @ValidBbox
  private String bbox;
  private String platform;
  private String lake;
  private String cruise;
  private String device;
  @Parameter(name = "start_date")
  @Size(min = 4, max = 8)
  private String startDate;
  @Parameter(name = "start_date_begins_with")
  @Size(min = 4, max = 8)
  private String startDateBeginsWith;

  //    Date startDate
  @Parameter(name = "min_depth")
  @Min(0L)
  private Integer minDepth;
  @Parameter(name = "max_depth")
  private Integer maxDepth;
  private String igsn;
  private String lithology;
  @Parameter(name = "rock_lithology")
  private String rockLithology;
  private String texture;
  private String mineralogy;
  private String weathering;
  private String metamorphism;
  private String composition;
  @Parameter(name = "storage_method")
  private String storageMethod;
  private String province;
  private String age;
  @Size(min = 12, max = 12)
  private String imlgs;

  @Parameter(name = "cruise_id")
  private Long cruiseId;
  @Parameter(name = "cruise_year")
  private Integer cruiseYear;
  @Parameter(name = "platform_id")
  private Long platformId;
  @Parameter(name = "facility_id")
  private Long facilityId;
  @Parameter(name = "interval_id")
  private Long intervalId;
  private String leg;

  private Geometry aoi;

  @Parameter(
      name = "aoi",
      description = "Search within this geometry.  Format is WKT (OGC well known text) format. Ex. POLYGON ((30.0 10.0, 40.0 40.0, 20.0 40.0, 10.0 20.0, 30.0 10.0))",
      schema = @Schema(
          externalDocs = @ExternalDocumentation(url = "https://en.wikipedia.org/wiki/Well-known_text_representation_of_geometry")
      )
  )
  private String aoiDocumentation;

  //This is not a real field.  This is here to properly display the aoi parameter in swagger.  DO NOT USE!
  public String getAoiDocumentation() {
    throw new UnsupportedOperationException("This is a workaround to display swagger documentation for geometries");
  }


  @Min(1)
  private int page;
  @Min(1)
  @Max(2000)
  @Parameter(name = "items_per_page")
  private int itemsPerPage;
  private List<String> order = DEFAULT_SORT;

  @ConstructorProperties({
      "repository",
      "bbox",
      "platform",
      "lake",
      "cruise",
      "device",
      "start_date",
      "min_depth",
      "max_depth",
      "igsn",
      "lithology",
      "rock_lithology",
      "texture",
      "mineralogy",
      "weathering",
      "metamorphism",
      "composition",
      "storage_method",
      "province",
      "age",
      "imlgs",
      "cruise_id",
      "cruise_year",
      "platform_id",
      "facility_id",
      "interval_id",
      "leg",
      "page",
      "items_per_page",
      "start_date_begins_with",
      "order",
      "aoi"
  })
  public GeosampleSearchParameterObject(
      String repository,
      String bbox,
      String platform,
      String lake,
      String cruise,
      String device,
      String startDate,
      Integer minDepth,
      Integer maxDepth,
      String igsn,
      String lithology,
      String rockLithology,
      String texture,
      String mineralogy,
      String weathering,
      String metamorphism,
      String composition,
      String storageMethod,
      String province,
      String age,
      String imlgs,
      Long cruiseId,
      Integer cruiseYear,
      Long platformId,
      Long facilityId,
      Long intervalId,
      String leg,
      Integer page,
      Integer itemsPerPage,
      String startDateBeginsWith,
      List<String> order,
      Geometry areaOfInterest) {
    this.repository = repository;
    this.bbox = bbox;
    this.platform = platform;
    this.lake = lake;
    this.cruise = cruise;
    this.device = device;
    this.startDate = startDate;
    this.minDepth = minDepth;
    this.maxDepth = maxDepth;
    this.igsn = igsn;
    this.lithology = lithology;
    this.rockLithology = rockLithology;
    this.texture = texture;
    this.mineralogy = mineralogy;
    this.weathering = weathering;
    this.metamorphism = metamorphism;
    this.composition = composition;
    this.storageMethod = storageMethod;
    this.province = province;
    this.age = age;
    this.imlgs = imlgs;
    this.cruiseId = cruiseId;
    this.cruiseYear = cruiseYear;
    this.platformId = platformId;
    this.facilityId = facilityId;
    this.intervalId = intervalId;
    this.leg = leg;
    this.page = page == null ? 1 : page;
    this.itemsPerPage = itemsPerPage == null ? 500 : itemsPerPage;
    this.startDateBeginsWith = startDateBeginsWith;
    this.order = order == null || order.isEmpty() ? DEFAULT_SORT : new ArrayList<>(order);
    this.aoi = areaOfInterest;
  }

  private static String trim(String s) {
    return s == null ? null : s.trim();
  }

  public Long getPlatformId() {
    return platformId;
  }

  public void setPlatformId(Long platformId) {
    this.platformId = platformId;
  }

  public Long getFacilityId() {
    return facilityId;
  }

  public void setFacilityId(Long facilityId) {
    this.facilityId = facilityId;
  }

  public Long getIntervalId() {
    return intervalId;
  }

  public void setIntervalId(Long intervalId) {
    this.intervalId = intervalId;
  }

  public String getLeg() {
    return leg;
  }

  public void setLeg(String leg) {
    this.leg = trim(leg);
  }

  public String getRepository() {
    return repository;
  }

  public void setRepository(String repository) {
    this.repository = trim(repository);
  }

  public String getBbox() {
    return bbox;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = trim(platform);
  }

  public String getLake() {
    return lake;
  }

  public void setLake(String lake) {
    this.lake = trim(lake);
  }

  public String getCruise() {
    return cruise;
  }

  public void setCruise(String cruise) {
    this.cruise = trim(cruise);
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = trim(device);
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = trim(startDate);
  }

  public Integer getMinDepth() {
    return minDepth;
  }

  public void setMinDepth(Integer minDepth) {
    this.minDepth = minDepth;
  }

  public Integer getMaxDepth() {
    return maxDepth;
  }

  public void setMaxDepth(Integer maxDepth) {
    this.maxDepth = maxDepth;
  }

  public String getIgsn() {
    return igsn;
  }

  public void setIgsn(String igsn) {
    this.igsn = trim(igsn);
  }

  public String getLithology() {
    return lithology;
  }

  public void setLithology(String lithology) {
    this.lithology = trim(lithology);
  }

  public String getRockLithology() {
    return rockLithology;
  }

  public void setRockLithology(String rockLithology) {
    this.rockLithology = trim(rockLithology);
  }

  public String getComposition() {
    return composition;
  }

  public void setComposition(String composition) {
    this.composition = trim(composition);
  }

  public String getTexture() {
    return texture;
  }

  public void setTexture(String texture) {
    this.texture = trim(texture);
  }

  public String getMineralogy() {
    return mineralogy;
  }

  public void setMineralogy(String mineralogy) {
    this.mineralogy = trim(mineralogy);
  }

  public String getWeathering() {
    return weathering;
  }

  public void setWeathering(String weathering) {
    this.weathering = trim(weathering);
  }

  public String getMetamorphism() {
    return metamorphism;
  }

  public void setMetamorphism(String metamorphism) {
    this.metamorphism = trim(metamorphism);
  }

  public String getStorageMethod() {
    return storageMethod;
  }

  public void setStorageMethod(String storageMethod) {
    this.storageMethod = trim(storageMethod);
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = trim(province);
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = trim(age);
  }

  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = trim(imlgs);
  }

  public Integer getCruiseYear() {
    return cruiseYear;
  }

  public void setCruiseYear(Integer cruiseYear) {
    this.cruiseYear = cruiseYear;
  }

  public Long getCruiseId() {
    return cruiseId;
  }

  public void setCruiseId(Long cruiseId) {
    this.cruiseId = cruiseId;
  }

  public void setBbox(String bbox) {
    this.bbox = bbox;
  }

  public String getStartDateBeginsWith() {
    return startDateBeginsWith;
  }

  public void setStartDateBeginsWith(String startDateBeginsWith) {
    this.startDateBeginsWith = startDateBeginsWith;
  }

  public Geometry getAoi() {
    return aoi;
  }

  public void setAoi(Geometry aoi) {
    this.aoi = aoi;
  }

  @Override
  public int getPage() {
    return page;
  }

  @Override
  public void setPage(int page) {
    this.page = page;
  }

  @Override
  public int getItemsPerPage() {
    return itemsPerPage;
  }

  @Override
  public void setItemsPerPage(int itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

  @Override
  public List<String> getOrder() {
    return order;
  }

  @Override
  public void setOrder(List<String> order) {
    if (order == null || order.isEmpty()) {
      order = DEFAULT_SORT;
    }
    this.order = new ArrayList<>(order);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeosampleSearchParameterObject that = (GeosampleSearchParameterObject) o;
    return page == that.page && itemsPerPage == that.itemsPerPage && Objects.equals(repository, that.repository) && Objects.equals(
        bbox, that.bbox) && Objects.equals(platform, that.platform) && Objects.equals(lake, that.lake) && Objects.equals(
        cruise, that.cruise) && Objects.equals(device, that.device) && Objects.equals(startDate, that.startDate)
        && Objects.equals(startDateBeginsWith, that.startDateBeginsWith) && Objects.equals(minDepth, that.minDepth)
        && Objects.equals(maxDepth, that.maxDepth) && Objects.equals(igsn, that.igsn) && Objects.equals(lithology,
        that.lithology) && Objects.equals(rockLithology, that.rockLithology) && Objects.equals(texture, that.texture)
        && Objects.equals(mineralogy, that.mineralogy) && Objects.equals(weathering, that.weathering) && Objects.equals(
        metamorphism, that.metamorphism) && Objects.equals(storageMethod, that.storageMethod) && Objects.equals(province,
        that.province) && Objects.equals(age, that.age) && Objects.equals(imlgs, that.imlgs) && Objects.equals(cruiseId,
        that.cruiseId) && Objects.equals(cruiseYear, that.cruiseYear) && Objects.equals(platformId, that.platformId)
        && Objects.equals(facilityId, that.facilityId) && Objects.equals(intervalId, that.intervalId) && Objects.equals(
        leg, that.leg) && Objects.equals(aoi, that.aoi)
        && Objects.equals(order, that.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(repository, bbox, platform, lake, cruise, device, startDate, startDateBeginsWith, minDepth, maxDepth, igsn, lithology,
        rockLithology, texture, mineralogy, weathering, metamorphism, storageMethod, province, age, imlgs, cruiseId, cruiseYear, platformId,
        facilityId,
        intervalId, leg, aoi, page, itemsPerPage, order);
  }

  @Override
  public String toString() {
    return "GeosampleSearchParameterObject{" +
        "repository='" + repository + '\'' +
        ", bbox='" + bbox + '\'' +
        ", platform='" + platform + '\'' +
        ", lake='" + lake + '\'' +
        ", cruise='" + cruise + '\'' +
        ", device='" + device + '\'' +
        ", startDate='" + startDate + '\'' +
        ", startDateBeginsWith='" + startDateBeginsWith + '\'' +
        ", minDepth=" + minDepth +
        ", maxDepth=" + maxDepth +
        ", igsn='" + igsn + '\'' +
        ", lithology='" + lithology + '\'' +
        ", rockLithology='" + rockLithology + '\'' +
        ", texture='" + texture + '\'' +
        ", mineralogy='" + mineralogy + '\'' +
        ", weathering='" + weathering + '\'' +
        ", metamorphism='" + metamorphism + '\'' +
        ", storageMethod='" + storageMethod + '\'' +
        ", province='" + province + '\'' +
        ", age='" + age + '\'' +
        ", imlgs='" + imlgs + '\'' +
        ", cruiseId=" + cruiseId +
        ", cruiseYear=" + cruiseYear +
        ", platformId=" + platformId +
        ", facilityId=" + facilityId +
        ", intervalId=" + intervalId +
        ", leg='" + leg + '\'' +
        ", aoi=" + aoi +
        ", page=" + page +
        ", itemsPerPage=" + itemsPerPage +
        ", order=" + order +
        '}';
  }
}
