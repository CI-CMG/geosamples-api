package gov.noaa.ncei.geosamples.api.service.csv;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

public class ExportContext<V> {

  private final Map<String, Integer> positions;
  private final List<String> columns;
  private final List<String> fields;
  private final List<Class<?>> classes;
  private final Class<V> viewClass;

  public ExportContext(Class<V> viewClass, Class<? extends V> viewClassImpl) {
    this.viewClass = viewClass;
    Field[] fields = viewClassImpl.getDeclaredFields();
    Map<String, Integer> positions = new HashMap<>();
    Map<Integer, String> columns = new HashMap<>();
    Map<Integer, String> fieldNames = new HashMap<>();
    Map<Integer, Class<?>> classes = new HashMap<>();
    for (Field field : fields) {
      int position = getPosition(field);
      if (position >= 0) {
        String column = getColumn(field);
        columns.put(position, column);
        fieldNames.put(position, field.getName());
        classes.put(position, field.getType());
        positions.put(column, position);
      }
    }
    String[] columnsArray = new String[columns.size()];
    for (Entry<Integer, String> e : columns.entrySet()) {
      columnsArray[e.getKey()] = e.getValue();
    }

    String[] fieldNamesArray = new String[fieldNames.size()];
    for (Entry<Integer, String> e : fieldNames.entrySet()) {
      fieldNamesArray[e.getKey()] = e.getValue();
    }

    Class<?>[] classesArray = new Class<?>[classes.size()];
    for (Entry<Integer, Class<?>> e : classes.entrySet()) {
      classesArray[e.getKey()] = e.getValue();
    }

    this.columns = Collections.unmodifiableList(Arrays.asList(columnsArray));
    this.fields = Collections.unmodifiableList(Arrays.asList(fieldNamesArray));
    this.classes = Collections.unmodifiableList(Arrays.asList(classesArray));
    this.positions = Collections.unmodifiableMap(positions);
  }

  public Map<String, Integer> getPositions() {
    return positions;
  }

  public List<String> getColumns() {
    return columns;
  }

  public List<String> getFields() {
    return fields;
  }

  public List<Class<?>> getClasses() {
    return classes;
  }

  private static int getPosition(Field field) {
    CsvColumn[] annotations = field.getAnnotationsByType(CsvColumn.class);
    if (annotations == null || annotations.length == 0) {
      return -1;
    }
    return annotations[0].order();
  }

  private static String getColumn(Field field) {
    CsvColumn[] annotations = field.getAnnotationsByType(CsvColumn.class);
    if (annotations == null || annotations.length == 0) {
      return null;
    }
    return annotations[0].column();
  }


  public Object[] beanToRecord(V bean) {
    Object[] result = new Object[fields.size()];
    for (int i = 0; i < fields.size(); i++) {
      String field = fields.get(i);
      PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(viewClass, field);
      if (descriptor == null) {
        throw new IllegalStateException("Getter and setter could not be found for: " + field);
      }
      try {
        Object obj = descriptor.getReadMethod().invoke(bean);
        Object recordValue;
        if (obj instanceof Collection) {
          Collection<?> collection = (Collection<?>) obj;
          List<String> values = collection.stream().map(Object::toString).collect(Collectors.toList());
          recordValue = String.join(",", values);
        } else {
          recordValue = obj;
        }
        result[i] = recordValue;
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new IllegalStateException("Unable to parse CSV", e);
      }
    }
    return result;
  }


}
