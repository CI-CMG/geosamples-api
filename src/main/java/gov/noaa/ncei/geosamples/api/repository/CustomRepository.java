package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SelectBuilder;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SortBuilder;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.SpecExtender;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

@NoRepositoryBean
public interface CustomRepository<E> {

  <P> Page<P> searchParameters(
      GeosampleSearchParameterObject searchParameters,
      int page, int pageSize,
      Class<P> projection,
      SelectBuilder<E, P> selectBuilder,
      SortBuilder<E> sortBuilder,
      SpecificationFactory<E> specFactory);

  <P> Page<P> searchParameters(
      GeosampleSearchParameterObject searchParameters,
      int page, int pageSize,
      Class<P> projection,
      SelectBuilder<E, P> selectBuilder,
      SortBuilder<E> sortBuilder,
      SpecificationFactory<E> specFactory,
      @Nullable SpecExtender<E> specExtender);

  Long count(GeosampleSearchParameterObject searchParameters,
      SpecificationFactory<E> specFactory,
      @Nullable SpecExtender<E> specExtender);

  Long count(GeosampleSearchParameterObject searchParameters,
      SpecificationFactory<E> specFactory);
}
