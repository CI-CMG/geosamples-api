package gov.noaa.ncei.geosamples.api.model;

import gov.noaa.ncei.geosamples.api.error.GeosamplesBadRequestException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

// This has to be a Java class, not Groovy, for the Swagger documentation to work
public class GeosampleSearchParameterObject {

  private String repository;
  private String bbox;
  @Hidden
  private Double xmin;
  @Hidden
  private Double ymin;
  @Hidden
  private Double xmax;
  @Hidden
  private Double ymax;
  private String platform;
  private String lake;
  private String cruise;
  private String device;
  @Parameter(name = "start_date")
  @Size(min = 4, max = 8)
  private String startDate;
  //    Date startDate
  @Parameter(name = "min_depth")
  @Min(0L)
  private Integer minDepth;
  @Parameter(name = "max_depth")
  private Integer maxDepth;
  private String igsn;
  private String lithology;
  private String texture;
  private String mineralogy;
  private String weathering;
  private String metamorphism;
  @Parameter(name = "storage_method")
  private String storageMethod;
  private String province;
  private String age;
  @Size(min = 12, max = 12)
  private String imlgs;

  // paging controls
  @Min(0L)
  private Integer offset = 0;

  @Parameter(name = "page_size")
  //TODO allow requests w/o limits? e.g. populate table which pages client-side?
  @Min(1L) @Max(5000L)
  private Integer pageSize = 500;

  public String getRepository() {
    return repository;
  }

  public void setRepository(String repository) {
    this.repository = repository;
  }

  public String getBbox() {
    return bbox;
  }

  public Double getXmin() {
    return xmin;
  }

  public void setXmin(Double xmin) {
    this.xmin = xmin;
  }

  public Double getYmin() {
    return ymin;
  }

  public void setYmin(Double ymin) {
    this.ymin = ymin;
  }

  public Double getXmax() {
    return xmax;
  }

  public void setXmax(Double xmax) {
    this.xmax = xmax;
  }

  public Double getYmax() {
    return ymax;
  }

  public void setYmax(Double ymax) {
    this.ymax = ymax;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getLake() {
    return lake;
  }

  public void setLake(String lake) {
    this.lake = lake;
  }

  public String getCruise() {
    return cruise;
  }

  public void setCruise(String cruise) {
    this.cruise = cruise;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStart_date(String startDate) {
    this.startDate = startDate;
  }

  public Integer getMinDepth() {
    return minDepth;
  }

  public void setMin_depth(Integer minDepth) {
    this.minDepth = minDepth;
  }

  public Integer getMaxDepth() {
    return maxDepth;
  }

  public void setMax_depth(Integer maxDepth) {
    this.maxDepth = maxDepth;
  }

  public String getIgsn() {
    return igsn;
  }

  public void setIgsn(String igsn) {
    this.igsn = igsn;
  }

  public String getLithology() {
    return lithology;
  }

  public void setLithology(String lithology) {
    this.lithology = lithology;
  }

  public String getTexture() {
    return texture;
  }

  public void setTexture(String texture) {
    this.texture = texture;
  }

  public String getMineralogy() {
    return mineralogy;
  }

  public void setMineralogy(String mineralogy) {
    this.mineralogy = mineralogy;
  }

  public String getWeathering() {
    return weathering;
  }

  public void setWeathering(String weathering) {
    this.weathering = weathering;
  }

  public String getMetamorphism() {
    return metamorphism;
  }

  public void setMetamorphism(String metamorphism) {
    this.metamorphism = metamorphism;
  }

  public String getStorageMethod() {
    return storageMethod;
  }

  public void setStorage_method(String storageMethod) {
    this.storageMethod = storageMethod;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPage_size(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /*
   * custom setters.  Type conversion exceptions handled as HTTP status 400.
   */
  // TODO build custom validation annotation for geographic coordinates
  // format: minx,miny,maxx,maxy
  public void setBbox(String bboxString) {
    List<Double> coords = Arrays.asList(bboxString.split(",")).stream().map(String::trim).map(Double::parseDouble).collect(Collectors.toList());
    if (coords.get(0) < -180 || coords.get(1) < -90 || coords.get(2) > 180 || coords.get(3) > 90
        || coords.get(0) >= coords.get(2) || coords.get(1) >= coords.get(3)) {
      throw new GeosamplesBadRequestException("invalid geographic coordinates provided for bbox");
    }
    // ensure there are no spaces in the provided value
    bbox = String.join(",", coords.stream().map(Object::toString).collect(Collectors.toList()));
    xmin = coords.get(0);
    ymin = coords.get(1);
    xmax = coords.get(2);
    ymax = coords.get(3);
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
    return Objects.equals(repository, that.repository) && Objects.equals(bbox, that.bbox) && Objects.equals(xmin,
        that.xmin) && Objects.equals(ymin, that.ymin) && Objects.equals(xmax, that.xmax) && Objects.equals(ymax,
        that.ymax) && Objects.equals(platform, that.platform) && Objects.equals(lake, that.lake) && Objects.equals(cruise,
        that.cruise) && Objects.equals(device, that.device) && Objects.equals(startDate, that.startDate)
        && Objects.equals(minDepth, that.minDepth) && Objects.equals(maxDepth, that.maxDepth) && Objects.equals(igsn,
        that.igsn) && Objects.equals(lithology, that.lithology) && Objects.equals(texture, that.texture)
        && Objects.equals(mineralogy, that.mineralogy) && Objects.equals(weathering, that.weathering) && Objects.equals(
        metamorphism, that.metamorphism) && Objects.equals(storageMethod, that.storageMethod) && Objects.equals(province,
        that.province) && Objects.equals(age, that.age) && Objects.equals(imlgs, that.imlgs) && Objects.equals(offset,
        that.offset) && Objects.equals(pageSize, that.pageSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(repository, bbox, xmin, ymin, xmax, ymax, platform, lake, cruise, device, startDate, minDepth, maxDepth, igsn, lithology,
        texture, mineralogy, weathering, metamorphism, storageMethod, province, age, imlgs, offset, pageSize);
  }

  @Override
  public String toString() {
    return "GeosampleSearchParameterObject{" +
        "repository='" + repository + '\'' +
        ", bbox='" + bbox + '\'' +
        ", xmin=" + xmin +
        ", ymin=" + ymin +
        ", xmax=" + xmax +
        ", ymax=" + ymax +
        ", platform='" + platform + '\'' +
        ", lake='" + lake + '\'' +
        ", cruise='" + cruise + '\'' +
        ", device='" + device + '\'' +
        ", startDate='" + startDate + '\'' +
        ", minDepth=" + minDepth +
        ", maxDepth=" + maxDepth +
        ", igsn='" + igsn + '\'' +
        ", lithology='" + lithology + '\'' +
        ", texture='" + texture + '\'' +
        ", mineralogy='" + mineralogy + '\'' +
        ", weathering='" + weathering + '\'' +
        ", metamorphism='" + metamorphism + '\'' +
        ", storageMethod='" + storageMethod + '\'' +
        ", province='" + province + '\'' +
        ", age='" + age + '\'' +
        ", imlgs='" + imlgs + '\'' +
        ", offset=" + offset +
        ", pageSize=" + pageSize +
        '}';
  }
}
