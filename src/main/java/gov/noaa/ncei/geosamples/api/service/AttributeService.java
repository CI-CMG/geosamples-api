package gov.noaa.ncei.geosamples.api.service;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CustomRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public abstract class AttributeService<E> {

  protected final String attributeColumn;
  protected final CustomRepository<E> repository;
  protected final SpecificationFactory<E> specificationFactory;

  protected AttributeService(String attributeColumn,
      CustomRepository<E> repository,
      SpecificationFactory<E> specificationFactory) {
    this.attributeColumn = attributeColumn;
    this.repository = repository;
    this.specificationFactory = specificationFactory;
  }

  protected SpecExtender<E> getAdditionalSpecifications() {
    return null;
  }

  protected String mapItem(String item) {
    return item;
  }

  public PagedItemsView<String> search(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;

    Page<String> page = repository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        String.class,
        (r, cb, j) -> r.get(attributeColumn),
        (r, cb, j) -> Collections.singletonList(cb.asc(r.get(attributeColumn))),
        specificationFactory,
        getAdditionalSpecifications());

    return new PagedItemsView.Builder<String>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.toList().stream().map(this::mapItem).collect(Collectors.toList()))
        .build();
  }
}
