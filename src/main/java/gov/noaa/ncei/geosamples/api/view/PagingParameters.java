package gov.noaa.ncei.geosamples.api.view;

import java.util.List;

public interface PagingParameters {


  int getPage();

  void setPage(int page);

  int getItemsPerPage();

  void setItemsPerPage(int itemsPerPage);

  List<String> getOrder();

  void setOrder(List<String> sort);

}
