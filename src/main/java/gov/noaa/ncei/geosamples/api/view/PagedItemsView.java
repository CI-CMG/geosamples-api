package gov.noaa.ncei.geosamples.api.view;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder = PagedItemsView.Builder.class)
public class PagedItemsView<T> {

  private final List<T> items;
  private final int page;
  private final int totalPages;
  private final long totalItems;
  private final int itemsPerPage;

  private PagedItemsView(List<T> items, int page, int totalPages, int itemsPerPage, long totalItems) {
    this.items = Collections.unmodifiableList(new ArrayList<>(items));
    this.page = page;
    this.totalPages = totalPages;
    this.itemsPerPage = itemsPerPage;
    this.totalItems = totalItems;
  }

  public List<T> getItems() {
    return items;
  }

  public int getPage() {
    return page;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public int getItemsPerPage() {
    return itemsPerPage;
  }

  public long getTotalItems() {
    return totalItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagedItemsView<?> view = (PagedItemsView<?>) o;
    return page == view.page && totalPages == view.totalPages && totalItems == view.totalItems && itemsPerPage == view.itemsPerPage && Objects
        .equals(items, view.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, page, totalPages, totalItems, itemsPerPage);
  }

  @Override
  public String toString() {
    return "PagedItemsView{" +
        "items=" + items +
        ", page=" + page +
        ", totalPages=" + totalPages +
        ", totalItems=" + totalItems +
        ", itemsPerPage=" + itemsPerPage +
        '}';
  }

  public static final class Builder<T> {
    private List<T> items = new ArrayList<>();
    private int page = 1;
    private int totalPages = -1;
    private int itemsPerPage = -1;
    private long totalItems = -1;

    public Builder<T> withTotalItems(long totalItems) {
      this.totalItems = totalItems;
      return this;
    }

    public Builder<T> withItems(List<T> items) {
      this.items.clear();
      this.items.addAll(items);
      return this;
    }

    public Builder<T> withItem(T item) {
      this.items.add(item);
      return this;
    }

    public Builder<T> withPage(int page) {
      this.page = page;
      return this;
    }

    public Builder<T> withTotalPages(int totalPages) {
      this.totalPages = totalPages;
      return this;
    }

    public Builder<T> withItemsPerPage(int maxItemsPerPage) {
      this.itemsPerPage = maxItemsPerPage;
      return this;
    }

    public PagedItemsView<T> build() {
      return new PagedItemsView<>(items, page, totalPages, itemsPerPage, totalItems);
    }
  }

}
