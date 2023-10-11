package gov.noaa.ncei.geosamples.api.view;

import java.util.Comparator;
import java.util.Objects;

public class PlatformNameView implements CsvColumnObject, Comparable<PlatformNameView> {

  private Long id;
  private String platform;

  public PlatformNameView() {

  }

  public PlatformNameView(Long id, String platform) {
    this.id = id;
    this.platform = platform;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPlatform() {
    return platform;
  }

  public PlatformNameView setPlatform(String platform) {
    this.platform = platform;
    return this;
  }

  @Override
  public Object asCsvColumn() {
    return platform;
  }

  @Override
  public int compareTo(PlatformNameView o) {
    return Comparator.nullsLast(Comparator.comparing(PlatformNameView::getPlatform, String::compareToIgnoreCase)).compare(this, o);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlatformNameView view = (PlatformNameView) o;
    return Objects.equals(id, view.id) && Objects.equals(platform, view.platform);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, platform);
  }
}
