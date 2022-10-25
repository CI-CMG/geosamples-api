package gov.noaa.ncei.geosamples.api.service.sample;

import java.util.Objects;

/**
 * This class is needed because the {@link gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity} contains
 * a shape which prevents the "distinct" SQL query.  Also when doing a "distinct" query, sorting becomes an issue when
 * the columns being sorted are in a join table. The nature of the queries makes using the "distinct" keyword unavoidable.
 *
 */
public class SampleDto {

  private final String imlgs;
  private final String sample;
  private final String cruiseName;
  private final String normalizedPlatform;
  private final String leg;
  private final String facilityCode;

  public SampleDto(String imlgs, String sample, String cruiseName, String normalizedPlatform, String leg, String facilityCode) {
    this.imlgs = imlgs;
    this.sample = sample;
    this.cruiseName = cruiseName;
    this.normalizedPlatform = normalizedPlatform;
    this.leg = leg;
    this.facilityCode = facilityCode;
  }

  public String getImlgs() {
    return imlgs;
  }

  public String getSample() {
    return sample;
  }

  public String getCruiseName() {
    return cruiseName;
  }

  public String getNormalizedPlatform() {
    return normalizedPlatform;
  }

  public String getLeg() {
    return leg;
  }

  public String getFacilityCode() {
    return facilityCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SampleDto sampleDto = (SampleDto) o;
    return Objects.equals(imlgs, sampleDto.imlgs) && Objects.equals(sample, sampleDto.sample) && Objects.equals(
        cruiseName, sampleDto.cruiseName) && Objects.equals(normalizedPlatform, sampleDto.normalizedPlatform) && Objects.equals(leg,
        sampleDto.leg) && Objects.equals(facilityCode, sampleDto.facilityCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imlgs, sample, cruiseName, normalizedPlatform, leg, facilityCode);
  }

  @Override
  public String toString() {
    return "SampleDto{" +
        "imlgs='" + imlgs + '\'' +
        ", sample='" + sample + '\'' +
        ", cruiseName='" + cruiseName + '\'' +
        ", normalizedPlatform='" + normalizedPlatform + '\'' +
        ", leg='" + leg + '\'' +
        ", facilityCode='" + facilityCode + '\'' +
        '}';
  }
}
