package gov.noaa.ncei.geosamples.api;

import gov.noaa.ncei.geosamples.api.controller.Interval;
import gov.noaa.ncei.geosamples.api.controller.Sample;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TestUtils {

  private static final List<String> TABLES = Arrays.asList(
      "CURATORS_SAMPLE_LINKS",
      "AGE_INTERVAL",
      "CURATORS_INTERVAL",
      "CURATORS_SAMPLE_TSQP",
      "CURATORS_CRUISE_FACILITY",
      "CURATORS_CRUISE_LINKS",
      "CURATORS_LEG",
      "CURATORS_CRUISE_PLATFORM",
      "CURATORS_CRUISE"
  );

  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private TransactionTemplate tx;
  @Autowired
  private ServiceProperties properties;

  private void clearTable(String table) {
    tx.executeWithoutResult(s -> {
      jdbcTemplate.update("delete from " + properties.getSchema() + "." + table);
    });
  }

  public void cleanDb() {
    TABLES.forEach(this::clearTable);
  }

  public void insertCruise(String cruiseName, String publish, int year) {
    tx.executeWithoutResult(ts -> {
      jdbcTemplate.update(
          "insert into " + properties.getCruiseTable() + "(cruise_name, publish, year) values (?, ?, ?)",
          cruiseName, publish, year);
    });
  }

  public void insertCruisePlatform(String cruiseName, int cruiseYear, String platform, String publish) {
    tx.executeWithoutResult(ts -> {
      Long cruiseId = jdbcTemplate.queryForObject("select ID from " + properties.getCruiseTable() + " where cruise_name = ? and year = ?", Long.class,
          cruiseName, cruiseYear);
      Long platformId = jdbcTemplate.queryForObject("select ID from " + properties.getPlatformTable() + " where platform = ?", Long.class, platform);
      jdbcTemplate.update(
          "insert into " + properties.getCruisePlatformTable() + "(CRUISE_ID, PLATFORM_ID, PUBLISH) values (?, ?, ?)",
          cruiseId, platformId, publish);
    });
  }


  public void insertCruiseFacility(String cruiseName, int cruiseYear, String facilityCode, String publish) {
    tx.executeWithoutResult(ts -> {
      Long cruiseId = jdbcTemplate.queryForObject("select ID from " + properties.getCruiseTable() + " where cruise_name = ? and year = ?", Long.class,
          cruiseName, cruiseYear);
      Long facilityId = jdbcTemplate.queryForObject("select ID from " + properties.getFacilityTable() + " where FACILITY_CODE = ?", Long.class,
          facilityCode);
      jdbcTemplate.update(
          "insert into " + properties.getCruiseFacilityTable() + "(CRUISE_ID, FACILITY_ID, PUBLISH) values (?, ?, ?)",
          cruiseId, facilityId, publish);
    });
  }

  public void insertSampleLink(
      String cruiseName,
      int cruiseYear,
      String sample,
      String dataLink,
      String linkLevel,
      String linkSource,
      String linkType,
      String publish) {
    tx.executeWithoutResult(ts -> {
      String imlgs = getImlgs( cruiseName,  cruiseYear, sample);
      jdbcTemplate.update(
          "insert into " + properties.getLinksTable() + "(DATALINK, LINK_LEVEL, LINK_SOURCE, LINK_TYPE, PUBLISH, IMLGS) values (?, ?, ?, ?, ?, ?)",
          dataLink, linkLevel, linkSource, linkType, publish, imlgs);
    });
  }

  public void insertCruiseLink(
      String cruiseName,
      int cruiseYear,
      String platform,
      String dataLink,
      String linkLevel,
      String linkSource,
      String linkType,
      String publish,
      String legName) {
    tx.executeWithoutResult(ts -> {
      Long cruisePlatformId = getCruisePlatformId(cruiseName, cruiseYear, platform);
      Long legId = getLegId(cruiseName, cruiseYear, legName);
      jdbcTemplate.update(
          "insert into " + properties.getCruiseLinksTable() + "(DATALINK, LINK_LEVEL, LINK_SOURCE, LINK_TYPE, PUBLISH, CRUISE_PLATFORM_ID, LEG_ID) values (?, ?, ?, ?, ?, ?, ?)",
          dataLink, linkLevel, linkSource, linkType, publish, cruisePlatformId, legId);
    });
  }

  public void insertLeg(String cruiseName, int cruiseYear, String legName, String publish) {
    tx.executeWithoutResult(ts -> {
      Long cruiseId = jdbcTemplate.queryForObject("select ID from " + properties.getCruiseTable() + " where cruise_name = ? and year = ?", Long.class,
          cruiseName, cruiseYear);
      jdbcTemplate.update(
          "insert into " + properties.getLegTable() + "(CRUISE_ID, LEG_NAME, PUBLISH) values (?, ?, ?)",
          cruiseId, legName, publish);
    });
  }

  public Long getCruiseId(String cruiseName, int cruiseYear) {
    return tx.execute(ts -> {
      return jdbcTemplate.queryForObject("select ID from " + properties.getCruiseTable() + " where cruise_name = ? and year = ?", Long.class,
          cruiseName, cruiseYear);
    });
  }

  private Long getCruisePlatformId(String cruiseName, int cruiseYear, String platform) {
    return tx.execute(ts -> {
      Long cruiseId = getCruiseId(cruiseName, cruiseYear);
      Long platformId = jdbcTemplate.queryForObject("select ID from " + properties.getPlatformTable() + " where platform = ?", Long.class, platform);
      return jdbcTemplate.queryForObject(
          "select ID from " + properties.getCruisePlatformTable() + " where CRUISE_ID = ? and PLATFORM_ID = ?", Long.class, cruiseId, platformId);
    });
  }

  private Long getLegId(String cruiseName, int cruiseYear, String legName) {
    return tx.execute(ts -> {
      Long cruiseId = getCruiseId(cruiseName, cruiseYear);
      return  legName == null ? null
          : jdbcTemplate.queryForObject("select ID from " + properties.getLegTable() + " where CRUISE_ID = ? and LEG_NAME = ?", Long.class, cruiseId,
              legName);
    });
  }

  public Long getFacilityId(String facilityCode)  {
    return jdbcTemplate.queryForObject("select ID from " + properties.getFacilityTable() + " where FACILITY_CODE = ?", Long.class,
        facilityCode);
  }

  public void insertSample(String cruiseName, int cruiseYear, String legName, String facilityCode, String platform, Sample sample) {
    tx.executeWithoutResult(ts -> {
      Long facilityId = getFacilityId(facilityCode);
      Long cruiseId = getCruiseId(cruiseName, cruiseYear);
      Long cruisePlatformId = getCruisePlatformId(cruiseName, cruiseYear, platform);
      Long cruiseFacilityId = jdbcTemplate.queryForObject(
          "select ID from " + properties.getCruiseFacilityTable() + " where CRUISE_ID = ? and FACILITY_ID = ?", Long.class, cruiseId, facilityId);
      Long legId = getLegId(cruiseName, cruiseYear, legName);
      jdbcTemplate.update(
          "insert into " + properties.getSampleTable() +
              "(SAMPLE, DEVICE, BEGIN_DATE, END_DATE, LAT, END_LAT, LON, END_LON, LATLON_ORIG, "
              + "WATER_DEPTH, END_WATER_DEPTH, STORAGE_METH, CORED_LENGTH, CORED_LENGTH_MM, "
              + "CORED_DIAM, CORED_DIAM_MM, PI, PROVINCE, LAKE, OTHER_LINK, IGSN, "
              + "SAMPLE_COMMENTS, PUBLISH, SHOW_SAMPL, "
              + "CRUISE_ID, CRUISE_PLATFORM_ID, CRUISE_FACILITY_ID, LEG_ID) values "
              + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
          sample.getSample(), sample.getDevice(), sample.getBeginDate(), sample.getEndDate(), sample.getLat(), sample.getEndLat(), sample.getLon(),
          sample.getEndLon(), sample.getLatLonOrig(),
          sample.getWaterDepth(), sample.getEndWaterDepth(), sample.getStorageMeth(), sample.getCoredLength(), sample.getCoredLengthMm(),
          sample.getCoredDiam(), sample.getCoredDiamMm(), sample.getPi(), sample.getProvince(), sample.getLake(), sample.getOtherLink(),
          sample.getIgsn(),
          sample.getSampleComments(), sample.getPublish(), sample.getShowSampl(),
          cruiseId, cruisePlatformId, cruiseFacilityId, legId);
    });
  }

  public String getLastUpdate(String imlgs) {
    return tx.execute(ts -> jdbcTemplate.queryForObject("select LAST_UPDATE from " + properties.getSampleTable() + " where IMLGS = ?", Instant.class, imlgs).toString());
  }

  public String getImlgs(String cruiseName, int cruiseYear, String sample) {
    return tx.execute(ts -> {
      Long cruiseId = jdbcTemplate.queryForObject("select ID from " + properties.getCruiseTable() + " where cruise_name = ? and year = ?", Long.class,
          cruiseName, cruiseYear);
      return jdbcTemplate.queryForObject("select IMLGS from " + properties.getSampleTable() + " where SAMPLE = ? and CRUISE_ID = ?", String.class,
          sample, cruiseId);
    });
  }

  public Long getIntervalId(String imlgs, int interval) {
    return tx.execute(ts -> {
      return jdbcTemplate.queryForObject("select ID from " + properties.getIntervalTable() + " where imlgs = ? and interval = ?", Long.class,
          imlgs, interval);
    });
  }

  public void insertInterval(String cruiseName, int cruiseYear, String sample, Interval interval) {
    tx.executeWithoutResult(ts -> {
      String imlgs = getImlgs(cruiseName, cruiseYear, sample);
      jdbcTemplate.update(
          "insert into " + properties.getIntervalTable() +
              "("
              + "IMLGS, "
              + "INTERVAL, "
              + "DEPTH_TOP, "
              + "DEPTH_TOP_MM, "
              + "DEPTH_BOT, "
              + "DEPTH_BOT_MM, "
              + "DHCORE_ID, "
              + "DHCORE_LENGTH, "
              + "DHCORE_LENGTH_MM, "
              + "DHCORE_INTERVAL, "
              + "DTOP_IN_DHCORE, "
              + "DTOP_MM_IN_DHCORE, "
              + "DBOT_IN_DHCORE, "
              + "DBOT_MM_IN_DHCORE, "
              + "LITH1, "
              + "TEXT1, "
              + "LITH2, "
              + "TEXT2, "
              + "COMP1, "
              + "COMP2, "
              + "COMP3, "
              + "COMP4, "
              + "COMP5, "
              + "COMP6, "
              + "DESCRIPTION, "
              + "ABSOLUTE_AGE_TOP, "
              + "ABSOLUTE_AGE_BOT, "
              + "WEIGHT, "
              + "ROCK_LITH, "
              + "ROCK_MIN, "
              + "WEATH_META, "
              + "REMARK, "
              + "MUNSELL_CODE, "
              + "MUNSELL, "
              + "EXHAUST_CODE, "
              + "PHOTO_LINK, "
              + "LAKE, "
              + "UNIT_NUMBER, "
              + "INT_COMMENTS, "
              + "DHDEVICE, "
              + "CMCD_TOP, "
              + "MMCD_TOP, "
              + "CMCD_BOT, "
              + "MMCD_BOT, "
              + "PUBLISH, "
              + "IGSN "
              + ") values "
              + "("
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?, "
              + "?"
              + ")",
          imlgs,

          interval.getInterval(),
          interval.getDepthTop(),
          interval.getDepthTopMm(),
          interval.getDepthBot(),
          interval.getDepthBotMm(),
          interval.getDhCoreId(),
          interval.getDhCoreLength(),
          interval.getDhCoreLengthMm(),
          interval.getDhCoreInterval(),
          interval.getdTopInDhCore(),
          interval.getdTopMmInDhCore(),
          interval.getdBotInDhCore(),
          interval.getdBotMmInDhCore(),
          interval.getLith1(),
          interval.getText1(),
          interval.getLith2(),
          interval.getText2(),
          interval.getComp1(),
          interval.getComp2(),
          interval.getComp3(),
          interval.getComp4(),
          interval.getComp5(),
          interval.getComp6(),
          interval.getDescription(),
          interval.getAbsoluteAgeTop(),
          interval.getAbsoluteAgeBot(),
          interval.getWeight(),
          interval.getRockLith(),
          interval.getRockMin(),
          interval.getWeathMeta(),
          interval.getRemark(),
          interval.getMunsellCode(),
          interval.getMunsell(),
          interval.getExhaustCode(),
          interval.getPhotoLink(),
          interval.getLake(),
          interval.getUnitNumber(),
          interval.getIntComments(),
          interval.getDhDevice(),
          interval.getCmcdTop(),
          interval.getMmcdTop(),
          interval.getCmcdBot(),
          interval.getMmcdBot(),
          interval.getPublish(),
          interval.getIgsn()
      );
      List<String> ages = interval.getAges();
      if (ages != null && !ages.isEmpty()) {
        for (String age : ages) {
          jdbcTemplate.update(
              "insert into " + properties.getAgeIntervalJoinTable()
              + "("
                + "AGE, "
                + "INTERVAL_ID "
              + ") values "
              + "("
                  + "?, "
                  + "? "
              + ")",
              age,
              getIntervalId(imlgs, interval.getInterval())
          );
        }
      }
    });
  }

  public void createBasicCruise(String cruiseName, int year, String platform, String facility, String leg1, String leg2) {
    insertCruise(cruiseName, "Y", year);
    insertCruisePlatform(cruiseName, year, platform, "Y");
    insertCruiseFacility(cruiseName, year, facility, "Y");
    if (leg1 != null) {
      insertLeg(cruiseName, year, leg1, "Y");
    }
    if (leg2 != null) {
      insertLeg(cruiseName, year, leg2, "Y");
    }

  }

}
