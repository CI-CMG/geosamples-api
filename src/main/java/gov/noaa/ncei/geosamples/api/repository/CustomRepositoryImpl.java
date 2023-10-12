package gov.noaa.ncei.geosamples.api.repository;

import gov.noaa.ncei.geosamples.api.model.GeosampleSearchParameterObject;
import gov.noaa.ncei.geosamples.api.service.IntersectsPredicate;
import gov.noaa.ncei.geosamples.api.service.SearchUtils;
import gov.noaa.ncei.geosamples.api.service.SpecificationFactory;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity_;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class CustomRepositoryImpl<E, ID> extends SimpleJpaRepository<E, ID> implements CustomRepository<E> {


  private final EntityManager entityManager;

  public CustomRepositoryImpl(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }


  private static long getOffset(int page, int pageSize) {
    return (long) page * (long) pageSize;
  }

  @Override
  public Long count(GeosampleSearchParameterObject searchParameters, SpecificationFactory<E> specFactory) {
    return count(searchParameters, specFactory, null);
  }

  @Override
  public Long count(GeosampleSearchParameterObject searchParameters, SpecificationFactory<E> specFactory, @Nullable SpecExtender<E> specExtender) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<E> r = q.from(getDomainClass());
    Joiner<E> joiner = specFactory.getJoins(r);

    List<Predicate> specs = where(searchParameters, r, cb, q, joiner, specExtender);

    if (!specs.isEmpty()) {
      q.where(cb.and(specs.toArray(new Predicate[0])));
    }

    return entityManager.createQuery(q.select(cb.countDistinct(r))).getSingleResult();
  }

  @Override
  public <P> Page<P> searchParameters(
      GeosampleSearchParameterObject searchParameters,
      int page, int pageSize,
      Class<P> projection,
      SelectBuilder<E, P> selectBuilder,
      SortBuilder<E> sortBuilder,
      SpecificationFactory<E> specFactory) {
    return searchParameters(searchParameters, page, pageSize, projection, selectBuilder, sortBuilder, specFactory, null);
  }


  @Override
  public <P> Page<P> searchParameters(
      GeosampleSearchParameterObject searchParameters,
      int page, int pageSize,
      Class<P> projection,
      SelectBuilder<E, P> selectBuilder,
      SortBuilder<E> sortBuilder,
      SpecificationFactory<E> specFactory,
      @Nullable SpecExtender<E> specExtender) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<P> q = cb.createQuery(projection).distinct(true);
    Root<E> r = q.from(getDomainClass());
    Joiner<E> joiner = specFactory.getJoins(r);

    List<Predicate> specs = where(searchParameters, r, cb, q, joiner, specExtender);

    if (!specs.isEmpty()) {
      q.where(cb.and(specs.toArray(new Predicate[0])));
    }

    TypedQuery<P> typedQuery = entityManager.createQuery(q.select(selectBuilder.select(r, cb, joiner)).orderBy(sortBuilder.sort(r, cb, joiner)));
    typedQuery.setFirstResult((int) getOffset(page, pageSize));
    typedQuery.setMaxResults(pageSize);

    return PageableExecutionUtils.getPage(typedQuery.getResultList(), Pageable.ofSize(pageSize).withPage(page),
        () -> count(searchParameters, specFactory, specExtender));
  }


  public static  <E, P> List<Predicate> where(GeosampleSearchParameterObject searchParameters, Root<E> r, CriteriaBuilder cb, CriteriaQuery<P> q, Joiner<E> joiner,
      @Nullable SpecExtender<E> specExtender) {
    List<Predicate> specs = new ArrayList<>();

    String repository = searchParameters.getRepository();
    String bbox = searchParameters.getBbox();
    String platform = searchParameters.getPlatform();
    String lake = searchParameters.getLake();
    String cruise = searchParameters.getCruise();
    Long cruiseId = searchParameters.getCruiseId();
    Integer cruiseYear = searchParameters.getCruiseYear();
    String device = searchParameters.getDevice();
    String startDate = searchParameters.getStartDate();
    Integer minDepth = searchParameters.getMinDepth();
    Integer maxDepth = searchParameters.getMaxDepth();
    String igsn = searchParameters.getIgsn();
    String lithology = searchParameters.getLithology();
    String rockLithology = searchParameters.getRockLithology();
    String texture = searchParameters.getTexture();
    String mineralogy = searchParameters.getMineralogy();
    String weathering = searchParameters.getWeathering();
    String metamorphism = searchParameters.getMetamorphism();
    String composition = searchParameters.getComposition();
    String storageMethod = searchParameters.getStorageMethod();
    String province = searchParameters.getProvince();
    String age = searchParameters.getAge();
    String imlgs = searchParameters.getImlgs();
    Long platformId = searchParameters.getPlatformId();
    Long facilityId = searchParameters.getFacilityId();
    Long intervalId = searchParameters.getIntervalId();
    String leg = searchParameters.getLeg();
    Geometry wkt = searchParameters.getAoi();
    String startBeginsWith = searchParameters.getStartDateBeginsWith();

    if (StringUtils.hasText(startBeginsWith)) {
      specs.add(SearchUtils.startsWithIgnoreCase(cb, startBeginsWith.trim(), joiner.joinSample().get(CuratorsSampleTsqpEntity_.BEGIN_DATE)));
    }

    if (cruiseId != null) {
      specs.add(cb.equal(joiner.joinCruise().get(CuratorsCruiseEntity_.ID), cruiseId));
    }
    if (platformId != null) {
      specs.add(cb.equal(joiner.joinPlatform().get(PlatformMasterEntity_.ID), platformId));
    }
    if (facilityId != null) {
      specs.add(cb.equal(joiner.joinFacility().get(CuratorsFacilityEntity_.ID), facilityId));
    }
    if (intervalId != null) {
      specs.add(cb.equal(joiner.joinInterval().get(CuratorsIntervalEntity_.ID), intervalId));
    }
    if (StringUtils.hasText(bbox) && wkt == null) {
      Bbox box = Bbox.parse(bbox);
      specs.add(cb.greaterThanOrEqualTo(joiner.joinSample().get(CuratorsSampleTsqpEntity_.LON), box.getXMin()));
      specs.add(cb.greaterThanOrEqualTo(joiner.joinSample().get(CuratorsSampleTsqpEntity_.LAT), box.getYMin()));
      specs.add(cb.lessThanOrEqualTo(joiner.joinSample().get(CuratorsSampleTsqpEntity_.LON), box.getXMax()));
      specs.add(cb.lessThanOrEqualTo(joiner.joinSample().get(CuratorsSampleTsqpEntity_.LAT), box.getYMax()));
    }
    if (StringUtils.hasText(cruise)) {
      specs.add(SearchUtils.startsWithIgnoreCase(cb, cruise, joiner.joinCruise().get(CuratorsCruiseEntity_.CRUISE_NAME)));
    }
    if (StringUtils.hasText(leg)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, leg, joiner.joinLeg().get(CuratorsLegEntity_.LEG_NAME)));
    }
    if (cruiseYear != null) {
      specs.add(cb.equal(joiner.joinCruise().get(CuratorsCruiseEntity_.YEAR), cruiseYear));
    }
    if (StringUtils.hasText(repository)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, repository, joiner.joinFacility().get(CuratorsFacilityEntity_.FACILITY_CODE)));
    }
    if (StringUtils.hasText(platform)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, platform, joiner.joinPlatform().get(PlatformMasterEntity_.PLATFORM)));
    }
    if (StringUtils.hasText(lake)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, lake, joiner.joinSample().get(CuratorsSampleTsqpEntity_.LAKE)));
    }
    if (StringUtils.hasText(startDate)) {
      specs.add(cb.equal(joiner.joinSample().get(CuratorsSampleTsqpEntity_.BEGIN_DATE), startDate));
    }
    if (StringUtils.hasText(device)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, device, joiner.joinSample()
          .join(CuratorsSampleTsqpEntity_.DEVICE).get(CuratorsDeviceEntity_.DEVICE)));
    }
    if (minDepth != null) {
      specs.add(cb.greaterThanOrEqualTo(joiner.joinSample().get(CuratorsSampleTsqpEntity_.WATER_DEPTH), minDepth));
    }
    if (maxDepth != null) {
      specs.add(cb.lessThanOrEqualTo(joiner.joinSample().get(CuratorsSampleTsqpEntity_.WATER_DEPTH), maxDepth));
    }
    if (StringUtils.hasText(igsn)) {
      specs.add(cb.or(
          SearchUtils.equalIgnoreCase(cb, igsn, joiner.joinSample().get(CuratorsSampleTsqpEntity_.IGSN)),
          SearchUtils.equalIgnoreCase(cb, igsn, joiner.joinInterval().get(CuratorsIntervalEntity_.IGSN))
      ));
    }
    if (StringUtils.hasText(lithology)) {
      specs.add(cb.or(
          SearchUtils.equalIgnoreCase(cb, lithology,
              joiner.joinInterval().join(CuratorsIntervalEntity_.LITH1, JoinType.LEFT).get(CuratorsLithologyEntity_.LITHOLOGY)),
          SearchUtils.equalIgnoreCase(cb, lithology,
              joiner.joinInterval().join(CuratorsIntervalEntity_.LITH2, JoinType.LEFT).get(CuratorsLithologyEntity_.LITHOLOGY))
      ));
    }
    if (StringUtils.hasText(rockLithology)) {
      specs.add(
          SearchUtils.equalIgnoreCase(cb, rockLithology,
              joiner.joinInterval().join(CuratorsIntervalEntity_.ROCK_LITH, JoinType.LEFT).get(CuratorsRockLithEntity_.ROCK_LITH))
      );
    }
    if (StringUtils.hasText(texture)) {
      specs.add(cb.or(
          SearchUtils.equalIgnoreCase(cb, texture, joiner.joinInterval().join(CuratorsIntervalEntity_.TEXT1, JoinType.LEFT).get(
              CuratorsTextureEntity_.TEXTURE)),
          SearchUtils.equalIgnoreCase(cb, texture,
              joiner.joinInterval().join(CuratorsIntervalEntity_.TEXT2, JoinType.LEFT).get(CuratorsTextureEntity_.TEXTURE))
      ));
    }
    if (StringUtils.hasText(mineralogy)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, mineralogy, joiner.joinInterval()
          .join(CuratorsIntervalEntity_.ROCK_MIN).get(CuratorsRockMinEntity_.ROCK_MIN)));
    }
    if (StringUtils.hasText(weathering)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, "weathering - " + weathering, joiner.joinInterval()
          .join(CuratorsIntervalEntity_.WEATH_META).get(CuratorsWeathMetaEntity_.WEATH_META)));
    }
    if (StringUtils.hasText(metamorphism)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, "metamorphism - " + metamorphism, joiner.joinInterval()
          .join(CuratorsIntervalEntity_.WEATH_META).get(CuratorsWeathMetaEntity_.WEATH_META)));
    }
    if (StringUtils.hasText(composition)) {
      specs.add(cb.or(
          SearchUtils.equalIgnoreCase(cb, composition,
              joiner.joinInterval().join(CuratorsIntervalEntity_.COMP1, JoinType.LEFT).get(CuratorsLithologyEntity_.LITHOLOGY)),
          SearchUtils.equalIgnoreCase(cb, composition,
              joiner.joinInterval().join(CuratorsIntervalEntity_.COMP2, JoinType.LEFT).get(CuratorsLithologyEntity_.LITHOLOGY)),
          SearchUtils.equalIgnoreCase(cb, composition,
              joiner.joinInterval().join(CuratorsIntervalEntity_.COMP3, JoinType.LEFT).get(CuratorsLithologyEntity_.LITHOLOGY)),
          SearchUtils.equalIgnoreCase(cb, composition,
              joiner.joinInterval().join(CuratorsIntervalEntity_.COMP4, JoinType.LEFT).get(CuratorsLithologyEntity_.LITHOLOGY)),
          SearchUtils.equalIgnoreCase(cb, composition,
              joiner.joinInterval().join(CuratorsIntervalEntity_.COMP5, JoinType.LEFT).get(CuratorsLithologyEntity_.LITHOLOGY)),
          SearchUtils.equalIgnoreCase(cb, composition,
              joiner.joinInterval().join(CuratorsIntervalEntity_.COMP6, JoinType.LEFT).get(CuratorsLithologyEntity_.LITHOLOGY))
      ));
    }
    if (StringUtils.hasText(storageMethod)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, storageMethod, joiner.joinSample()
          .join(CuratorsSampleTsqpEntity_.STORAGE_METH).get(CuratorsStorageMethEntity_.STORAGE_METH)));
    }
    if (StringUtils.hasText(province)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, province, joiner.joinSample()
          .join(CuratorsSampleTsqpEntity_.PROVINCE).get(CuratorsProvinceEntity_.PROVINCE)));
    }
    if (StringUtils.hasText(age)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, age, joiner.joinInterval()
          .join(CuratorsIntervalEntity_.AGES).get(CuratorsAgeEntity_.AGE)));
    }
    if (StringUtils.hasText(imlgs)) {
      specs.add(SearchUtils.equalIgnoreCase(cb, imlgs, joiner.joinSample().get(CuratorsSampleTsqpEntity_.IMLGS)));
    }

    if (wkt != null) {
      specs.add(new IntersectsPredicate(cb, joiner.joinSample().get(CuratorsSampleTsqpEntity_.SHAPE), wkt));
    }

    if (specExtender != null) {
      specExtender.extend(specs, r, cb, joiner);
    }

    if (joiner.isJoinedInterval()) {
      specs.add(cb.equal(joiner.joinSample().get(CuratorsSampleTsqpEntity_.PUBLISH), "Y"));
      specs.add(cb.equal(joiner.joinCruise().get(CuratorsCruiseEntity_.PUBLISH), "Y"));
      specs.add(cb.equal(joiner.joinInterval().get(CuratorsIntervalEntity_.PUBLISH), "Y"));
    } else if (joiner.isJoinedSample()) {
      specs.add(cb.equal(joiner.joinSample().get(CuratorsSampleTsqpEntity_.PUBLISH), "Y"));
      specs.add(cb.equal(joiner.joinCruise().get(CuratorsCruiseEntity_.PUBLISH), "Y"));
    } else if (joiner.isJoinedCruise()) {
      specs.add(cb.equal(joiner.joinCruise().get(CuratorsCruiseEntity_.PUBLISH), "Y"));
    }

    return specs;
  }

  @FunctionalInterface
  public interface SpecExtender<E> {

    void extend(List<Predicate> specs, Root<E> r, CriteriaBuilder cb, Joiner<E> joiner);
  }

  @FunctionalInterface
  public interface SelectBuilder<E, P> {

    Selection<? extends P> select(Root<E> r, CriteriaBuilder cb, Joiner<E> j);
  }

  @FunctionalInterface
  public interface SortBuilder<E> {

    List<Order> sort(Root<E> r, CriteriaBuilder cb, Joiner<E> j);
  }

  public interface Joiner<E> {

    From<E, CuratorsSampleTsqpEntity> joinSample();

    From<E, CuratorsIntervalEntity> joinInterval();

    From<E, CuratorsCruiseEntity> joinCruise();

    From<E, CuratorsDeviceEntity> joinDevice();

    From<E, CuratorsLegEntity> joinLeg();

    From<E, PlatformMasterEntity> joinPlatform();

    From<E, CuratorsFacilityEntity> joinFacility();

    boolean isJoinedSample();

    boolean isJoinedInterval();

    boolean isJoinedCruise();

    From<E, CuratorsStorageMethEntity> joinStorageMethod();

    From<E, CuratorsProvinceEntity> joinProvince();
  }

  private static class Bbox {


    //value was already validated
    public static Bbox parse(String value) {
      String[] parts = value.split(",");
      return new Bbox(
          Double.parseDouble(parts[0].trim()),
          Double.parseDouble(parts[1].trim()),
          Double.parseDouble(parts[2].trim()),
          Double.parseDouble(parts[3].trim()));
    }

    private final double xMin;
    private final double yMin;
    private final double xMax;
    private final double yMax;

    private Bbox(double xMin, double yMin, double xMax, double yMax) {
      this.xMin = xMin;
      this.yMin = yMin;
      this.xMax = xMax;
      this.yMax = yMax;
    }

    public double getXMin() {
      return xMin;
    }

    public double getYMin() {
      return yMin;
    }

    public double getXMax() {
      return xMax;
    }

    public double getYMax() {
      return yMax;
    }
  }
}
