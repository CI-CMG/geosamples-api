package gov.noaa.ncei.geosamples.api.repository;

import static gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.where;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.repository.CustomRepositoryImpl.Joiner;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class CustomTextureRepositoryImpl implements CustomTextureRepository {

  @PersistenceContext
  private EntityManager entityManager;


  private Long count(
      GeosampleSearchParameterObject searchParameters,
      SpecificationFactory<CuratorsTextureEntity> specFactory1,
      SpecificationFactory<CuratorsTextureEntity> specFactory2) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class).distinct(true);
    Root<CuratorsTextureEntity> r = q.from(CuratorsTextureEntity.class);

    textWhere(searchParameters, r, cb, q, specFactory1, specFactory2);

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
      SpecificationFactory<CuratorsTextureEntity> specFactory1,
      SpecificationFactory<CuratorsTextureEntity> specFactory2) {

    Joiner<CuratorsTextureEntity> joiner1 = specFactory1.getJoins(r);
    Joiner<CuratorsTextureEntity> joiner2 = specFactory2.getJoins(r);

    List<Predicate> specs =new ArrayList<>();
    specs.add(cb.or(cb.isNotNull(joiner1.joinInterval().get(CuratorsIntervalEntity_.TEXT1)), cb.isNotNull(joiner2.joinInterval().get(CuratorsIntervalEntity_.TEXT2))));
    specs.add(cb.or(
        cb.and(where(searchParameters, r, cb, q, joiner1, null).toArray(new Predicate[0])),
        cb.and(where(searchParameters, r, cb, q, joiner2, null).toArray(new Predicate[0]))));

    q.where(cb.and(specs.toArray(new Predicate[0])));
  }

  @Override
  public Page<String> getTextures(
      GeosampleSearchParameterObject searchParameters,
      int page, int pageSize,
      SpecificationFactory<CuratorsTextureEntity> specFactory1,
      SpecificationFactory<CuratorsTextureEntity> specFactory2) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<String> q = cb.createQuery(String.class).distinct(true);
    Root<CuratorsTextureEntity> r = q.from(CuratorsTextureEntity.class);

    textWhere(searchParameters, r, cb, q, specFactory1, specFactory2);

    TypedQuery<String> typedQuery = entityManager.createQuery(q.select(r.get(CuratorsTextureEntity_.TEXTURE)).orderBy(cb.asc(r.get(CuratorsTextureEntity_.TEXTURE))));
    typedQuery.setFirstResult((int) getOffset(page, pageSize));
    typedQuery.setMaxResults(pageSize);

    return PageableExecutionUtils.getPage(typedQuery.getResultList(), Pageable.ofSize(pageSize).withPage(page),
        () -> count(searchParameters, specFactory1, specFactory2));
  }

}
