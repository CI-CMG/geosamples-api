package gov.noaa.ncei.geosamples.api.service;

import gov.noaa.ncei.geosamples.api.ServiceProperties;
import gov.noaa.ncei.geosamples.api.repository.CuratorsAgeRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsCruiseRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsDeviceRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsFacilityRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsIntervalRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsLithologyRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsProvinceRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsRemarkRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsRockLithRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsRockMinRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsSampleTsqpRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsStorageMethodRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsTextureRepository;
import gov.noaa.ncei.geosamples.api.repository.CuratorsWeathMetaRepository;
import gov.noaa.ncei.geosamples.api.repository.PlatformMasterRepository;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsAgeEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruiseFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsCruisePlatformEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsDeviceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsFacilityEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsIntervalEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLegEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsLithologyEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsProvinceEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRemarkEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockLithEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsRockMinEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsSampleTsqpEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsStorageMethEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsTextureEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.CuratorsWeathMetaEntity;
import gov.noaa.ncei.mgg.geosamples.ingest.jpa.entity.PlatformMasterEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import javax.sql.DataSource;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SpecificationFactoryTestUtils {

  private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

  @Autowired
  private CuratorsCruiseRepository cruiseRepository;
  @Autowired
  private CuratorsSampleTsqpRepository sampleRepository;
  @Autowired
  private CuratorsIntervalRepository intervalRepository;
  @Autowired
  private PlatformMasterRepository platformRepository;
  @Autowired
  private CuratorsFacilityRepository facilityRepository;
  @Autowired
  private CuratorsDeviceRepository deviceRepository;
  @Autowired
  private CuratorsProvinceRepository provinceRepository;
  @Autowired
  private CuratorsStorageMethodRepository storageMethodRepository;
  @Autowired
  private CuratorsAgeRepository ageRepository;
  @Autowired
  private CuratorsLithologyRepository lithologyRepository;
  @Autowired
  private CuratorsRockLithRepository rockLithRepository;
  @Autowired
  private CuratorsTextureRepository textureRepository;
  @Autowired
  private CuratorsRockMinRepository rockMinRepository;
  @Autowired
  private CuratorsWeathMetaRepository weathMetaRepository;
  @Autowired
  private CuratorsRemarkRepository remarkRepository;
  @Autowired
  private DataSource dataSource;
  @Autowired
  private ServiceProperties properties;



  public void loadData() {
    PlatformMasterEntity explorerPlatform = platformRepository.findByPlatform("Explorer").get();
    PlatformMasterEntity fujiPlatform = platformRepository.findByPlatform("Fuji").get();
    PlatformMasterEntity happyPlatform = platformRepository.findByPlatform("Happy").get();
    PlatformMasterEntity loonPlatform = platformRepository.findByPlatform("Loon").get();

    CuratorsFacilityEntity oerFacility = facilityRepository.findByFacilityCode("OER").get();
    CuratorsFacilityEntity whoiFacility = facilityRepository.findByFacilityCode("WHOI").get();
    CuratorsFacilityEntity ldeoFacility = facilityRepository.findByFacilityCode("LDEO").get();
    CuratorsFacilityEntity awiFacility = facilityRepository.findByFacilityCode("AWI").get();

    CuratorsDeviceEntity coreDevice = deviceRepository.findById("core").get();
    CuratorsDeviceEntity probeDevice = deviceRepository.findById("probe").get();
    CuratorsDeviceEntity drillDevice = deviceRepository.findById("drill").get();
    CuratorsDeviceEntity dredgeDevice = deviceRepository.findById("drill").get();

    CuratorsProvinceEntity soundProvince = provinceRepository.findById("sound").get();
    CuratorsProvinceEntity straitProvince = provinceRepository.findById("strait").get();
    CuratorsProvinceEntity beachProvince = provinceRepository.findById("beach").get();
    CuratorsProvinceEntity estuaryProvince = provinceRepository.findById("estuary").get();
    CuratorsProvinceEntity fjordProvince = provinceRepository.findById("fjord").get();
    CuratorsProvinceEntity lagoonProvince = provinceRepository.findById("lagoon").get();
    CuratorsProvinceEntity lakeProvince = provinceRepository.findById("lake").get();

    CuratorsStorageMethEntity frozenStorage = storageMethodRepository.findById("frozen").get();
    CuratorsStorageMethEntity refrigeratedStorage = storageMethodRepository.findById("refrigerated").get();
    CuratorsStorageMethEntity moistStorage = storageMethodRepository.findById("room temperature, moisture sealed").get();
    CuratorsStorageMethEntity dryStorage = storageMethodRepository.findById("room temperature, dry").get();

    CuratorsAgeEntity holoceneAge = ageRepository.findById("Holocene").get();
    CuratorsAgeEntity pleistoceneAge = ageRepository.findById("Pleistocene").get();
    CuratorsAgeEntity cretaceousAge = ageRepository.findById("Cretaceous").get();
    CuratorsAgeEntity jurassicAge = ageRepository.findById("Jurassic").get();
    CuratorsAgeEntity triassicAge = ageRepository.findById("Triassic").get();
    CuratorsAgeEntity devonianAge = ageRepository.findById("Devonian").get();

    CuratorsLithologyEntity terrigenousLith = lithologyRepository.findById("terrigenous").get();
    CuratorsLithologyEntity volcanicsLith = lithologyRepository.findById("volcanics").get();
    CuratorsLithologyEntity manganeseLith = lithologyRepository.findById("manganese").get();
    CuratorsLithologyEntity glauconiteLith = lithologyRepository.findById("glauconite").get();
    CuratorsLithologyEntity zeolitesLith = lithologyRepository.findById("zeolites").get();

    CuratorsRockLithEntity sedimentaryRockLith = rockLithRepository.findById("sedimentary (chemical)").get();
    CuratorsRockLithEntity metamorphicRockLith = rockLithRepository.findById("metamorphic (metamorphosed)").get();
    CuratorsRockLithEntity sedimentsRockLith = rockLithRepository.findById("sediments (unconsolidated)").get();
    CuratorsRockLithEntity igneousRockLith = rockLithRepository.findById("igneous (intrusive/plutonic), gabbro").get();

    CuratorsTextureEntity ashTexture = textureRepository.findById("ash").get();
    CuratorsTextureEntity brecciaTexture = textureRepository.findById("breccia").get();
    CuratorsTextureEntity nodulesTexture = textureRepository.findById("nodules").get();
    CuratorsTextureEntity slabsTexture = textureRepository.findById("slabs").get();
    CuratorsTextureEntity crustsTexture = textureRepository.findById("crusts").get();

    CuratorsRockMinEntity biotiteMineral = rockMinRepository.findById("biotite").get();
    CuratorsRockMinEntity muscoviteMineral = rockMinRepository.findById("muscovite").get();
    CuratorsRockMinEntity garnetMineral = rockMinRepository.findById("garnet").get();
    CuratorsRockMinEntity spinelMineral = rockMinRepository.findById("spinel").get();

    CuratorsWeathMetaEntity moderateWeathering = weathMetaRepository.findById("weathering - moderate").get();
    CuratorsWeathMetaEntity heavyWeathering = weathMetaRepository.findById("weathering - heavy").get();
    CuratorsWeathMetaEntity veryHeavyWeathering = weathMetaRepository.findById("weathering - heavy, very").get();
    CuratorsWeathMetaEntity hydrothermalMetamorphism = weathMetaRepository.findById("metamorphism - hydrothermal").get();
    CuratorsWeathMetaEntity zeoliteMetamorphism = weathMetaRepository.findById("metamorphism - zeolite").get();
    CuratorsWeathMetaEntity greenschistMetamorphism = weathMetaRepository.findById("metamorphism - greenschist").get();

    CuratorsRemarkEntity freshGlass = remarkRepository.findByRemark("some samples exhibit fresh glass").get();

//      Start CRUISE_1
    {
      CuratorsCruiseEntity cruise1 = cruiseRepository.save(buildCruise(
          "CRUISE_1",
          (short) 2020,
          true,
          () -> Collections.singletonList(buildPlatformMapping(explorerPlatform, true)),
          () -> Collections.singletonList(buildFacilityMapping(oerFacility, true)),
          () -> Arrays.asList(buildLeg("CRUISE_1_L1", true), buildLeg("CRUISE_1_L2", true))));

      CuratorsSampleTsqpEntity cruise1Sample1 = sampleRepository.save(buildSample(true, "CRUISE_1_S1", coreDevice, 23.2, 67.1,
          cruise1.getPlatformMappings().get(0),
          cruise1.getFacilityMappings().get(0),
          new SampleSearchableFields()
              .setBeginDate("20200521")
              .setIgsn("cruise1Sample1")
              .setLake("ontario")
              .setProvince(lakeProvince)
              .setStorageMeth(frozenStorage)
              .setWaterDepth(30)
              .setLeg(cruise1.getLegs().stream().filter(l -> l.getLegName().equals("CRUISE_1_L1")).findFirst().orElse(null))
      ));

      intervalRepository.save(buildInterval(true, cruise1Sample1, 1,
          new IntervalSearchableFields()
              .setAge(cretaceousAge)
              .setLith1(zeolitesLith)
              .setLith2(manganeseLith)
              .setRockLith(igneousRockLith)
              .setText1(nodulesTexture)
              .setText2(ashTexture)
              .setRockMin(garnetMineral)
              .setWeathMeta(greenschistMetamorphism)
              .setRemark(freshGlass),
          "igsn_1"
      ));

      intervalRepository.save(buildInterval(true, cruise1Sample1, 2,
          new IntervalSearchableFields()
              .setAge(jurassicAge)
              .setLith1(zeolitesLith)
              .setText1(nodulesTexture)
              .setRockMin(biotiteMineral)
              .setWeathMeta(moderateWeathering)
      ));

      CuratorsSampleTsqpEntity cruise1Sample2 = sampleRepository.save(buildSample(true, "CRUISE_1_S2", coreDevice, 27.0, 55.1,
          cruise1.getPlatformMappings().get(0),
          cruise1.getFacilityMappings().get(0),
          new SampleSearchableFields()
              .setBeginDate("20220101")
              .setIgsn("cruise1Sample2")
              .setLake("ontario")
              .setProvince(lagoonProvince)
              .setStorageMeth(frozenStorage)
              .setWaterDepth(34)
              .setLeg(cruise1.getLegs().stream().filter(l -> l.getLegName().equals("CRUISE_1_L2")).findFirst().orElse(null))
      ));

      intervalRepository.save(buildInterval(true, cruise1Sample2, 1,
          new IntervalSearchableFields()
              .setAge(devonianAge)
              .setLith2(terrigenousLith)
              .setText2(crustsTexture)
              .setComp1(glauconiteLith)
      ));

      intervalRepository.save(buildInterval(false, cruise1Sample2, 2,
          new IntervalSearchableFields()
              .setAge(jurassicAge)
              .setLith1(zeolitesLith)
              .setRockLith(igneousRockLith)
              .setWeathMeta(moderateWeathering)
              .setRockMin(muscoviteMineral),
          "igsn_1"
      ));

      CuratorsSampleTsqpEntity cruise1Sample3 = sampleRepository.save(buildSample(true, "CRUISE_1_S3", coreDevice, 23.2, 67.1,
          cruise1.getPlatformMappings().get(0),
          cruise1.getFacilityMappings().get(0),
          new SampleSearchableFields()
              .setBeginDate("20200525")
              .setIgsn("cruise1Sample3")
              .setLake("superior")
              .setProvince(lakeProvince)
              .setStorageMeth(frozenStorage)
              .setWaterDepth(22)
              .setLeg(cruise1.getLegs().stream().filter(l -> l.getLegName().equals("CRUISE_1_L2")).findFirst().orElse(null))
      ));

      intervalRepository.save(buildInterval(true, cruise1Sample3, 1,
          new IntervalSearchableFields()
              .setLith2(zeolitesLith),
          "igsn_2"
      ));

      intervalRepository.save(buildInterval(true, cruise1Sample3, 2,
          new IntervalSearchableFields()
              .setAge(cretaceousAge)
      ));
    }
//      End CRUISE_1

//      Start CRUISE_2
    {
      CuratorsCruiseEntity cruise2 = cruiseRepository.save(buildCruise(
          "CRUISE_2",
          (short) 2020,
          true,
          () -> Arrays.asList(buildPlatformMapping(fujiPlatform, true), buildPlatformMapping(explorerPlatform, true)),
          () -> Arrays.asList(buildFacilityMapping(whoiFacility, true), buildFacilityMapping(ldeoFacility, true)),
          () -> Arrays.asList(buildLeg("CRUISE_2_L1", true))));

      CuratorsSampleTsqpEntity cruise2Sample1 = sampleRepository.save(buildSample(true, "CRUISE_2_S1", dredgeDevice, -25.2, -50.3,
          cruise2.getPlatformMappings().stream().filter(pm -> pm.getPlatform().equals(explorerPlatform)).findFirst().orElse(null),
          cruise2.getFacilityMappings().stream().filter(fm -> fm.getFacility().equals(ldeoFacility)).findFirst().orElse(null),
          new SampleSearchableFields()
              .setBeginDate("20200611")
              .setIgsn("cruise2Sample1")
              .setStorageMeth(refrigeratedStorage)
              .setWaterDepth(15)
              .setLeg(cruise2.getLegs().stream().filter(l -> l.getLegName().equals("CRUISE_2_L1")).findFirst().orElse(null))
      ));

      intervalRepository.save(buildInterval(true, cruise2Sample1, 1,
          new IntervalSearchableFields()
              .setAge(holoceneAge)
              .setLith1(volcanicsLith)
              .setRockLith(metamorphicRockLith)
              .setText1(slabsTexture)
              .setRockMin(muscoviteMineral)
              .setWeathMeta(hydrothermalMetamorphism)
              .setText2(crustsTexture)
      ));

      intervalRepository.save(buildInterval(false, cruise2Sample1, 2,
          new IntervalSearchableFields()
              .setWeathMeta(greenschistMetamorphism)
      ));

      CuratorsSampleTsqpEntity cruise2Sample2 = sampleRepository.save(buildSample(true, "CRUISE_2_S2", dredgeDevice, -30.0, 66.5,
          cruise2.getPlatformMappings().stream().filter(pm -> pm.getPlatform().equals(fujiPlatform)).findFirst().orElse(null),
          cruise2.getFacilityMappings().stream().filter(fm -> fm.getFacility().equals(whoiFacility)).findFirst().orElse(null),
          new SampleSearchableFields()
              .setBeginDate("20200611")
              .setIgsn("cruise2Sample2")
              .setProvince(beachProvince)
              .setWaterDepth(20)
              .setLake("placid")
              .setLeg(cruise2.getLegs().stream().filter(l -> l.getLegName().equals("CRUISE_2_L1")).findFirst().orElse(null))
      ));

      intervalRepository.save(buildInterval(true, cruise2Sample2, 1,
          new IntervalSearchableFields()
              .setAge(holoceneAge)
              .setLith1(volcanicsLith)
              .setRockLith(metamorphicRockLith)
              .setText1(slabsTexture)
              .setRockMin(muscoviteMineral)
              .setWeathMeta(zeoliteMetamorphism)
              .setComp6(glauconiteLith)
      ));

      intervalRepository.save(buildInterval(false, cruise2Sample2, 2,
          new IntervalSearchableFields()
              .setAge(holoceneAge)
              .setLith1(volcanicsLith)
              .setRockLith(metamorphicRockLith)
              .setText1(slabsTexture)
              .setRockMin(muscoviteMineral)
              .setWeathMeta(zeoliteMetamorphism)
      ));

      CuratorsSampleTsqpEntity cruise2Sample3 = sampleRepository.save(buildSample(false, "CRUISE_2_S3", probeDevice, 21.0, 3.0,
          cruise2.getPlatformMappings().stream().filter(pm -> pm.getPlatform().equals(explorerPlatform)).findFirst().orElse(null),
          cruise2.getFacilityMappings().stream().filter(fm -> fm.getFacility().equals(ldeoFacility)).findFirst().orElse(null),
          new SampleSearchableFields()
              .setBeginDate("20220101")
              .setIgsn("cruise2Sample3")
              .setProvince(lagoonProvince)
              .setStorageMeth(dryStorage)
              .setWaterDepth(65)
              .setLake("placid")
              .setLeg(cruise2.getLegs().stream().filter(l -> l.getLegName().equals("CRUISE_2_L1")).findFirst().orElse(null))
      ));

      intervalRepository.save(buildInterval(true, cruise2Sample3, 1,
          new IntervalSearchableFields()
              .setRockLith(sedimentaryRockLith)
              .setRockMin(muscoviteMineral)
              .setLith1(zeolitesLith)
              .setText2(crustsTexture)
              .setWeathMeta(hydrothermalMetamorphism)
      ));

      intervalRepository.save(buildInterval(false, cruise2Sample3, 2,
          new IntervalSearchableFields()
              .setRockLith(sedimentsRockLith)
              .setRockMin(spinelMineral)


      ));
    }
//      End CRUISE_2

//      Start CRUISE_3
    {
      CuratorsCruiseEntity cruise3 = cruiseRepository.save(buildCruise(
          "CRUISE_3",
          (short) 2022,
          true,
          () -> Collections.singletonList(buildPlatformMapping(happyPlatform, true)),
          () -> Collections.singletonList(buildFacilityMapping(ldeoFacility, true)),
          null));

      CuratorsSampleTsqpEntity cruise3Sample1 = sampleRepository.save(buildSample(true, "CRUISE_3_S1", probeDevice, 10.2, 78.9,
          cruise3.getPlatformMappings().get(0),
          cruise3.getFacilityMappings().get(0),
          new SampleSearchableFields()
              .setBeginDate("20221225")
              .setIgsn("cruise3Sample1")
              .setLake("bear")
              .setProvince(soundProvince)
              .setStorageMeth(moistStorage)
              .setWaterDepth(99)
      ));

      intervalRepository.save(buildInterval(true, cruise3Sample1, 1,
          new IntervalSearchableFields()
              .setAge(triassicAge)
      ));

    }
//      End CRUISE_3

//      Start CRUISE_4
    {
      CuratorsCruiseEntity cruise4 = cruiseRepository.save(buildCruise(
          "CRUISE_4",
          (short) 2021,
          true,
          () -> Collections.singletonList(buildPlatformMapping(loonPlatform, true)),
          () -> Collections.singletonList(buildFacilityMapping(awiFacility, true)),
          null));

      CuratorsSampleTsqpEntity cruise4Sample1 = sampleRepository.save(buildSample(true, "CRUISE_4_S1", drillDevice, 25.4, 5.7,
          cruise4.getPlatformMappings().get(0),
          cruise4.getFacilityMappings().get(0),
          new SampleSearchableFields()
              .setBeginDate("20211225")
              .setIgsn("cruise4Sample1")
              .setLake("great")
              .setProvince(fjordProvince)
              .setStorageMeth(dryStorage)
              .setWaterDepth(5)
      ));

      intervalRepository.save(buildInterval(true, cruise4Sample1, 1,
          new IntervalSearchableFields()
              .setAge(pleistoceneAge)
      ));

    }
//      End CRUISE_4

//      Start CRUISE_5
    {
      CuratorsCruiseEntity cruise5 = cruiseRepository.save(buildCruise(
          "CRUISE_5",
          (short) 2020,
          false,
          () -> Arrays.asList(buildPlatformMapping(loonPlatform, true), buildPlatformMapping(explorerPlatform, true)),
          () -> Collections.singletonList(buildFacilityMapping(oerFacility, true)),
          null));

      CuratorsSampleTsqpEntity cruise5Sample1 = sampleRepository.save(buildSample(true, "CRUISE_5_S1", probeDevice, 20.0, 1.0,
          cruise5.getPlatformMappings().stream().filter(pm -> pm.getPlatform().equals(explorerPlatform)).findFirst().orElse(null),
          cruise5.getFacilityMappings().get(0),
          new SampleSearchableFields()
              .setBeginDate("20220101")
              .setIgsn("cruise5Sample1")
              .setLake("placid")
              .setProvince(lagoonProvince)
              .setStorageMeth(dryStorage)
              .setIgsn("cruise5Sample1")
              .setWaterDepth(55)
      ));

      intervalRepository.save(buildInterval(true, cruise5Sample1, 1,
          new IntervalSearchableFields()
              .setWeathMeta(moderateWeathering)
              .setLith1(zeolitesLith)
              .setText1(crustsTexture)
              .setRockMin(muscoviteMineral)
              .setAge(jurassicAge)
      ));
    }
//      End CRUISE_5

  }

  private static CuratorsLegEntity buildLeg(String name, boolean publish) {
    CuratorsLegEntity leg = new CuratorsLegEntity();
    leg.setLegName(name);
    leg.setPublish(publish);
    return leg;
  }

  private static CuratorsCruisePlatformEntity buildPlatformMapping(PlatformMasterEntity platform, boolean publish) {
    CuratorsCruisePlatformEntity mapping = new CuratorsCruisePlatformEntity();
    mapping.setPlatform(platform);
    mapping.setPublish(publish);
    return mapping;
  }

  private static CuratorsCruiseFacilityEntity buildFacilityMapping(CuratorsFacilityEntity facility, boolean publish) {
    CuratorsCruiseFacilityEntity mapping = new CuratorsCruiseFacilityEntity();
    mapping.setPublish(publish);
    mapping.setFacility(facility);
    return mapping;
  }

  private static CuratorsCruiseEntity buildCruise(String name, short year, boolean publish,
      @Nullable Supplier<List<CuratorsCruisePlatformEntity>> platforms, @Nullable Supplier<List<CuratorsCruiseFacilityEntity>> facilities,
      @Nullable Supplier<List<CuratorsLegEntity>> legs) {
    CuratorsCruiseEntity cruise = new CuratorsCruiseEntity();
    cruise.setCruiseName(name);
    cruise.setYear(year);
    cruise.setPublish(publish);
    if (platforms != null) {
      platforms.get().forEach(cruise::addPlatformMapping);
    }
    if (facilities != null) {
      facilities.get().forEach(cruise::addFacilityMapping);
    }
    if (legs != null) {
      legs.get().forEach(cruise::addLeg);
    }
    return cruise;
  }

  private static class SampleSearchableFields {

    private String lake;
    private String beginDate;
    private String igsn;
    private CuratorsProvinceEntity province;
    private Integer waterDepth;
    private CuratorsStorageMethEntity storageMeth;
    private CuratorsLegEntity leg;

    public String getLake() {
      return lake;
    }

    public SampleSearchableFields setLake(String lake) {
      this.lake = lake;
      return this;
    }

    public String getBeginDate() {
      return beginDate;
    }

    public SampleSearchableFields setBeginDate(String beginDate) {
      this.beginDate = beginDate;
      return this;
    }

    public String getIgsn() {
      return igsn;
    }

    public SampleSearchableFields setIgsn(String igsn) {
      this.igsn = igsn;
      return this;
    }

    public CuratorsProvinceEntity getProvince() {
      return province;
    }

    public SampleSearchableFields setProvince(CuratorsProvinceEntity province) {
      this.province = province;
      return this;
    }

    public Integer getWaterDepth() {
      return waterDepth;
    }

    public SampleSearchableFields setWaterDepth(Integer waterDepth) {
      this.waterDepth = waterDepth;
      return this;
    }

    public CuratorsStorageMethEntity getStorageMeth() {
      return storageMeth;
    }

    public SampleSearchableFields setStorageMeth(CuratorsStorageMethEntity storageMeth) {
      this.storageMeth = storageMeth;
      return this;
    }

    public CuratorsLegEntity getLeg() {
      return leg;
    }

    public SampleSearchableFields setLeg(CuratorsLegEntity leg) {
      this.leg = leg;
      return this;
    }
  }

  private static class IntervalSearchableFields {

    private CuratorsLithologyEntity lith1;
    private CuratorsLithologyEntity lith2;
    private CuratorsTextureEntity text1;
    private CuratorsTextureEntity text2;
    private CuratorsRockMinEntity rockMin;
    private CuratorsWeathMetaEntity weathMeta;
    private CuratorsAgeEntity age;
    private CuratorsRockLithEntity rockLith;
    private CuratorsLithologyEntity comp1;
    private CuratorsLithologyEntity comp2;
    private CuratorsLithologyEntity comp3;
    private CuratorsLithologyEntity comp4;
    private CuratorsLithologyEntity comp5;
    private CuratorsLithologyEntity comp6;
    private CuratorsRemarkEntity remark;

    public CuratorsLithologyEntity getComp1() {
      return comp1;
    }

    public IntervalSearchableFields setComp1(CuratorsLithologyEntity comp1) {
      this.comp1 = comp1;
      return this;
    }

    public CuratorsLithologyEntity getComp2() {
      return comp2;
    }

    public IntervalSearchableFields setComp2(CuratorsLithologyEntity comp2) {
      this.comp2 = comp2;
      return this;
    }

    public CuratorsLithologyEntity getComp3() {
      return comp3;
    }

    public IntervalSearchableFields setComp3(CuratorsLithologyEntity comp3) {
      this.comp3 = comp3;
      return this;
    }

    public CuratorsLithologyEntity getComp4() {
      return comp4;
    }

    public IntervalSearchableFields setComp4(CuratorsLithologyEntity comp4) {
      this.comp4 = comp4;
      return this;
    }

    public CuratorsLithologyEntity getComp5() {
      return comp5;
    }

    public IntervalSearchableFields setComp5(CuratorsLithologyEntity comp5) {
      this.comp5 = comp5;
      return this;
    }

    public CuratorsLithologyEntity getComp6() {
      return comp6;
    }

    public IntervalSearchableFields setComp6(CuratorsLithologyEntity comp6) {
      this.comp6 = comp6;
      return this;
    }

    public CuratorsRemarkEntity getRemark() {
      return remark;
    }

    public IntervalSearchableFields setRemark(CuratorsRemarkEntity remark) {
      this.remark = remark;
      return this;
    }

    public CuratorsLithologyEntity getLith1() {
      return lith1;
    }

    public IntervalSearchableFields setLith1(CuratorsLithologyEntity lith1) {
      this.lith1 = lith1;
      return this;
    }

    public CuratorsLithologyEntity getLith2() {
      return lith2;
    }

    public IntervalSearchableFields setLith2(CuratorsLithologyEntity lith2) {
      this.lith2 = lith2;
      return this;
    }

    public CuratorsTextureEntity getText1() {
      return text1;
    }

    public IntervalSearchableFields setText1(CuratorsTextureEntity text1) {
      this.text1 = text1;
      return this;
    }

    public CuratorsTextureEntity getText2() {
      return text2;
    }

    public IntervalSearchableFields setText2(CuratorsTextureEntity text2) {
      this.text2 = text2;
      return this;
    }

    public CuratorsRockMinEntity getRockMin() {
      return rockMin;
    }

    public IntervalSearchableFields setRockMin(CuratorsRockMinEntity rockMin) {
      this.rockMin = rockMin;
      return this;
    }

    public CuratorsWeathMetaEntity getWeathMeta() {
      return weathMeta;
    }

    public IntervalSearchableFields setWeathMeta(CuratorsWeathMetaEntity weathMeta) {
      this.weathMeta = weathMeta;
      return this;
    }

    public CuratorsAgeEntity getAge() {
      return age;
    }

    public IntervalSearchableFields setAge(CuratorsAgeEntity age) {
      this.age = age;
      return this;
    }

    public CuratorsRockLithEntity getRockLith() {
      return rockLith;
    }

    public IntervalSearchableFields setRockLith(CuratorsRockLithEntity rockLith) {
      this.rockLith = rockLith;
      return this;
    }
  }

  private static CuratorsIntervalEntity buildInterval(boolean publish, CuratorsSampleTsqpEntity sample, int intervalNum,
      IntervalSearchableFields fields) {
    return buildInterval(publish, sample, intervalNum, fields, null);
  }

  private static CuratorsIntervalEntity buildInterval(boolean publish, CuratorsSampleTsqpEntity sample, int intervalNum,
      IntervalSearchableFields fields, String igsn) {
    CuratorsIntervalEntity interval = new CuratorsIntervalEntity();
    interval.setSample(sample);
    interval.setInterval(intervalNum);
    interval.setLith1(fields.getLith1());
    interval.setLith2(fields.getLith2());
    interval.setRockLith(fields.getRockLith());
    interval.setText1(fields.getText1());
    interval.setText2(fields.getText2());
    interval.setRockMin(fields.getRockMin());
    interval.setWeathMeta(fields.getWeathMeta());
    interval.setAges(Collections.singletonList(fields.getAge()));
    interval.setPublish(publish);
    interval.setIgsn(igsn);
    interval.setComp1(fields.getComp1());
    interval.setComp2(fields.getComp2());
    interval.setComp3(fields.getComp3());
    interval.setComp4(fields.getComp4());
    interval.setComp5(fields.getComp5());
    interval.setComp6(fields.getComp6());
    interval.setRemark(fields.getRemark());
    return interval;
  }

  private String resolveSeq() throws SQLException {
    return String.format("%s.%s", properties.getSchema(), "CURATORS_SEQ");
  }

  public static String getImlgs(long objectId) {
    return String.format("imlgs%07d", objectId);
  }

  private long getObjectId() {
    try (Connection connection = dataSource.getConnection()) {
      Dialect dialect = new StandardDialectResolver()
          .resolveDialect(new DatabaseMetaDataDialectResolutionInfoAdapter(connection.getMetaData()));
      try (
          PreparedStatement preparedStatement = connection.prepareStatement(dialect.getSequenceNextValString(resolveSeq()));
          ResultSet resultSet = preparedStatement.executeQuery();
      ) {
        resultSet.next();
        return resultSet.getLong(1);
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Unable to get connection", e);
    }
  }

  private CuratorsSampleTsqpEntity buildSample(
      boolean publish, String name, CuratorsDeviceEntity device,
      double lat, double lon, CuratorsCruisePlatformEntity cruisePlatform, CuratorsCruiseFacilityEntity cruiseFacility,
      SampleSearchableFields fields) {
    CuratorsSampleTsqpEntity sample = new CuratorsSampleTsqpEntity();
    sample.setImlgs(getImlgs(getObjectId()));
    sample.setSample(name);
    sample.setDevice(device);
    sample.setLat(lat);
    sample.setLon(lon);
    sample.setShape(geometryFactory.createPoint(new CoordinateXY(lon, lat)));
    sample.setPublish(publish);
    sample.setCruise(cruisePlatform.getCruise());
    sample.setCruisePlatform(cruisePlatform);
    sample.setCruiseFacility(cruiseFacility);
    sample.setLake(fields.getLake());
    sample.setBeginDate(fields.getBeginDate());
    sample.setWaterDepth(fields.getWaterDepth());
    sample.setIgsn(fields.getIgsn());
    sample.setProvince(fields.getProvince());
    sample.setStorageMeth(fields.getStorageMeth());
    sample.setLeg(fields.getLeg());

    return sample;
  }

}