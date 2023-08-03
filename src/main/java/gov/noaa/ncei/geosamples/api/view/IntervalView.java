package gov.noaa.ncei.geosamples.api.view;

import gov.noaa.ncei.geosamples.api.service.csv.CsvColumn;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class IntervalView implements Comparable<IntervalView> {


  private Long id;
  @CsvColumn(order = 0, column = "Repository")
  private String facilityCode;
  @CsvColumn(order = 1, column = "Ship/Platform")
  private String platform;
  @CsvColumn(order = 2, column = "Cruise ID")
  private String cruise;
  @CsvColumn(order = 3, column = "Sample ID")
  private String sample;
  @CsvColumn(order = 4, column = "Sampling Device")
  private String device;
  @CsvColumn(order = 5, column = "Interval/Subsample")
  private Integer interval;
  @CsvColumn(order = 6, column = "Depth to Top")
  private Integer depthTop;
  @CsvColumn(order = 7, column = "Depth to Bottom")
  private Integer depthBot;
  @CsvColumn(order = 8, column = "Primary Lithologic Composition")
  private String lith1;
  @CsvColumn(order = 10, column = "Secondary Lithologic Composition")
  private String lith2;
  @CsvColumn(order = 9, column = "Primary Texture")
  private String text1;
  @CsvColumn(order = 11, column = "Secondary Texture")
  private String text2;
  @CsvColumn(order = 12, column = "Other Component 1")
  private String comp1;
  @CsvColumn(order = 13, column = "Other Component 2")
  private String comp2;
  @CsvColumn(order = 14, column = "Other Component 3")
  private String comp3;
  @CsvColumn(order = 15, column = "Other Component 4")
  private String comp4;
  @CsvColumn(order = 16, column = "Other Component 5")
  private String comp5;
  @CsvColumn(order = 17, column = "Other Component 6")
  private String comp6;
  @CsvColumn(order = 18, column = "Description")
  private String description;
  @CsvColumn(order = 19, column = "Geologic Age")
  private List<String> ages = new ArrayList<>(0);
  @CsvColumn(order = 20, column = "Absolute Age, Top")
  private String absoluteAgeTop;
  @CsvColumn(order = 21, column = "Absolute Age, Bottom")
  private String absoluteAgeBot;
  @CsvColumn(order = 22, column = "Bulk Weight (kg)")
  private Double weight;
  @CsvColumn(order = 23, column = "Rock Lithology")
  private String rockLith;
  @CsvColumn(order = 24, column = "Rock Mineralogy")
  private String rockMin;
  @CsvColumn(order = 25, column = "Rock Weathering and Metamorphism")
  private String weathMeta;
  @CsvColumn(order = 26, column = "Rock Glass Remarks and MN/Fe Oxide Coating")
  private String remark;
  @CsvColumn(order = 27, column = "Color (Munsell Code)")
  private String munsellCode;
  @CsvColumn(order = 28, column = "Exhausted")
  private String exhaustCode;

  private String photoLink;
  @CsvColumn(order = 29, column = "Lake")
  private String lake;
  @CsvColumn(order = 30, column = "Comments")
  private String intComments;
  @CsvColumn(order = 31, column = "Interval/Subsample IGSN")
  private String igsn;
  @CsvColumn(order = 32, column = "IMLGS Number")
  private String imlgs;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFacilityCode() {
    return facilityCode;
  }

  public void setFacilityCode(String facilityCode) {
    this.facilityCode = facilityCode;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getCruise() {
    return cruise;
  }

  public void setCruise(String cruise) {
    this.cruise = cruise;
  }

  public String getSample() {
    return sample;
  }

  public void setSample(String sample) {
    this.sample = sample;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  public Integer getDepthTop() {
    return depthTop;
  }

  public void setDepthTop(Integer depthTop) {
    this.depthTop = depthTop;
  }

  public Integer getDepthBot() {
    return depthBot;
  }

  public void setDepthBot(Integer depthBot) {
    this.depthBot = depthBot;
  }

  public String getLith1() {
    return lith1;
  }

  public void setLith1(String lith1) {
    this.lith1 = lith1;
  }

  public String getLith2() {
    return lith2;
  }

  public void setLith2(String lith2) {
    this.lith2 = lith2;
  }

  public String getText1() {
    return text1;
  }

  public void setText1(String text1) {
    this.text1 = text1;
  }

  public String getText2() {
    return text2;
  }

  public void setText2(String text2) {
    this.text2 = text2;
  }

  public String getComp1() {
    return comp1;
  }

  public void setComp1(String comp1) {
    this.comp1 = comp1;
  }

  public String getComp2() {
    return comp2;
  }

  public void setComp2(String comp2) {
    this.comp2 = comp2;
  }

  public String getComp3() {
    return comp3;
  }

  public void setComp3(String comp3) {
    this.comp3 = comp3;
  }

  public String getComp4() {
    return comp4;
  }

  public void setComp4(String comp4) {
    this.comp4 = comp4;
  }

  public String getComp5() {
    return comp5;
  }

  public void setComp5(String comp5) {
    this.comp5 = comp5;
  }

  public String getComp6() {
    return comp6;
  }

  public void setComp6(String comp6) {
    this.comp6 = comp6;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getAges() {
    return ages;
  }

  public void setAges(List<String> ages) {
    if (ages == null) {
      ages = new ArrayList<>(0);
    }
    this.ages = ages;
  }

  public String getAbsoluteAgeTop() {
    return absoluteAgeTop;
  }

  public void setAbsoluteAgeTop(String absoluteAgeTop) {
    this.absoluteAgeTop = absoluteAgeTop;
  }

  public String getAbsoluteAgeBot() {
    return absoluteAgeBot;
  }

  public void setAbsoluteAgeBot(String absoluteAgeBot) {
    this.absoluteAgeBot = absoluteAgeBot;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public String getRockLith() {
    return rockLith;
  }

  public void setRockLith(String rockLith) {
    this.rockLith = rockLith;
  }

  public String getRockMin() {
    return rockMin;
  }

  public void setRockMin(String rockMin) {
    this.rockMin = rockMin;
  }

  public String getWeathMeta() {
    return weathMeta;
  }

  public void setWeathMeta(String weathMeta) {
    this.weathMeta = weathMeta;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getMunsellCode() {
    return munsellCode;
  }

  public void setMunsellCode(String munsellCode) {
    this.munsellCode = munsellCode;
  }

  public String getExhaustCode() {
    return exhaustCode;
  }

  public void setExhaustCode(String exhaustCode) {
    this.exhaustCode = exhaustCode;
  }

  public String getPhotoLink() {
    return photoLink;
  }

  public void setPhotoLink(String photoLink) {
    this.photoLink = photoLink;
  }

  public String getLake() {
    return lake;
  }

  public void setLake(String lake) {
    this.lake = lake;
  }

  public String getIntComments() {
    return intComments;
  }

  public void setIntComments(String intComments) {
    this.intComments = intComments;
  }

  public String getIgsn() {
    return igsn;
  }

  public void setIgsn(String igsn) {
    this.igsn = igsn;
  }

  public String getImlgs() {
    return imlgs;
  }

  public void setImlgs(String imlgs) {
    this.imlgs = imlgs;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IntervalView view = (IntervalView) o;
    return Objects.equals(facilityCode, view.facilityCode) && Objects.equals(platform, view.platform) && Objects.equals(
        cruise, view.cruise) && Objects.equals(sample, view.sample) && Objects.equals(device, view.device)
        && Objects.equals(interval, view.interval) && Objects.equals(depthTop, view.depthTop) && Objects.equals(depthBot,
        view.depthBot) && Objects.equals(lith1, view.lith1) && Objects.equals(lith2, view.lith2) && Objects.equals(text1,
        view.text1) && Objects.equals(text2, view.text2) && Objects.equals(comp1, view.comp1) && Objects.equals(comp2,
        view.comp2) && Objects.equals(comp3, view.comp3) && Objects.equals(comp4, view.comp4) && Objects.equals(comp5,
        view.comp5) && Objects.equals(comp6, view.comp6) && Objects.equals(description, view.description)
        && Objects.equals(ages, view.ages) && Objects.equals(absoluteAgeTop, view.absoluteAgeTop) && Objects.equals(
        absoluteAgeBot, view.absoluteAgeBot) && Objects.equals(weight, view.weight) && Objects.equals(rockLith, view.rockLith)
        && Objects.equals(rockMin, view.rockMin) && Objects.equals(weathMeta, view.weathMeta) && Objects.equals(remark,
        view.remark) && Objects.equals(munsellCode, view.munsellCode) && Objects.equals(exhaustCode, view.exhaustCode)
        && Objects.equals(photoLink, view.photoLink) && Objects.equals(lake, view.lake) && Objects.equals(intComments,
        view.intComments) && Objects.equals(igsn, view.igsn) && Objects.equals(imlgs, view.imlgs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(facilityCode, platform, cruise, sample, device, interval, depthTop, depthBot, lith1, lith2, text1, text2, comp1, comp2, comp3,
        comp4, comp5, comp6, description, ages, absoluteAgeTop, absoluteAgeBot, weight, rockLith, rockMin, weathMeta, remark, munsellCode, exhaustCode,
        photoLink, lake, intComments, igsn, imlgs);
  }

  @Override
  public int compareTo(IntervalView o) {
    return Comparator.nullsFirst(Comparator.comparing(IntervalView::getImlgs)
        .thenComparing(IntervalView::getInterval)
    ).compare(this, o);
  }

  @Override
  public String toString() {
    return "IntervalView{" +
        "id=" + id +
        ", facilityCode='" + facilityCode + '\'' +
        ", platform='" + platform + '\'' +
        ", cruise='" + cruise + '\'' +
        ", sample='" + sample + '\'' +
        ", device='" + device + '\'' +
        ", interval=" + interval +
        ", depthTop=" + depthTop +
        ", depthBot=" + depthBot +
        ", lith1='" + lith1 + '\'' +
        ", lith2='" + lith2 + '\'' +
        ", text1='" + text1 + '\'' +
        ", text2='" + text2 + '\'' +
        ", comp1='" + comp1 + '\'' +
        ", comp2='" + comp2 + '\'' +
        ", comp3='" + comp3 + '\'' +
        ", comp4='" + comp4 + '\'' +
        ", comp5='" + comp5 + '\'' +
        ", comp6='" + comp6 + '\'' +
        ", description='" + description + '\'' +
        ", ages='" + ages + '\'' +
        ", absoluteAgeTop='" + absoluteAgeTop + '\'' +
        ", absoluteAgeBot='" + absoluteAgeBot + '\'' +
        ", weight=" + weight +
        ", rockLith='" + rockLith + '\'' +
        ", rockMin='" + rockMin + '\'' +
        ", weathMeta='" + weathMeta + '\'' +
        ", remark='" + remark + '\'' +
        ", munsellCode='" + munsellCode + '\'' +
        ", exhaustCode='" + exhaustCode + '\'' +
        ", photoLink='" + photoLink + '\'' +
        ", lake='" + lake + '\'' +
        ", intComments='" + intComments + '\'' +
        ", igsn='" + igsn + '\'' +
        ", imlgs='" + imlgs + '\'' +
        '}';
  }
}
