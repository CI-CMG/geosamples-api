package gov.noaa.ncei.geosamples.api.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

class QueryContext<E, T> {

  private final Root<E> root;
  private final CriteriaQuery<T> query;
  private final CriteriaBuilder criteriaBuilder;

  QueryContext(Root<E> root, CriteriaQuery<T> query, CriteriaBuilder criteriaBuilder) {
    this.root = root;
    this.query = query;
    this.criteriaBuilder = criteriaBuilder;
  }

  Root<E> getRoot() {
    return root;
  }

  CriteriaQuery<T> getQuery() {
    return query;
  }

  CriteriaBuilder getCriteriaBuilder() {
    return criteriaBuilder;
  }

}
