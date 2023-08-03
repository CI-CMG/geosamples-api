package gov.noaa.ncei.geosamples.api.service;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class PagingIterator<V> implements Iterator<V> {

  private final GeosampleSearchParameterObject searchParameters;
  private final Function<GeosampleSearchParameterObject, PagedItemsView<V>> search;

  private int currentPage = 0;
  private int lastPage = 0;
  private LinkedList<V> items = new LinkedList<>();

  public PagingIterator(GeosampleSearchParameterObject searchParameters,
      Function<GeosampleSearchParameterObject, PagedItemsView<V>> search) {
    this.searchParameters = searchParameters;
    this.search = search;
  }

  private void nextPage() {
    currentPage++;
    searchParameters.setPage(currentPage);
    PagedItemsView<V> page = search.apply(searchParameters);
    lastPage = page.getTotalPages();
    items = new LinkedList<>(page.getItems());
  }

  @Override
  public boolean hasNext() {
    if (items.isEmpty() && (lastPage == 0 || currentPage < lastPage)) {
      nextPage();
    }
    return !items.isEmpty();
  }

  @Override
  public V next() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more items");
    }
    return items.pop();
  }
}
