package gov.noaa.ncei.geosamples.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PagingIteratorTest {

  private static GeosampleSearchParameterObject createSearchParameters() {
    return new GeosampleSearchParameterObject(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );
  }

  private static final List<String> PAGE_1 = Arrays.asList("1", "2");
  private static final List<String> PAGE_2 = Arrays.asList("3", "4");
  private static final List<String> PAGE_3 = Arrays.asList("5");

  @Test
  public void testHappyPath() throws Exception {
    PagingIterator<String> iterator = new PagingIterator<>(createSearchParameters(), sp -> {
      List<String> page;
      switch (sp.getPage()) {
        case 1:
          page = PAGE_1;
          break;
        case 2:
          page = PAGE_2;
          break;
        case 3:
          page = PAGE_3;
          break;
        default:
          throw new IllegalStateException();
      }
      return new PagedItemsView.Builder<String>()
          .withItemsPerPage(2)
          .withTotalPages(3)
          .withPage(sp.getPage())
          .withTotalItems(5)
          .withItems(page)
          .build();
    });

    List<String> all = new ArrayList<>();
    while (iterator.hasNext()) {
      all.add(iterator.next());
    }
    assertEquals(Arrays.asList("1", "2", "3", "4", "5"), all);
  }

}