package gov.noaa.ncei.geosamples.api.repository;

import static gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.where;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class CustomTextureRepositoryImpl implements CustomTextureRepository {

  @PersistenceContext
  private EntityManager entityManager;


  private Long count(
      GeosampleSearchParameterObject searchParameters,
      SpecificationFactory<CuratorsIntervalEntity> specFactory) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class).distinct(true);
    Root<CuratorsTextureEntity> r = q.from(CuratorsTextureEntity.class);

    textWhere(searchParameters, r, cb, q, specFactory);

    return entityManager.createQuery(q.select(cb.count(r))).getSingleResult();
  }


  private static long getOffset(int page, int pageSize) {
    return (long) page * (long) pageSize;
  }

  private void textWhere(
      GeosampleSearchParameterObject searchParameters,
      Root<CuratorsTextureEntity> r,
      CriteriaBuilder cb,
      CriteriaQuery<?> q,
      SpecificationFactory<CuratorsIntervalEntity> specFactory) {


    Subquery<CuratorsIntervalEntity> subCq = q.subquery(CuratorsIntervalEntity.class);
    Root<CuratorsIntervalEntity> ci = subCq.from(CuratorsIntervalEntity.class);

    Joiner<CuratorsIntervalEntity> joiner = specFactory.getJoins(ci);
    List<Predicate> specs = new ArrayList<>();
    specs.add(cb.and(where(searchParameters, ci, cb, q, joiner, null).toArray(new Predicate[0])));
    specs.add(cb.or(
        cb.equal(ci.get(CuratorsIntervalEntity_.TEXT1), r.get(CuratorsTextureEntity_.TEXTURE)),
        cb.equal(ci.get(CuratorsIntervalEntity_.TEXT2), r.get(CuratorsTextureEntity_.TEXTURE))));

    subCq.select(ci.get(CuratorsIntervalEntity_.ID));
    subCq.where(cb.and(specs.toArray(new Predicate[0])));

    q.where(cb.exists(subCq));

  }

  @Override
  public Page<String> getTextures(
      GeosampleSearchParameterObject searchParameters,
      int page, int pageSize,
      SpecificationFactory<CuratorsIntervalEntity> specFactory) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<String> q = cb.createQuery(String.class).distinct(true);
    Root<CuratorsTextureEntity> r = q.from(CuratorsTextureEntity.class);

    textWhere(searchParameters, r, cb, q, specFactory);

    TypedQuery<String> typedQuery = entityManager.createQuery(q.select(r.get(CuratorsTextureEntity_.TEXTURE)).orderBy(cb.asc(r.get(CuratorsTextureEntity_.TEXTURE))));
    typedQuery.setFirstResult((int) getOffset(page, pageSize));
    typedQuery.setMaxResults(pageSize);

    return PageableExecutionUtils.getPage(typedQuery.getResultList(), Pageable.ofSize(pageSize).withPage(page),
        () -> count(searchParameters, specFactory));
  }

}
