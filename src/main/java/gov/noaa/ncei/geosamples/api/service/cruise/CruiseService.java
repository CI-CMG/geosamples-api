package gov.noaa.ncei.geosamples.api.service.cruise;

import gov.noaa.ncei.geosamples.api.error.ApiError;
import gov.noaa.ncei.geosamples.api.error.ApiException;
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SortBuilder;
import gov.noaa.ncei.geosamples.api.service.SortDirection;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.CruiseLinkDetailView;
import gov.noaa.ncei.geosamples.api.view.CruiseNameView;
import gov.noaa.ncei.geosamples.api.view.CruiseView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.persistence.criteria.Order;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CruiseService {


  private final CuratorsCruiseRepository curatorsCruiseRepository;
  private final CruiseSpecificationFactory specificationFactory;

  @Autowired
  public CruiseService(CuratorsCruiseRepository curatorsCruiseRepository,
      CruiseSpecificationFactory specificationFactory) {
    this.curatorsCruiseRepository = curatorsCruiseRepository;
    this.specificationFactory = specificationFactory;
  }

  public CruiseLinkDetailView load(Long id) {
    return ViewTransformers.toCruiseLinkDetailView(
        curatorsCruiseRepository.findById(id)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error(HttpStatus.NOT_FOUND.getReasonPhrase()).build())));
  }

  public CountView count(GeosampleSearchParameterObject searchParameters) {
    return new CountView(curatorsCruiseRepository.count(searchParameters, specificationFactory));
  }

  public enum CruiseSortField {
    id,
    cruise,
    year
  }

  private static final List<String> DEFAULT_SORT = Arrays.asList(
      "cruise:asc",
      "year:asc",
      "id:asc"
  );

  private static SortBuilder<CuratorsCruiseEntity> sortBuilder(GeosampleSearchParameterObject searchParameters) {
    return  (r, cb, j) -> {
      HibernateCriteriaBuilder hcb = (HibernateCriteriaBuilder) cb;
      List<Order> orders = new ArrayList<>();
      List<String> definedSorts = new ArrayList<>(searchParameters.getOrder());
      if (definedSorts.isEmpty()) {
        definedSorts = new ArrayList<>(DEFAULT_SORT);
      }
      if (!definedSorts.contains("id:asc") && !definedSorts.contains("id:desc")) {
        definedSorts.add("id:asc");
      }
      definedSorts.forEach(definedSort -> {
        String[] parts = definedSort.split(":");
        CruiseSortField field = CruiseSortField.valueOf(parts[0]);
        SortDirection direction = SortDirection.valueOf(parts[1].toLowerCase(Locale.ENGLISH));
        switch (field) {
          case id:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(r.get(CuratorsCruiseEntity_.ID), false));
            } else {
              orders.add(hcb.desc(r.get(CuratorsCruiseEntity_.ID), false));
            }
            break;
          case cruise:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(r.get(CuratorsCruiseEntity_.CRUISE_NAME)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(r.get(CuratorsCruiseEntity_.CRUISE_NAME)), false));
            }
            break;
          case year:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(r.get(CuratorsCruiseEntity_.YEAR), false));
            } else {
              orders.add(hcb.desc(r.get(CuratorsCruiseEntity_.YEAR), false));
            }
            break;
          default:
            throw new IllegalStateException("Unsupported sort field: " + field);
        }
      });
      return orders;
    };
  }

  public PagedItemsView<CruiseView> search(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;

    SortBuilder<CuratorsCruiseEntity> sortBuilder = sortBuilder(searchParameters);

    Page<CuratorsCruiseEntity> page = curatorsCruiseRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        CuratorsCruiseEntity.class,
        (r, cb, j) -> r,
        sortBuilder,
        specificationFactory);

    return new PagedItemsView.Builder<CruiseView>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.get().map(ViewTransformers::toCruiseView).collect(Collectors.toList()))
        .build();
  }

  public PagedItemsView<CruiseNameView> names(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;

    SortBuilder<CuratorsCruiseEntity> sortBuilder = sortBuilder(searchParameters);

    Page<CuratorsCruiseEntity> page = curatorsCruiseRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        CuratorsCruiseEntity.class,
        (r, cb, j) -> r,
        sortBuilder,
        specificationFactory);

    return new PagedItemsView.Builder<CruiseNameView>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.get().map(ViewTransformers::toCruiseNameView).collect(Collectors.toList()))
        .build();
  }


}
