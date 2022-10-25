package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.error.ApiError;
import gov.noaa.ncei.geosamples.api.error.ApiException;
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

abstract class SampleBaseService<V> {

  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final SampleSpecificationFactory specificationFactory;

  protected SampleBaseService(CuratorsSampleTsqpRepository curatorsSampleTsqpRepository, SampleSpecificationFactory specificationFactory) {
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.specificationFactory = specificationFactory;
  }

  protected abstract V toView(CuratorsSampleTsqpEntity entity);


  public V load(String id) {
    return toView(curatorsSampleTsqpRepository.findById(normalizeId(id))
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error(HttpStatus.NOT_FOUND.getReasonPhrase()).build())));
  }

  public CountView count(GeosampleSearchParameterObject searchParameters) {
    return new CountView(curatorsSampleTsqpRepository.count(searchParameters, specificationFactory));
  }

  private static String normalizeId(String id) {
    return id.trim().toLowerCase(Locale.ENGLISH);
  }

  public PagedItemsView<V> search(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;

    Page<SampleDto> dtoPage = curatorsSampleTsqpRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        SampleDto.class,
        (r, cb, j) -> cb.construct(SampleDto.class,
            r.get(CuratorsSampleTsqpEntity_.IMLGS),
            r.get(CuratorsSampleTsqpEntity_.SAMPLE),
            j.joinCruise().get(CuratorsCruiseEntity_.CRUISE_NAME),
            j.joinPlatform().get(PlatformMasterEntity_.PLATFORM_NORMALIZED),
            j.joinLeg().get(CuratorsLegEntity_.LEG_NAME),
            j.joinFacility().get(CuratorsFacilityEntity_.FACILITY_CODE)),
        (r, cb, j) -> Arrays.asList(
            cb.asc(r.get(CuratorsSampleTsqpEntity_.SAMPLE)),
            cb.asc(j.joinPlatform().get(PlatformMasterEntity_.PLATFORM_NORMALIZED)),
            cb.asc(j.joinFacility().get(CuratorsFacilityEntity_.FACILITY_CODE)),
            cb.asc(j.joinCruise().get(CuratorsCruiseEntity_.CRUISE_NAME)),
            cb.asc(j.joinLeg().get(CuratorsLegEntity_.LEG_NAME))),
        specificationFactory);

    return new PagedItemsView.Builder<V>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(dtoPage.getTotalPages())
        .withPage(dtoPage.getNumber() + 1)
        .withTotalItems(dtoPage.getTotalElements())
        .withItems(curatorsSampleTsqpRepository.findAllById(dtoPage.stream().map(SampleDto::getImlgs).collect(Collectors.toList()))
            .stream().map(this::toView).collect(Collectors.toList()))
        .build();
  }
}
