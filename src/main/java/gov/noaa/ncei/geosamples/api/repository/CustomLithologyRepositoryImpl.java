package gov.noaa.ncei.geosamples.api.repository;

import static gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.where;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.support.PageableExecutionUtils;

public class CustomLithologyRepositoryImpl implements CustomLithologyRepository {

  @PersistenceContext
  private EntityManager entityManager;

  private <T> QueryContext<CuratorsLithologyEntity, T> query(Class<T> queryClass, Specification<CuratorsLithologyEntity> spec) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> query = cb.createQuery(queryClass);
    Root<CuratorsLithologyEntity> from = query.from(CuratorsLithologyEntity.class);
    Join<CuratorsLithologyEntity, CuratorsIntervalEntity> interval1 = from.join(CuratorsLithologyEntity_.INTERVAL_LITH1, JoinType.LEFT);
    Join<CuratorsLithologyEntity, CuratorsIntervalEntity> interval2 = from.join(CuratorsLithologyEntity_.INTERVAL_LITH2, JoinType.LEFT);

    Predicate where = cb.or(
        cb.isNotNull(interval1.get(CuratorsIntervalEntity_.LITH1)),
        cb.isNotNull(interval2.get(CuratorsIntervalEntity_.LITH2))
    );
    if (spec != null) {
      Predicate predicate = spec.toPredicate(from, query, cb);
      if (predicate != null) {
        where = cb.and(where, predicate);
      }
    }
    query.where(where);
    query.distinct(true);
    return new QueryContext<>(from, query, cb);
  }

  private static long getOffset(int page, int pageSize) {
    return (long) page * (long) pageSize;
  }

  private Long count(
      GeosampleSearchParameterObject searchParameters,
      SpecificationFactory<CuratorsIntervalEntity> specFactory) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class).distinct(true);
    Root<CuratorsLithologyEntity> r = q.from(CuratorsLithologyEntity.class);

    lithWhere(searchParameters, r, cb, q, specFactory);

    return entityManager.createQuery(q.select(cb.count(r))).getSingleResult();
  }

  private void lithWhere(
      GeosampleSearchParameterObject searchParameters,
      Root<CuratorsLithologyEntity> r,
      CriteriaBuilder cb,
      CriteriaQuery<?> q,
      SpecificationFactory<CuratorsIntervalEntity> specFactory) {

    Subquery<CuratorsIntervalEntity> subCq = q.subquery(CuratorsIntervalEntity.class);
    Root<CuratorsIntervalEntity> ci = subCq.from(CuratorsIntervalEntity.class);

    Joiner<CuratorsIntervalEntity> joiner = specFactory.getJoins(ci);
    List<Predicate> specs = new ArrayList<>();
    specs.add(cb.and(where(searchParameters, ci, cb, q, joiner, null).toArray(new Predicate[0])));
    specs.add(cb.or(cb.equal(ci.get(CuratorsIntervalEntity_.LITH1), r.get(CuratorsLithologyEntity_.LITHOLOGY)),
        cb.equal(ci.get(CuratorsIntervalEntity_.LITH2), r.get(CuratorsLithologyEntity_.LITHOLOGY))));

    subCq.select(ci.get(CuratorsIntervalEntity_.ID));
    subCq.where(cb.and(specs.toArray(new Predicate[0])));

    q.where(cb.exists(subCq));

  }

  @Override
  public Page<String> getLithologies(
      GeosampleSearchParameterObject searchParameters,
      int page, int pageSize,
      SpecificationFactory<CuratorsIntervalEntity> specFactory
  ) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<String> q = cb.createQuery(String.class).distinct(true);
    Root<CuratorsLithologyEntity> r = q.from(CuratorsLithologyEntity.class);

    lithWhere(searchParameters, r, cb, q, specFactory);

    TypedQuery<String> typedQuery = entityManager.createQuery(
        q.select(r.get(CuratorsLithologyEntity_.LITHOLOGY)).orderBy(cb.asc(r.get(CuratorsLithologyEntity_.LITHOLOGY))));
    typedQuery.setFirstResult((int) getOffset(page, pageSize));
    typedQuery.setMaxResults(pageSize);

    return PageableExecutionUtils.getPage(typedQuery.getResultList(), Pageable.ofSize(pageSize).withPage(page),
        () -> count(searchParameters, specFactory));
  }
}
