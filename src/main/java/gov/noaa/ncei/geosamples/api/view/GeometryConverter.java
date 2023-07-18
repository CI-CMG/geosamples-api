package gov.noaa.ncei.geosamples.api.view;

import edu.colorado.cires.cmg.polarprocessor.PolarProcessor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.shape.jts.JtsGeometry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GeometryConverter implements Converter<String, Geometry>, Serializable {

  private static final Logger LOGGER = LoggerFactory.getLogger(GeometryConverter.class);

  private Geometry handleAnteMeridian(Geometry geometry) {
    return new JtsGeometry(geometry, JtsSpatialContext.GEO, true, false).getGeom();
  }

  private Geometry processPolygon(Polygon polygon, GeometryFactory geometryFactory) {
    return PolarProcessor.splitPolar(polygon, geometryFactory).orElseGet(() -> handleAnteMeridian(polygon));
  }

  private Geometry processMultiPolygon(MultiPolygon multiPolygon, GeometryFactory geometryFactory) {
    List<Polygon> polygons = new ArrayList<>();
    for (int n = 0; n < multiPolygon.getNumGeometries(); n++) {
      Polygon polygon = (Polygon) multiPolygon.getGeometryN(n);
      Geometry split = PolarProcessor.splitPolar(polygon, geometryFactory).orElseGet(() -> handleAnteMeridian(polygon));
      if (split instanceof Polygon) {
        polygons.add((Polygon) split);
      } else if (split instanceof MultiPolygon) {
        for (int i = 0; i < split.getNumGeometries(); i++) {
          polygons.add((Polygon) split.getGeometryN(i));
        }
      }
    }
    return geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[0]));
  }

  private Geometry processGeometryCollection(GeometryCollection collection, GeometryFactory geometryFactory) {
    List<Geometry> geometries = new ArrayList<>();
    for (int n = 0; n < collection.getNumGeometries(); n++) {
      Geometry geometry = collection.getGeometryN(n);
      Geometry split;
      if (geometry instanceof Point) {
        split = geometry;
      } else if (geometry instanceof LineString) {
        split = handleAnteMeridian(geometry);
      } else if (geometry instanceof Polygon) {
        split = PolarProcessor.splitPolar((Polygon) geometry, geometryFactory).orElseGet(() -> handleAnteMeridian(geometry));
      } else if (geometry instanceof MultiPoint) {
        split = geometry;
      } else if (geometry instanceof MultiLineString) {
        split = handleAnteMeridian(geometry);
      } else if (geometry instanceof MultiPolygon) {
        split = processMultiPolygon((MultiPolygon) geometry, geometryFactory);
      } else {
        throw new IllegalStateException("Unable to process nested geometry collection: " + geometry.getClass().toString());
      }

      if (split instanceof GeometryCollection) {
        for (int i = 0; i < split.getNumGeometries(); i++) {
          geometries.add(split.getGeometryN(i));
        }
      } else {
        geometries.add(split);
      }
    }

    return geometryFactory.createGeometryCollection(geometries.toArray(new Geometry[0]));
  }

  private Geometry processGeometry(Geometry geometry, GeometryFactory geometryFactory) {

    Geometry result;

    if (geometry instanceof Point) {
      result = geometry;
    } else if (geometry instanceof LineString) {
      result = handleAnteMeridian(geometry);
    } else if (geometry instanceof Polygon) {
      result = processPolygon((Polygon) geometry, geometryFactory);
    } else if (geometry instanceof MultiPoint) {
      result = geometry;
    } else if (geometry instanceof MultiLineString) {
      result = handleAnteMeridian(geometry);
    } else if (geometry instanceof MultiPolygon) {
      result = processMultiPolygon((MultiPolygon) geometry, geometryFactory);
    } else if (geometry instanceof GeometryCollection) {
      result = processGeometryCollection((GeometryCollection) geometry, geometryFactory);
    } else {
      result = geometry;
    }

    result = fixWindingGeometry(result, geometryFactory);

    return result;

  }

  private Geometry fixWindingGeometry(Geometry result, GeometryFactory geometryFactory) {
    if (result instanceof Polygon) {
      result = fixWinding((Polygon) result, geometryFactory);
    } else if (result instanceof MultiPolygon) {
      result = fixWindingMultiPolygon((MultiPolygon) result, geometryFactory);
    } else if (result instanceof GeometryCollection && result.getGeometryType().equals("GeometryCollection")) {
      result = fixWindingGeometryCollection((GeometryCollection) result, geometryFactory);
    }
    return result;
  }

  private GeometryCollection fixWindingGeometryCollection(GeometryCollection collection, GeometryFactory geometryFactory) {
    List<Geometry> geometries = new LinkedList<>();
    for (int n = 0; n < collection.getNumGeometries(); n++) {
      Geometry geometry = collection.getGeometryN(n);
      if (geometry instanceof Polygon) {
        geometries.add(fixWinding((Polygon) geometry, geometryFactory));
      } else if (geometry instanceof MultiPolygon) {
        geometries.add(fixWindingMultiPolygon((MultiPolygon) geometry, geometryFactory));
      } else {
        geometries.add(geometry);
      }
    }
    return geometryFactory.createGeometryCollection(geometries.toArray(new Geometry[0]));
  }

  private MultiPolygon fixWindingMultiPolygon(MultiPolygon multiPolygon, GeometryFactory geometryFactory) {
    List<Polygon> polygons = new LinkedList<>();
    for (int n = 0; n < multiPolygon.getNumGeometries(); n++) {
      polygons.add(fixWinding((Polygon) multiPolygon.getGeometryN(n), geometryFactory));
    }
    return geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[0]));
  }

  @Override
  public Geometry convert(@NonNull String wkt) {
    if (!StringUtils.hasText(wkt)) {
      return null;
    }
    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    WKTReader wktReader = new WKTReader(geometryFactory);
    try {
      Geometry geometry = processGeometry(wktReader.read(wkt), geometryFactory);
      if (!geometry.isValid()) {
        LOGGER.warn("WKT does not meet the OGC SFT specification, but may still be valid: {}", wkt);
      }
      return geometry;
    } catch (ParseException e) {
      throw new IllegalArgumentException("Unable to parse WKT: " + wkt, e);
    }
  }

  public static Polygon fixWinding(Polygon polygon, GeometryFactory factory) {
    boolean reversed = false;
    LineString ext = polygon.getExteriorRing();
    if (isCC(ext)) {
      ext = ext.reverse();
      reversed = true;
    }
    List<LineString> ints = new ArrayList<>(polygon.getNumInteriorRing());
    for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
      LineString intRing = polygon.getInteriorRingN(i);
      if (!isCC(intRing)) {
        intRing = intRing.reverse();
        reversed = true;
      }
      ints.add(intRing);
    }
    if (!reversed) {
      return polygon;
    }
    return createPolygon(ext, ints, factory, polygon.getUserData());
  }

  public static Polygon createPolygon(LineString ext, List<LineString> ints, GeometryFactory factory, Object userData) {
    LinearRing[] intRings = convertToLinearRings(ints, factory);
    LinearRing extRing = createLinearRing(ext.getCoordinates(), factory, ext.getUserData());
    Polygon result = factory.createPolygon(extRing, intRings);
    result.setUserData(userData);
    return result;
  }

  public static LinearRing[] convertToLinearRings(List<LineString> ints, GeometryFactory factory) {
    LinearRing[] intRings = new LinearRing[ints.size()];
    for (int i = 0; i < ints.size(); i++) {
      intRings[i] = createLinearRing(ints.get(i).getCoordinates(), factory, ints.get(i).getUserData());
    }
    return intRings;
  }

  public static LinearRing createLinearRing(Coordinate[] coordinates, GeometryFactory factory, Object userData) {
    LinearRing result = factory.createLinearRing(coordinates);
    result.setUserData(userData);
    return result;
  }

  // https://en.wikipedia.org/wiki/Curve_orientation
  private static boolean isCC(LineString lineString) {


    Polygon hull = (Polygon) lineString.convexHull();
    LineString ext = hull.getExteriorRing();
    Point first = ext.getStartPoint();
    int start = -1;
    List<Point> points = new ArrayList<>();
    for(int i = 0; i < lineString.getNumPoints() - 1; i++) {
      Point point = lineString.getPointN(i);
      points.add(point);
      if(start < 0 && point.equals(first)) {
        start = i;
      }
    }

    Collections.rotate(points, -start);
    points.add(first);

    Point second = ext.getPointN(1);
    Point third = ext.getPointN(2);
    boolean secondFound = false;
    boolean thirdFound = false;
    for (int i = 1; i < points.size(); i++) {
      Point point = points.get(i);
      if(!secondFound && point.equals(second)) {
        secondFound = true;
      } else if(secondFound && point.equals(third)) {
        thirdFound = true;
        break;
      }
    }

    //hull & linestring go the same direction if true
    //by default JTS hulls go clockwise
    return thirdFound;
  }
}

