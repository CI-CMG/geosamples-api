package gov.noaa.ncei.geosamples.api.service.sample;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.service.csv.CsvExportHandler;
import gov.noaa.ncei.geosamples.api.view.CountView;
import gov.noaa.ncei.geosamples.api.view.MinMaxView;
import gov.noaa.ncei.geosamples.api.view.PagedItemsView;
import gov.noaa.ncei.geosamples.api.view.SampleDetailView;
import gov.noaa.ncei.geosamples.api.view.SampleDetailViewImpl;
import gov.noaa.ncei.geosamples.api.view.SampleDisplayView;
import gov.noaa.ncei.geosamples.api.view.SampleLinkedDetailView;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import java.io.OutputStream;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SampleService {

  private final SampleDetailService sampleDetailService;
  private final CsvExportHandler<SampleDetailView> csvExportHandler;
  private final CuratorsSampleTsqpRepository sampleRepository;
  private final SampleDisplayService sampleDisplayService;
  private final SampleLinkedDetailService sampleLinkedDetailService;
  private final SampleSpecificationFactory specificationFactory;
  private final IgsnAttributeService igsnAttributeService;
  private final LakeAttributeService lakeAttributeService;
  private final DeviceAttributeService deviceAttributeService;
  private final ProvinceAttributeService provinceAttributeService;
  private final StorageMethodAttributeService storageMethService;
  private final PlatformAttributeService platformAttributeService;

  @Autowired
  public SampleService(SampleDetailService sampleDetailService, CuratorsSampleTsqpRepository sampleRepository,
      SampleDisplayService sampleDisplayService, SampleLinkedDetailService sampleLinkedDetailService,
      SampleSpecificationFactory specificationFactory, IgsnAttributeService igsnAttributeService,
      LakeAttributeService lakeAttributeService, DeviceAttributeService deviceAttributeService,
      ProvinceAttributeService provinceAttributeService, StorageMethodAttributeService storageMethService,
      PlatformAttributeService platformAttributeService) {
    this.sampleDetailService = sampleDetailService;
    this.sampleDisplayService = sampleDisplayService;
    this.sampleLinkedDetailService = sampleLinkedDetailService;
    this.specificationFactory = specificationFactory;
    this.igsnAttributeService = igsnAttributeService;
    this.lakeAttributeService = lakeAttributeService;
    this.deviceAttributeService = deviceAttributeService;
    this.provinceAttributeService = provinceAttributeService;
    this.storageMethService = storageMethService;
    this.platformAttributeService = platformAttributeService;
    this.csvExportHandler = new CsvExportHandler<>(SampleDetailView.class, SampleDetailViewImpl.class);
    this.sampleRepository = sampleRepository;
  }

  public void exportCsv(OutputStream outputStream, GeosampleSearchParameterObject searchParameters) {
    csvExportHandler.export(outputStream, searchDetail(searchParameters).getItems());
  }

  public MinMaxView getDepthRange(GeosampleSearchParameterObject searchParameters) {

    int maxPerPage = searchParameters.getItemsPerPage();
    int pageIndex = searchParameters.getPage() - 1;
    Page<MinMaxView> page = sampleRepository.searchParameters(
        searchParameters,
        pageIndex, maxPerPage,
        MinMaxView.class,
        (r, cb, j) -> cb.construct(
            MinMaxView.class,
            cb.coalesce(cb.min(r.get(CuratorsSampleTsqpEntity_.WATER_DEPTH)), 0),
            cb.coalesce(cb.max(r.get(CuratorsSampleTsqpEntity_.WATER_DEPTH)), 0)),
        (r, cb, j) -> Collections.emptyList(),
        specificationFactory);

    return page.toList().get(0);
  }

  public SampleLinkedDetailView load(String id) {
    return sampleLinkedDetailService.load(id);
  }

  public PagedItemsView<SampleDetailView> searchDetail(GeosampleSearchParameterObject searchParameters) {
    return sampleDetailService.search(searchParameters);
  }

  public PagedItemsView<SampleDisplayView> searchDisplay(GeosampleSearchParameterObject searchParameters) {
    return sampleDisplayService.search(searchParameters);
  }

  public CountView count(GeosampleSearchParameterObject searchParameters) {
    return sampleDetailService.count(searchParameters);
  }

  public PagedItemsView<String> searchIgsn(GeosampleSearchParameterObject searchParameters) {
    return igsnAttributeService.search(searchParameters);
  }

  public PagedItemsView<String> searchLake(GeosampleSearchParameterObject searchParameters) {
    return lakeAttributeService.search(searchParameters);
  }

  public PagedItemsView<String> searchDevice(GeosampleSearchParameterObject searchParameters) {
    return deviceAttributeService.search(searchParameters);
  }

  public PagedItemsView<String> searchProvince(GeosampleSearchParameterObject searchParameters) {
    return provinceAttributeService.search(searchParameters);
  }

  public PagedItemsView<String> searchStorageMeth(GeosampleSearchParameterObject searchParameters) {
    return storageMethService.search(searchParameters);
  }

  public PagedItemsView<String> searchPlatform(GeosampleSearchParameterObject searchParameters) {
    return platformAttributeService.search(searchParameters);
  }
}
