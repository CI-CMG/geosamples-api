package gov.noaa.ncei.geosamples.api.view;

import java.util.Comparator;
import java.util.Objects;

public class LinkView implements Comparable<LinkView> {

  private String link;
  private String linkLevel;
  private String source;
  private String type;

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getLinkLevel() {
    return linkLevel;
  }

  public void setLinkLevel(String linkLevel) {
    this.linkLevel = linkLevel;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public int compareTo(LinkView o) {
    return Comparator.nullsFirst(Comparator.comparing(LinkView::getType)
        .thenComparing(LinkView::getLink)
        .thenComparing(LinkView::getLinkLevel)
        .thenComparing(LinkView::getSource)
    ).compare(this, o);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkView view = (LinkView) o;
    return Objects.equals(link, view.link) && Objects.equals(linkLevel, view.linkLevel) && Objects.equals(source,
        view.source) && Objects.equals(type, view.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(link, linkLevel, source, type);
  }
}
