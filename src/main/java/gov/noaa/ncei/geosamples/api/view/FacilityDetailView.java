package gov.noaa.ncei.geosamples.api.view;

import java.util.Objects;

public class FacilityDetailView extends FacilityDisplayView {

  private String instCode;
  private String addr1;
  private String addr2;
  private String addr3;
  private String addr4;
  private String contact1;
  private String contact2;
  private String contact3;
  private String emailLink;
  private String urlLink;
  private String ftpLink;

  public String getInstCode() {
    return instCode;
  }

  public void setInstCode(String instCode) {
    this.instCode = instCode;
  }

  public String getAddr1() {
    return addr1;
  }

  public void setAddr1(String addr1) {
    this.addr1 = addr1;
  }

  public String getAddr2() {
    return addr2;
  }

  public void setAddr2(String addr2) {
    this.addr2 = addr2;
  }

  public String getAddr3() {
    return addr3;
  }

  public void setAddr3(String addr3) {
    this.addr3 = addr3;
  }

  public String getAddr4() {
    return addr4;
  }

  public void setAddr4(String addr4) {
    this.addr4 = addr4;
  }

  public String getContact1() {
    return contact1;
  }

  public void setContact1(String contact1) {
    this.contact1 = contact1;
  }

  public String getContact2() {
    return contact2;
  }

  public void setContact2(String contact2) {
    this.contact2 = contact2;
  }

  public String getContact3() {
    return contact3;
  }

  public void setContact3(String contact3) {
    this.contact3 = contact3;
  }

  public String getEmailLink() {
    return emailLink;
  }

  public void setEmailLink(String emailLink) {
    this.emailLink = emailLink;
  }

  public String getUrlLink() {
    return urlLink;
  }

  public void setUrlLink(String urlLink) {
    this.urlLink = urlLink;
  }

  public String getFtpLink() {
    return ftpLink;
  }

  public void setFtpLink(String ftpLink) {
    this.ftpLink = ftpLink;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    FacilityDetailView that = (FacilityDetailView) o;
    return Objects.equals(instCode, that.instCode) && Objects.equals(addr1, that.addr1) && Objects.equals(addr2,
        that.addr2) && Objects.equals(addr3, that.addr3) && Objects.equals(addr4, that.addr4) && Objects.equals(contact1,
        that.contact1) && Objects.equals(contact2, that.contact2) && Objects.equals(contact3, that.contact3)
        && Objects.equals(emailLink, that.emailLink) && Objects.equals(urlLink, that.urlLink) && Objects.equals(ftpLink,
        that.ftpLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), instCode, addr1, addr2, addr3, addr4, contact1, contact2, contact3, emailLink, urlLink, ftpLink);
  }
}
