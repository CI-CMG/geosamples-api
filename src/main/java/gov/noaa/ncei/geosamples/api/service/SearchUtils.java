package gov.noaa.ncei.geosamples.api.service;


import java.util.Locale;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public class SearchUtils {

  public static <E> Specification<E> and(@Nullable Specification<E> a, @Nullable Specification<E> b) {
    if (a == null) {
      return b;
    }
    return a.and(b);
  }

  public static <E> Specification<E> or(@Nullable Specification<E> a, @Nullable Specification<E> b) {
    if (a == null) {
      return b;
    }
    return a.or(b);
  }

  public static <E> Specification<E> distinct(@Nullable Specification<E> spec) {
    if (spec == null) {
      return (root, query, criteriaBuilder) -> {
        query.distinct(true);
        return null;
      };
    }
    return (root, query, criteriaBuilder) -> {
      query.distinct(true);
      return spec.toPredicate(root, query, criteriaBuilder);
    };
  }

  public static String escape(String s) {
    return s.replaceAll("/", "//").replaceAll("_", "/_").replaceAll("%", "/%");
  }

  public static String contains(String s) {
    return "%" + escape(s) + "%";
  }

  public static String startsWith(String s) {
    return escape(s) + "%";
  }

  public static <E> Specification<E> startsWithIgnoreCase(String searchValue, String columnAlias) {
    return startsWithIgnoreCase(searchValue, e -> e.get(columnAlias));
  }

  public static <E> Specification<E> startsWithIgnoreCase(String searchValue, Function<Root<E>, Expression<String>> columnAlias) {
    return (e, cq, cb) -> startsWithIgnoreCase(cb,  searchValue, columnAlias.apply(e));
  }

  public static <E> Predicate startsWithIgnoreCase( CriteriaBuilder cb, String searchValue, Expression<String> columnAlias) {
    return cb.like(cb.lower(columnAlias), startsWith(searchValue.toLowerCase(Locale.ENGLISH)), '/');
  }


  public static <E> Specification<E> containsIgnoreCase(String searchValue, String columnAlias) {
    return containsIgnoreCase(searchValue, e -> e.get(columnAlias));
  }

  public static <E> Specification<E> containsIgnoreCase(String searchValue, Function<Root<E>, Expression<String>> columnAlias) {
    return (e, cq, cb) -> cb.like(cb.lower(columnAlias.apply(e)), contains(searchValue.toLowerCase(Locale.ENGLISH)), '/');
  }

  public static <E> Specification<E> equalIgnoreCase(String value, Function<Root<E>, Expression<String>> columnAlias) {
    return (e, cq, cb) -> equalIgnoreCase(e, cb, value, columnAlias);
  }

  public static Predicate equalIgnoreCase(CriteriaBuilder cb, String value, Expression<String> columnAlias) {
    return cb.equal(cb.lower(columnAlias), value.toLowerCase(Locale.ENGLISH));
  }

  public static <E> Predicate equalIgnoreCase(Root<E> e, CriteriaBuilder cb, String value, Function<Root<E>, Expression<String>> columnAlias) {
    return cb.equal(cb.lower(columnAlias.apply(e)), value.toLowerCase(Locale.ENGLISH));
  }

  public static <E, Y extends Comparable<? super Y>> Predicate greaterThanOrEqualPredicate(Root<E> e, CriteriaBuilder cb, Y value, Expression<Y> columnAlias) {
    return cb.greaterThanOrEqualTo(columnAlias, value);
  }


  public static <E, Y extends Comparable<? super Y>> Predicate greaterThanOrEqualPredicate(Root<E> e, CriteriaBuilder cb, Y value,
      Function<Root<E>, Expression<Y>> columnAlias) {
    return cb.greaterThanOrEqualTo(columnAlias.apply(e), value);
  }

  public static <E, Y extends Comparable<? super Y>> Specification<E> greaterThanOrEqual(Y searchValue, String columnAlias) {
    return greaterThanOrEqual(searchValue, s -> s.get(columnAlias));
  }

  public static <E, Y extends Comparable<? super Y>> Specification<E> greaterThanOrEqual(Y searchValue,
      Function<Root<E>, Expression<Y>> columnAlias) {
    return (e, cq, cb) -> greaterThanOrEqualPredicate(e, cb, searchValue, s -> columnAlias.apply(s));
  }

  public static <E, Y extends Comparable<? super Y>> Predicate lessThanOrEqualPredicate(Root<E> e, CriteriaBuilder cb, Y value,
      Function<Root<E>, Expression<Y>> columnAlias) {
    return cb.lessThanOrEqualTo(columnAlias.apply(e), value);
  }

  public static <E, Y extends Comparable<? super Y>> Specification<E> lessThanOrEqual(Y searchValue, String columnAlias) {
    return lessThanOrEqual(searchValue, s -> s.get(columnAlias));
  }

  public static <E, Y extends Comparable<? super Y>> Specification<E> lessThanOrEqual(Y searchValue, Function<Root<E>, Expression<Y>> columnAlias) {
    return (e, cq, cb) -> lessThanOrEqualPredicate(e, cb, searchValue, s -> columnAlias.apply(s));
  }

  public static <E> Specification<E> equalIgnoreCase(String value, String columnAlias) {
    return equalIgnoreCase(value, e -> e.get(columnAlias));
  }

  public static <E, C> Predicate equalOne(Root<E> e, CriteriaBuilder cb, C value, Function<Root<E>, Expression<String>> columnAlias) {
    return cb.equal(columnAlias.apply(e), value);
  }

  public static <E, C> Specification<E> equal(C value, Function<Root<E>, Expression<String>> columnAlias) {
    return (e, cq, cb) -> equalOne(e, cb, value, columnAlias);
  }

  public static <E, C> Specification<E> equal(C value, String columnAlias) {
    return equal(value, e -> e.get(columnAlias));
  }

}
