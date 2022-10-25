package gov.noaa.ncei.geosamples.api.service;

import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import javax.persistence.criteria.Root;

public interface SpecificationFactory<E> {

  Joiner<E> getJoins(Root<E> r);

}
