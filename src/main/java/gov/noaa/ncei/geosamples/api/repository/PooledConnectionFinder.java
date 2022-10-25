package gov.noaa.ncei.geosamples.api.repository;

import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.driver.OracleConnection;
import org.geolatte.geom.codec.db.oracle.DefaultConnectionFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PooledConnectionFinder extends DefaultConnectionFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(PooledConnectionFinder.class);

  @Override
  public Connection find(Connection conn) {
    if (conn instanceof OracleConnection) {
      return conn;
    }
    try {
      if (conn.isWrapperFor(OracleConnection.class)) {
        return conn.unwrap(OracleConnection.class);
      }
    } catch (SQLException e) {
      LOGGER.warn(
          "An error occurred getting Oracle database connection. Will reattempt with fallback method.",
          e);
    }
    return super.find(conn);
  }
}
