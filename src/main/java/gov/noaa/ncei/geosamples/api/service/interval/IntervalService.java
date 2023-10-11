package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.error.ApiError;
import gov.noaa.ncei.geosamples.api.error.ApiException;
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsLithologyRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsTextureRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.PagingIterator;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.service.csv.CsvExportHandler;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.IntervalView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class IntervalService {


  private final CuratorsIntervalRepository curatorsIntervalRepository;
  private final IntervalSpecificationFactory specificationFactory;
  private final CsvExportHandler<IntervalView> csvExportHandler;
  private final GeologicAgesAttributeService geologicAgesAttributeService;
  private final MetamorphismAttributeService metamorphismAttributeService;
  private final WeatheringAttributeService weatheringAttributeService;
  private final MineralogiesAttributeService mineralogiesAttributeService;
  private final CuratorsTextureRepository curatorsTextureRepository;
  private final CuratorsLithologyRepository curatorsLithologyRepository;
  private final RockLithAttributeService rockLithAttributeService;

  @Autowired
  public IntervalService(CuratorsIntervalRepository curatorsIntervalRepository,
      IntervalSpecificationFactory specificationFactory,
      GeologicAgesAttributeService geologicAgesAttributeService,
      MetamorphismAttributeService metamorphismAttributeService,
      WeatheringAttributeService weatheringAttributeService,
      MineralogiesAttributeService mineralogiesAttributeService,
      CuratorsTextureRepository curatorsTextureRepository,
      CuratorsLithologyRepository curatorsLithologyRepository,
      RockLithAttributeService rockLithAttributeService) {
    this.curatorsIntervalRepository = curatorsIntervalRepository;
    this.specificationFactory = specificationFactory;
    this.geologicAgesAttributeService = geologicAgesAttributeService;
    this.metamorphismAttributeService = metamorphismAttributeService;
    this.weatheringAttributeService = weatheringAttributeService;
    this.mineralogiesAttributeService = mineralogiesAttributeService;
    this.curatorsTextureRepository = curatorsTextureRepository;
    this.curatorsLithologyRepository = curatorsLithologyRepository;
    this.rockLithAttributeService = rockLithAttributeService;
    this.csvExportHandler = new CsvExportHandler<>(IntervalView.class, IntervalView.class);
  }

  private static IntervalView toView(CuratorsIntervalEntity entity) {
    SampleDisplayView sample = ViewTransformers.toSampleDisplayView(entity.getSample());
    return ViewTransformers.toIntervalView(entity, sample);
  }

  public IntervalView load(Long id) {
    return toView(curatorsIntervalRepository.findById(id)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ApiError.builder().error(HttpStatus.NOT_FOUND.getReasonPhrase()).build())));
  }

  public CountView count(GeosampleSearchParameterObject searchParameters) {
    return new CountView(curatorsIntervalRepository.count(searchParameters, specificationFactory));
  }

  public PagedItemsView<IntervalView> search(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    Page<CuratorsIntervalEntity> page = curatorsIntervalRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        CuratorsIntervalEntity.class,
        (r, cb, j) -> r,
        (r, cb, j) -> Arrays.asList(cb.asc(r.get(CuratorsIntervalEntity_.SAMPLE)), cb.asc(r.get(CuratorsIntervalEntity_.INTERVAL))),
        specificationFactory);

    return new PagedItemsView.Builder<IntervalView>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.get().map(IntervalService::toView).collect(Collectors.toList()))
        .build();
  }

  public void exportCsv(OutputStream outputStream, GeosampleSearchParameterObject searchParameters) {
    searchParameters.setItemsPerPage(1000);
    csvExportHandler.export(outputStream, new PagingIterator<>(searchParameters, this::search));
  }

  public PagedItemsView<String> searchAges(GeosampleSearchParameterObject searchParameters) {
    return geologicAgesAttributeService.search(searchParameters);
  }

  public PagedItemsView<String> searchMetamorphism(GeosampleSearchParameterObject searchParameters) {
    return metamorphismAttributeService.search(searchParameters);
  }

  public PagedItemsView<String> searchWeathering(GeosampleSearchParameterObject searchParameters) {
    return weatheringAttributeService.search(searchParameters);
  }

  public PagedItemsView<String> searchMineralogies(GeosampleSearchParameterObject searchParameters) {
    return mineralogiesAttributeService.search(searchParameters);
  }

  private static final SpecificationFactory<CuratorsTextureEntity> text1SpecFactory = new SpecificationFactory<CuratorsTextureEntity>() {

    @Override
    public Joiner<CuratorsTextureEntity> getJoins(Root<CuratorsTextureEntity> r) {
      return new Joiner<CuratorsTextureEntity>() {

        private From<CuratorsTextureEntity, CuratorsIntervalEntity> interval = null;
        private From<CuratorsTextureEntity, CuratorsSampleTsqpEntity> sample = null;
        private From<CuratorsTextureEntity, CuratorsCruiseEntity> cruise = null;
        private From<CuratorsTextureEntity, CuratorsLegEntity> leg = null;
        private From<CuratorsTextureEntity, PlatformMasterEntity> platform = null;
        private From<CuratorsTextureEntity, CuratorsFacilityEntity> facility = null;
        private From<CuratorsTextureEntity, CuratorsDeviceEntity> device = null;
        private From<CuratorsTextureEntity, CuratorsStorageMethEntity> storageMethod = null;
        private From<CuratorsTextureEntity, CuratorsProvinceEntity> province = null;

        @Override
        public From<CuratorsTextureEntity, CuratorsSampleTsqpEntity> joinSample() {
          if (sample == null) {
            sample = joinInterval().join(CuratorsIntervalEntity_.SAMPLE, JoinType.LEFT);
          }
          return sample;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsIntervalEntity> joinInterval() {
          if (interval == null) {
            interval = r.join(CuratorsTextureEntity_.INTERVAL_TEXT1, JoinType.LEFT);
          }
          return interval;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsCruiseEntity> joinCruise() {
          if (cruise == null) {
            cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE, JoinType.LEFT);
          }
          return cruise;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsDeviceEntity> joinDevice() {
          if (device == null) {
            device = joinSample().join(CuratorsSampleTsqpEntity_.DEVICE, JoinType.LEFT);
          }
          return device;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsLegEntity> joinLeg() {
          if (leg == null) {
            leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
          }
          return leg;
        }

        @Override
        public From<CuratorsTextureEntity, PlatformMasterEntity> joinPlatform() {
          if (platform == null) {
            platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM, JoinType.LEFT).join(CuratorsCruisePlatformEntity_.PLATFORM, JoinType.LEFT);
          }
          return platform;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsFacilityEntity> joinFacility() {
          if (facility == null) {
            facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY, JoinType.LEFT).join(CuratorsCruiseFacilityEntity_.FACILITY, JoinType.LEFT);
          }
          return facility;
        }

        @Override
        public boolean isJoinedSample() {
          return sample != null;
        }

        @Override
        public boolean isJoinedInterval() {
          return interval != null;
        }

        @Override
        public boolean isJoinedCruise() {
          return cruise != null;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsStorageMethEntity> joinStorageMethod() {
          if (storageMethod == null) {
            storageMethod = joinSample().join(CuratorsSampleTsqpEntity_.STORAGE_METH, JoinType.LEFT);
          }
          return storageMethod;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsProvinceEntity> joinProvince() {
          if (province == null) {
            province = joinSample().join(CuratorsSampleTsqpEntity_.PROVINCE, JoinType.LEFT);
          }
          return province;
        }
      };
    }
  };

  private static final SpecificationFactory<CuratorsTextureEntity> text2SpecFactory = new SpecificationFactory<CuratorsTextureEntity>() {

    @Override
    public Joiner<CuratorsTextureEntity> getJoins(Root<CuratorsTextureEntity> r) {
      return new Joiner<CuratorsTextureEntity>() {

        private From<CuratorsTextureEntity, CuratorsIntervalEntity> interval = null;
        private From<CuratorsTextureEntity, CuratorsSampleTsqpEntity> sample = null;
        private From<CuratorsTextureEntity, CuratorsCruiseEntity> cruise = null;
        private From<CuratorsTextureEntity, CuratorsLegEntity> leg = null;
        private From<CuratorsTextureEntity, PlatformMasterEntity> platform = null;
        private From<CuratorsTextureEntity, CuratorsFacilityEntity> facility = null;
        private From<CuratorsTextureEntity, CuratorsDeviceEntity> device = null;
        private From<CuratorsTextureEntity, CuratorsStorageMethEntity> storageMethod = null;
        private From<CuratorsTextureEntity, CuratorsProvinceEntity> province = null;

        @Override
        public From<CuratorsTextureEntity, CuratorsSampleTsqpEntity> joinSample() {
          if (sample == null) {
            sample = joinInterval().join(CuratorsIntervalEntity_.SAMPLE, JoinType.LEFT);
          }
          return sample;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsIntervalEntity> joinInterval() {
          if (interval == null) {
            interval = r.join(CuratorsTextureEntity_.INTERVAL_TEXT2, JoinType.LEFT);
          }
          return interval;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsCruiseEntity> joinCruise() {
          if (cruise == null) {
            cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE, JoinType.LEFT);
          }
          return cruise;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsDeviceEntity> joinDevice() {
          if (device == null) {
            device = joinSample().join(CuratorsSampleTsqpEntity_.DEVICE, JoinType.LEFT);
          }
          return device;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsLegEntity> joinLeg() {
          if (leg == null) {
            leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
          }
          return leg;
        }

        @Override
        public From<CuratorsTextureEntity, PlatformMasterEntity> joinPlatform() {
          if (platform == null) {
            platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM, JoinType.LEFT).join(CuratorsCruisePlatformEntity_.PLATFORM, JoinType.LEFT);
          }
          return platform;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsFacilityEntity> joinFacility() {
          if (facility == null) {
            facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY, JoinType.LEFT).join(CuratorsCruiseFacilityEntity_.FACILITY, JoinType.LEFT);
          }
          return facility;
        }

        @Override
        public boolean isJoinedSample() {
          return sample != null;
        }

        @Override
        public boolean isJoinedInterval() {
          return interval != null;
        }

        @Override
        public boolean isJoinedCruise() {
          return cruise != null;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsStorageMethEntity> joinStorageMethod() {
          if (storageMethod == null) {
            storageMethod = joinSample().join(CuratorsSampleTsqpEntity_.STORAGE_METH, JoinType.LEFT);
          }
          return storageMethod;
        }

        @Override
        public From<CuratorsTextureEntity, CuratorsProvinceEntity> joinProvince() {
          if (province == null) {
            province = joinSample().join(CuratorsSampleTsqpEntity_.PROVINCE, JoinType.LEFT);
          }
          return province;
        }
      };
    }
  };

  public PagedItemsView<String> searchTextures(GeosampleSearchParameterObject searchParameters) {
    
    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    
    Page<String> page = curatorsTextureRepository.getTextures(
        searchParameters, pageIndex, maxPerPage, text1SpecFactory, text2SpecFactory);

    return new PagedItemsView.Builder<String>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.toList())
        .build();
  }

  public PagedItemsView<String> searchRockLithologies(GeosampleSearchParameterObject searchParameters) {
    return rockLithAttributeService.search(searchParameters);
  }

  public PagedItemsView<String> searchLithologies(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    Page<String> page = curatorsLithologyRepository.getLithologies(
        searchParameters, pageIndex, maxPerPage, specificationFactory);

    return new PagedItemsView.Builder<String>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.toList())
        .build();
  }

  private static final SpecificationFactory<CuratorsLithologyEntity> lith1SpecFactory = new SpecificationFactory<CuratorsLithologyEntity>() {

    @Override
    public Joiner<CuratorsLithologyEntity> getJoins(Root<CuratorsLithologyEntity> r) {
      return new Joiner<CuratorsLithologyEntity>() {

        private From<CuratorsLithologyEntity, CuratorsIntervalEntity> interval = null;
        private From<CuratorsLithologyEntity, CuratorsSampleTsqpEntity> sample = null;
        private From<CuratorsLithologyEntity, CuratorsCruiseEntity> cruise = null;
        private From<CuratorsLithologyEntity, CuratorsLegEntity> leg = null;
        private From<CuratorsLithologyEntity, PlatformMasterEntity> platform = null;
        private From<CuratorsLithologyEntity, CuratorsFacilityEntity> facility = null;
        private From<CuratorsLithologyEntity, CuratorsDeviceEntity> device = null;
        private From<CuratorsLithologyEntity, CuratorsStorageMethEntity> storageMethod = null;
        private From<CuratorsLithologyEntity, CuratorsProvinceEntity> province = null;

        @Override
        public From<CuratorsLithologyEntity, CuratorsSampleTsqpEntity> joinSample() {
          if (sample == null) {
            sample = joinInterval().join(CuratorsIntervalEntity_.SAMPLE, JoinType.LEFT);
          }
          return sample;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsIntervalEntity> joinInterval() {
          if (interval == null) {
            interval = r.join(CuratorsLithologyEntity_.INTERVAL_LITH1, JoinType.LEFT);
          }
          return interval;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsCruiseEntity> joinCruise() {
          if (cruise == null) {
            cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE, JoinType.LEFT);
          }
          return cruise;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsDeviceEntity> joinDevice() {
          if (device == null) {
            device = joinSample().join(CuratorsSampleTsqpEntity_.DEVICE, JoinType.LEFT);
          }
          return device;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsLegEntity> joinLeg() {
          if (leg == null) {
            leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
          }
          return leg;
        }

        @Override
        public From<CuratorsLithologyEntity, PlatformMasterEntity> joinPlatform() {
          if (platform == null) {
            platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM, JoinType.LEFT).join(CuratorsCruisePlatformEntity_.PLATFORM, JoinType.LEFT);
          }
          return platform;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsFacilityEntity> joinFacility() {
          if (facility == null) {
            facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY, JoinType.LEFT).join(CuratorsCruiseFacilityEntity_.FACILITY, JoinType.LEFT);
          }
          return facility;
        }

        @Override
        public boolean isJoinedSample() {
          return sample != null;
        }

        @Override
        public boolean isJoinedInterval() {
          return interval != null;
        }

        @Override
        public boolean isJoinedCruise() {
          return cruise != null;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsStorageMethEntity> joinStorageMethod() {
          if (storageMethod == null) {
            storageMethod = joinSample().join(CuratorsSampleTsqpEntity_.STORAGE_METH, JoinType.LEFT);
          }
          return storageMethod;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsProvinceEntity> joinProvince() {
          if (province == null) {
            province = joinSample().join(CuratorsSampleTsqpEntity_.PROVINCE, JoinType.LEFT);
          }
          return province;
        }
      };
    }
  };

  private static final SpecificationFactory<CuratorsLithologyEntity> lith2SpecFactory = new SpecificationFactory<CuratorsLithologyEntity>() {

    @Override
    public Joiner<CuratorsLithologyEntity> getJoins(Root<CuratorsLithologyEntity> r) {
      return new Joiner<CuratorsLithologyEntity>() {

        private From<CuratorsLithologyEntity, CuratorsIntervalEntity> interval = null;
        private From<CuratorsLithologyEntity, CuratorsSampleTsqpEntity> sample = null;
        private From<CuratorsLithologyEntity, CuratorsCruiseEntity> cruise = null;
        private From<CuratorsLithologyEntity, CuratorsLegEntity> leg = null;
        private From<CuratorsLithologyEntity, PlatformMasterEntity> platform = null;
        private From<CuratorsLithologyEntity, CuratorsFacilityEntity> facility = null;
        private From<CuratorsLithologyEntity, CuratorsDeviceEntity> device = null;
        private From<CuratorsLithologyEntity, CuratorsStorageMethEntity> storageMethod = null;
        private From<CuratorsLithologyEntity, CuratorsProvinceEntity> province = null;

        @Override
        public From<CuratorsLithologyEntity, CuratorsSampleTsqpEntity> joinSample() {
          if (sample == null) {
            sample = joinInterval().join(CuratorsIntervalEntity_.SAMPLE, JoinType.LEFT);
          }
          return sample;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsIntervalEntity> joinInterval() {
          if (interval == null) {
            interval = r.join(CuratorsLithologyEntity_.INTERVAL_LITH2, JoinType.LEFT);
          }
          return interval;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsCruiseEntity> joinCruise() {
          if (cruise == null) {
            cruise = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE, JoinType.LEFT);
          }
          return cruise;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsDeviceEntity> joinDevice() {
          if (device == null) {
            device = joinSample().join(CuratorsSampleTsqpEntity_.DEVICE, JoinType.LEFT);
          }
          return device;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsLegEntity> joinLeg() {
          if (leg == null) {
            leg = joinSample().join(CuratorsSampleTsqpEntity_.LEG, JoinType.LEFT);
          }
          return leg;
        }

        @Override
        public From<CuratorsLithologyEntity, PlatformMasterEntity> joinPlatform() {
          if (platform == null) {
            platform = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_PLATFORM, JoinType.LEFT).join(CuratorsCruisePlatformEntity_.PLATFORM, JoinType.LEFT);
          }
          return platform;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsFacilityEntity> joinFacility() {
          if (facility == null) {
            facility = joinSample().join(CuratorsSampleTsqpEntity_.CRUISE_FACILITY, JoinType.LEFT).join(CuratorsCruiseFacilityEntity_.FACILITY, JoinType.LEFT);
          }
          return facility;
        }

        @Override
        public boolean isJoinedSample() {
          return sample != null;
        }

        @Override
        public boolean isJoinedInterval() {
          return interval != null;
        }

        @Override
        public boolean isJoinedCruise() {
          return cruise != null;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsStorageMethEntity> joinStorageMethod() {
          if (storageMethod == null) {
            storageMethod = joinSample().join(CuratorsSampleTsqpEntity_.STORAGE_METH, JoinType.LEFT);
          }
          return storageMethod;
        }

        @Override
        public From<CuratorsLithologyEntity, CuratorsProvinceEntity> joinProvince() {
          if (province == null) {
            province = joinSample().join(CuratorsSampleTsqpEntity_.PROVINCE, JoinType.LEFT);
          }
          return province;
        }
      };
    }
  };
}
