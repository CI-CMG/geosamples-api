package gov.noaa.ncei.geosamples.api.service.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


public class CsvExportHandler<V> {

  private final ExportContext<V> exportContext;

  public CsvExportHandler(Class<V> viewClass, Class<? extends V> viewClassImpl) {
    this.exportContext = new ExportContext<>(viewClass, viewClassImpl);
  }

  private static void printRecord(CSVPrinter csvPrinter, Object[] row) {
    try {
      csvPrinter.printRecord(row);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to write row", e);
    }
  }

  public void export(OutputStream outputStream, List<V> beans) {

    try (CSVPrinter csvPrinter =
        new CSVPrinter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8),
            CSVFormat.Builder
                .create(CSVFormat.DEFAULT)
                .setHeader(exportContext.getColumns().toArray(new String[0]))
                .build())) {

      beans.stream()
          .map(exportContext::beanToRecord)
          .forEach(r -> printRecord(csvPrinter, r));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to create CSV", e);
    }
  }

}
