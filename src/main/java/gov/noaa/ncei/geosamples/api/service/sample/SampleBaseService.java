package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.error.ApiError;
import gov.noaa.ncei.geosamples.api.error.ApiException;
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SelectBuilder;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SortBuilder;
import gov.noaa.ncei.geosamples.api.service.SortDirection;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

abstract class SampleBaseService<V, D> {

  private final CuratorsSampleTsqpRepository curatorsSampleTsqpRepository;
  private final SampleSpecificationFactory specificationFactory;
  private final Class<D> dtoClass;

  protected SampleBaseService(Class<D> dtoClass, CuratorsSampleTsqpRepository curatorsSampleTsqpRepository,
      SampleSpecificationFactory specificationFactory) {
    this.dtoClass = dtoClass;
    this.curatorsSampleTsqpRepository = curatorsSampleTsqpRepository;
    this.specificationFactory = specificationFactory;
  }

  protected abstract V toView(CuratorsSampleTsqpEntity entity);

  protected abstract V toView(D dto);

  protected abstract Selection<?>[] dtoConstructorSelect(Root<CuratorsSampleTsqpEntity> r, CriteriaBuilder cb, Joiner<CuratorsSampleTsqpEntity> j);


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

  public enum SampleSortField {
    imlgs,
    platform,
    cruise,
    sample,
    device,
    beginDate,
    lat,
    lon,
    water_depth,
    storage_meth,
    igsn,
    leg,
    facility_code,
    ship_code,
    province,
    lake
  }

  private static final List<String> DEFAULT_SORT = Arrays.asList(
      "sample:asc",
      "platform:asc",
      "facility_code:asc",
      "cruise:asc",
      "leg:asc",
      "imlgs:asc"
  );

  public PagedItemsView<V> search(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;

    SortBuilder<CuratorsSampleTsqpEntity> sortBuilder = (r, cb, j) -> {
      HibernateCriteriaBuilder hcb = (HibernateCriteriaBuilder) cb;
      List<Order> orders = new ArrayList<>();
      List<String> definedSorts = new ArrayList<>(searchParameters.getOrder());
      if (definedSorts.isEmpty()) {
        definedSorts = new ArrayList<>(DEFAULT_SORT);
      }
      if (!definedSorts.contains("imlgs:asc") && !definedSorts.contains("imlgs:desc")) {
        definedSorts.add("imlgs:asc");
      }
      definedSorts.forEach(definedSort -> {
        String[] parts = definedSort.split(":");
        SampleSortField field = SampleSortField.valueOf(parts[0]);
        SortDirection direction = SortDirection.valueOf(parts[1].toLowerCase(Locale.ENGLISH));
        switch (field) {
          case imlgs:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(r.get(CuratorsSampleTsqpEntity_.IMLGS)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(r.get(CuratorsSampleTsqpEntity_.IMLGS)), false));
            }
            break;
          case platform:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(j.joinPlatform().get(PlatformMasterEntity_.PLATFORM)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(j.joinPlatform().get(PlatformMasterEntity_.PLATFORM)), false));
            }
            break;
          case cruise:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(j.joinCruise().get(CuratorsCruiseEntity_.CRUISE_NAME)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(j.joinCruise().get(CuratorsCruiseEntity_.CRUISE_NAME)), false));
            }
            break;
          case sample:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(r.get(CuratorsSampleTsqpEntity_.SAMPLE)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(r.get(CuratorsSampleTsqpEntity_.SAMPLE)), false));
            }
            break;
          case device:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(j.joinDevice().get(CuratorsDeviceEntity_.DEVICE)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(j.joinDevice().get(CuratorsDeviceEntity_.DEVICE)), false));
            }
            break;
          case beginDate:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(r.get(CuratorsSampleTsqpEntity_.BEGIN_DATE), false));
            } else {
              orders.add(hcb.desc(r.get(CuratorsSampleTsqpEntity_.BEGIN_DATE), false));
            }
            break;
          case lat:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(r.get(CuratorsSampleTsqpEntity_.LAT), false));
            } else {
              orders.add(hcb.desc(r.get(CuratorsSampleTsqpEntity_.LAT), false));
            }
            break;
          case lon:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(r.get(CuratorsSampleTsqpEntity_.LON), false));
            } else {
              orders.add(hcb.desc(r.get(CuratorsSampleTsqpEntity_.LON), false));
            }
            break;
          case water_depth:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(r.get(CuratorsSampleTsqpEntity_.WATER_DEPTH), false));
            } else {
              orders.add(hcb.desc(r.get(CuratorsSampleTsqpEntity_.WATER_DEPTH), false));
            }
            break;
          case storage_meth:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(j.joinStorageMethod().get(CuratorsStorageMethEntity_.STORAGE_METH)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(j.joinStorageMethod().get(CuratorsStorageMethEntity_.STORAGE_METH)), false));
            }
            break;
          case igsn:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(r.get(CuratorsSampleTsqpEntity_.IGSN)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(r.get(CuratorsSampleTsqpEntity_.IGSN)), false));
            }
            break;
          case leg:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(j.joinLeg().get(CuratorsLegEntity_.LEG_NAME)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(j.joinLeg().get(CuratorsLegEntity_.LEG_NAME)), false));
            }
            break;
          case facility_code:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(j.joinFacility().get(CuratorsFacilityEntity_.FACILITY_CODE)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(j.joinFacility().get(CuratorsFacilityEntity_.FACILITY_CODE)), false));
            }
            break;
          case ship_code:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(j.joinPlatform().get(PlatformMasterEntity_.ICES_CODE)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(j.joinPlatform().get(PlatformMasterEntity_.ICES_CODE)), false));
            }
            break;
          case province:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(j.joinProvince().get(CuratorsProvinceEntity_.PROVINCE)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(j.joinProvince().get(CuratorsProvinceEntity_.PROVINCE)), false));
            }
            break;
          case lake:
            if (direction == SortDirection.asc) {
              orders.add(hcb.asc(hcb.lower(r.get(CuratorsSampleTsqpEntity_.LAKE)), false));
            } else {
              orders.add(hcb.desc(hcb.lower(r.get(CuratorsSampleTsqpEntity_.LAKE)), false));
            }
            break;
          default:
            throw new IllegalStateException("Unsupported sort field: " + field);
        }
      });
      return orders;
    };

    SelectBuilder<CuratorsSampleTsqpEntity, D> selectBuilder = (r, cb, j) -> cb.construct(dtoClass, dtoConstructorSelect(r, cb, j));

    Page<D> dtoPage = curatorsSampleTsqpRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        dtoClass,
        selectBuilder,
        sortBuilder,
        specificationFactory);

    return new PagedItemsView.Builder<V>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(dtoPage.getTotalPages())
        .withPage(dtoPage.getNumber() + 1)
        .withTotalItems(dtoPage.getTotalElements())
        .withItems(dtoPage.toList().stream().map(this::toView).collect(Collectors.toList()))
        .build();
  }
}
