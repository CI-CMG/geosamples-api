package gov.noaa.ncei.geosamples.api.view;

public interface PagingParameters {


  int getPage();

  void setPage(int page);

  int getItemsPerPage();

  void setItemsPerPage(int itemsPerPage);

}
