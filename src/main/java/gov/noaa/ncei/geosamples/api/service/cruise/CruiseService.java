package gov.noaa.ncei.geosamples.api.service.cruise;

import gov.noaa.ncei.geosamples.api.error.ApiError;
import gov.noaa.ncei.geosamples.api.error.ApiException;
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.CruiseLinkDetailView;
import gov.noaa.ncei.geosamples.api.view.CruiseNameView;
import gov.noaa.ncei.geosamples.api.view.CruiseView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import java.util.Arrays;
import java.util.stream.Collectors;
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


  public PagedItemsView<CruiseView> search(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    Page<CuratorsCruiseEntity> page = curatorsCruiseRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        CuratorsCruiseEntity.class,
        (r, cb, j) -> r,
        (r, cb, j) -> Arrays.asList(cb.asc(r.get(CuratorsCruiseEntity_.CRUISE_NAME)), cb.asc(r.get(CuratorsCruiseEntity_.YEAR))),
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
    Page<CuratorsCruiseEntity> page = curatorsCruiseRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        CuratorsCruiseEntity.class,
        (r, cb, j) -> r,
        (r, cb, j) -> Arrays.asList(cb.asc(r.get(CuratorsCruiseEntity_.CRUISE_NAME)), cb.asc(r.get(CuratorsCruiseEntity_.YEAR))),
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
