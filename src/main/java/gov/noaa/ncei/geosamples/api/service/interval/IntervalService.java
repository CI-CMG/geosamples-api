package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.error.ApiError;
import gov.noaa.ncei.geosamples.api.error.ApiException;
import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsLithologyRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsTextureRepository;
import gov.noaa.ncei.geosamples.api.service.PagingIterator;
import gov.noaa.ncei.geosamples.api.service.ViewTransformers;
import gov.noaa.ncei.geosamples.api.service.csv.CsvExportHandler;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.IntervalView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.Collectors;
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
  private final RemarkAttributeService remarkAttributeService;

  @Autowired
  public IntervalService(CuratorsIntervalRepository curatorsIntervalRepository,
      IntervalSpecificationFactory specificationFactory,
      GeologicAgesAttributeService geologicAgesAttributeService,
      MetamorphismAttributeService metamorphismAttributeService,
      WeatheringAttributeService weatheringAttributeService,
      MineralogiesAttributeService mineralogiesAttributeService,
      CuratorsTextureRepository curatorsTextureRepository,
      CuratorsLithologyRepository curatorsLithologyRepository,
      RockLithAttributeService rockLithAttributeService,
      RemarkAttributeService remarkAttributeService) {
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
    this.remarkAttributeService = remarkAttributeService;
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

  public PagedItemsView<String> searchRemarks(GeosampleSearchParameterObject searchParameters) {
    return remarkAttributeService.search(searchParameters);
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

  public PagedItemsView<String> searchTextures(GeosampleSearchParameterObject searchParameters) {
    
    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    
    Page<String> page = curatorsTextureRepository.getTextures(
        searchParameters, pageIndex, maxPerPage, specificationFactory);

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

  public PagedItemsView<String> searchCompositions(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    Page<String> page = curatorsLithologyRepository.getCompositions(
        searchParameters, pageIndex, maxPerPage, specificationFactory);

    return new PagedItemsView.Builder<String>()
        .withItemsPerPage(maxPerPage)
        .withTotalPages(page.getTotalPages())
        .withPage(page.getNumber() + 1)
        .withTotalItems(page.getTotalElements())
        .withItems(page.toList())
        .build();
  }

}
