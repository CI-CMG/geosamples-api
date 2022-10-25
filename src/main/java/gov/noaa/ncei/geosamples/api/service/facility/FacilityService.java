package gov.noaa.ncei.geosamples.api.service.facility;

import gov.noaa.ncei.geosamples.api.error.ApiError;
import gov.noaa.ncei.geosamples.api.error.ApiException;
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.FacilityDetailView;
import gov.noaa.ncei.geosamples.api.view.FacilityDisplayView;
import gov.noaa.ncei.geosamples.api.view.FacilityNameView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FacilityService {

  private final CuratorsFacilityRepository curatorsFacilityRepository;
  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final FacilitySpecificationFactory facilitySpecificationFactory;

  @Autowired
  public FacilityService(CuratorsFacilityRepository curatorsFacilityRepository,
      CuratorsSampleTsqpRepository curatorsSampleTsqpRepository,
      FacilitySpecificationFactory facilitySpecificationFactory) {
    this.curatorsFacilityRepository = curatorsFacilityRepository;
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.facilitySpecificationFactory = facilitySpecificationFactory;
  }

  private Integer countSamples(Long id) {
    return curatorsSampleTsqpRepository.countFacilitySamples(id);
  }

  public FacilityDetailView load(Long id) {
    return ViewTransformers.toFacilityDetailView(
        curatorsFacilityRepository.findById(id)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error(HttpStatus.NOT_FOUND.getReasonPhrase()).build())),
        countSamples(id));
  }

  public CountView count(GeosampleSearchParameterObject searchParameters) {
    return new CountView(curatorsFacilityRepository.count(searchParameters, facilitySpecificationFactory));
  }

  private FacilityDisplayView toView(CuratorsFacilityEntity entity) {
    return ViewTransformers.toFacilityDisplayView(entity, countSamples(entity.getId()));
  }

  public PagedItemsView<FacilityDisplayView> search(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    Page<CuratorsFacilityEntity> page = curatorsFacilityRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        CuratorsFacilityEntity.class,
        (r, cb, j) -> r,
        (r, cb, j) -> Collections.singletonList(cb.asc(r.get(CuratorsFacilityEntity_.FACILITY_CODE))),
        facilitySpecificationFactory);

    return new PagedItemsView.Builder<FacilityDisplayView>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.get().map(this::toView).collect(Collectors.toList()))
        .build();
  }

  public PagedItemsView<FacilityNameView> names(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    Page<CuratorsFacilityEntity> page = curatorsFacilityRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        CuratorsFacilityEntity.class,
        (r, cb, j) -> r,
        (r, cb, j) -> Collections.singletonList(cb.asc(r.get(CuratorsFacilityEntity_.FACILITY_CODE))),
        facilitySpecificationFactory);

    return new PagedItemsView.Builder<FacilityNameView>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.get().map(ViewTransformers::toFacilityNameView).collect(Collectors.toList()))
        .build();
  }


}
