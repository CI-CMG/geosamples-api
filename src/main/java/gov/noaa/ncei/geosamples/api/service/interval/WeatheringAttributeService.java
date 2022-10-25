package gov.noaa.ncei.geosamples.api.service.interval;

import gov.noaa.ncei.geosamples.api.repository.CuratorsWeathMetaRepository;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.AttributeService;
import gov.noaa.ncei.geosamples.api.service.SearchUtils;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class WeatheringAttributeService extends AttributeService<CuratorsWeathMetaEntity> {

  @Autowired
  WeatheringAttributeService(CuratorsWeathMetaRepository repository, WeathMetaSpecificationFactory specificationFactory) {
    super(CuratorsWeathMetaEntity_.WEATH_META, repository, specificationFactory);
  }

  @Override
  protected SpecExtender<CuratorsWeathMetaEntity> getAdditionalSpecifications() {
    return (specs, r, cb, j) -> {
      specs.add(cb.isNotNull(j.joinInterval().get(CuratorsIntervalEntity_.WEATH_META)));
      specs.add(SearchUtils.startsWithIgnoreCase(cb, "weathering - ", r.get(CuratorsWeathMetaEntity_.WEATH_META)));
    };
  }

  @Override
  protected String mapItem(String item) {
    return item.replaceFirst("weathering - ", "");
  }
}
