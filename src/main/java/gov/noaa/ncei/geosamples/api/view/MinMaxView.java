package gov.noaa.ncei.geosamples.api.view;

public class MinMaxView {
    private final Integer min;
    private final Integer max;

  public MinMaxView(Integer min, Integer max) {
    this.min = min;
    this.max = max;
  }

  public Integer getMin() {
    return min;
  }

  public Integer getMax() {
    return max;
  }

}
