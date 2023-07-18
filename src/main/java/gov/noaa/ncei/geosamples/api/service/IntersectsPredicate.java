package gov.noaa.ncei.geosamples.api.service;

import java.io.Serializable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.predicate.AbstractSimplePredicate;
import org.locationtech.jts.geom.Geometry;

public class IntersectsPredicate extends AbstractSimplePredicate implements Serializable {

  private final Expression<Geometry> g1;
  private final Expression<Geometry> g2;

  public IntersectsPredicate(CriteriaBuilder criteriaBuilder, Expression<Geometry> g1, Geometry g2) {
    this(criteriaBuilder, g1, new LiteralExpression<Geometry>((CriteriaBuilderImpl) criteriaBuilder, g2));
  }

  public IntersectsPredicate(CriteriaBuilder criteriaBuilder, Expression<Geometry> g1, Expression<Geometry> g2) {
    super((CriteriaBuilderImpl) criteriaBuilder);
    this.g1 = g1;
    this.g2 = g2;
  }

  @Override
  public void registerParameters(ParameterRegistry registry) {

  }

  private static String renderExpression(Expression<Geometry> g, RenderingContext rc) {
    return ((Renderable) g).render(rc);
  }

  @Override
  public String render(boolean isNegated, RenderingContext rc) {
    return String.format(" intersects( %s, %s ) = %b ", renderExpression(g1, rc), renderExpression(g2, rc), !isNegated);
  }
}
