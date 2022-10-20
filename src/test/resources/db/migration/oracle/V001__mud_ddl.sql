create sequence CURATORS_SEQ
    nocache
/

create sequence PLATFORM_MASTER_SEQ
    nocache
/

create sequence CURATORS_FACILITY_SEQ
    nocache
/

create sequence CURATORS_CRUISE_SEQ
    nocache
/

create sequence CURATORS_CRUISE_FACILITY_SEQ
    nocache
/

create sequence CURATORS_LEG_SEQ
    nocache
/

create sequence CURATORS_CRUISE_PLATFORM_SEQ
    nocache
/

create sequence CURATORS_SAMPLE_LINKS_SEQ
    nocache
/

create sequence CURATORS_CRUISE_LINKS_SEQ
    nocache
/

create sequence CURATORS_INTERVAL_SEQ
    nocache
/

create sequence GEOSAMPLES_USER_TOKEN_SEQ
    nocache
/

create table CURATORS_FACILITY
(
    INST_CODE        VARCHAR2(3)             not null
        constraint CURATORS_FACILITY_INST_CODE_NN
            check ("INST_CODE" IS NOT NULL),
    FACILITY_CODE    VARCHAR2(10)            not null,
    FACILITY         VARCHAR2(100)           not null
        constraint CURATORS_FACILITY_FACILITY_NN
            check ("FACILITY" IS NOT NULL),
    ADDR_1           VARCHAR2(100),
    ADDR_2           VARCHAR2(45),
    ADDR_3           VARCHAR2(45),
    ADDR_4           VARCHAR2(45),
    CONTACT_1        VARCHAR2(45),
    CONTACT_2        VARCHAR2(45),
    CONTACT_3        VARCHAR2(45),
    EMAIL_LINK       VARCHAR2(45),
    URL_LINK         VARCHAR2(45),
    FTP_LINK         VARCHAR2(45),
    DOI_LINK         VARCHAR2(45),
    FACILITY_COMMENT VARCHAR2(2000),
    PUBLISH          VARCHAR2(1) default 'Y' not null
        constraint CURATORS_FACILITY_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    PREVIOUS_STATE   VARCHAR2(1),
    ID               NUMBER(19)              not null
        constraint CURATORS_FACILITY_PK
            primary key,
    LAST_UPDATE      TIMESTAMP(6)            not null
        constraint CURATORS_FACILITY_LAST_UPDATE_NN
            check ("LAST_UPDATE" IS NOT NULL)
)
/

create or replace trigger CURATORS_FACILITY_BI
    before insert
    on CURATORS_FACILITY
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_FACILITY_SEQ.NEXTVAL;
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/

create or replace trigger CURATORS_FACILITY_BU
    before update
    on CURATORS_FACILITY
    for each row
begin
    if :new.previous_state != :old.publish then
        :new.previous_state := :old.publish;
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/

create table CURATORS_WEATH_META
(
    WEATH_META_CODE VARCHAR2(1),
    WEATH_META      VARCHAR2(30) not null
        constraint CURATORS_WEATH_META_PK
            primary key,
    PUBLISH         VARCHAR2(1) default 'Y',
    PREVIOUS_STATE  VARCHAR2(1),
    SOURCE_URI      VARCHAR2(255)
)
/

create or replace trigger CURATORS_WEATH_META_BI
    before insert
    on CURATORS_WEATH_META
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_WEATH_META_BU
    before update of PUBLISH
    on CURATORS_WEATH_META
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_TEXTURE
(
    TEXTURE_CODE   VARCHAR2(1),
    TEXTURE        VARCHAR2(40) not null
        constraint CURATORS_TEXT_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create or replace trigger CURATORS_TEXTURE_BI
    before insert
    on CURATORS_TEXTURE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_TEXTURE_BU
    before update of PUBLISH
    on CURATORS_TEXTURE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_PROVINCE
(
    PROVINCE_CODE    VARCHAR2(2),
    PROVINCE         VARCHAR2(35) not null
        constraint CURATORS_PROV_PK
            primary key,
    PROVINCE_COMMENT VARCHAR2(40),
    PUBLISH          VARCHAR2(1) default 'Y',
    PREVIOUS_STATE   VARCHAR2(1),
    SOURCE_URI       VARCHAR2(255)
)
/

create or replace trigger CURATORS_PROVINCE_BI
    before insert
    on CURATORS_PROVINCE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_PROVINCE_BU
    before update of PUBLISH
    on CURATORS_PROVINCE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_EXHAUST
(
    EXHAUST_CODE   VARCHAR2(1) not null
        constraint CURATORS_EXHAUST__CODE_PK
            primary key,
    EXHAUST        VARCHAR2(30),
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1)
)
/

create or replace trigger CURATORS_EXHAUST_BI
    before insert
    on CURATORS_EXHAUST
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_EXHAUST_BU
    before update of PUBLISH
    on CURATORS_EXHAUST
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_MUNSELL
(
    MUNSELL_CODE   VARCHAR2(10) not null
        constraint CURATORS_MUN_CODE_PK
            primary key,
    MUNSELL        VARCHAR2(30),
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1)
)
/

create or replace trigger CURATORS_MUNSELL_BI
    before insert
    on CURATORS_MUNSELL
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_MUNSELL_BU
    before update of PUBLISH
    on CURATORS_MUNSELL
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_REMARK
(
    REMARK_CODE    VARCHAR2(1),
    REMARK         VARCHAR2(70) not null
        constraint CURATORS_REMARK_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create or replace trigger CURATORS_REMARK_BI
    before insert
    on CURATORS_REMARK
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_REMARK_BU
    before update of PUBLISH
    on CURATORS_REMARK
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_AGE
(
    AGE_CODE       VARCHAR2(2),
    AGE            VARCHAR2(20) not null
        constraint CURATORS_AGE_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create or replace trigger CURATORS_AGE_BI
    before insert
    on CURATORS_AGE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_AGE_BU
    before update of PUBLISH
    on CURATORS_AGE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_DEVICE
(
    DEVICE_CODE    VARCHAR2(2),
    DEVICE         VARCHAR2(30) not null
        constraint CURATORS_DEVICE_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create or replace trigger CURATORS_DEVICE_BI
    before insert
    on CURATORS_DEVICE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_DEVICE_BU
    before update of PUBLISH
    on CURATORS_DEVICE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table PLATFORM_MASTER
(
    MASTER_ID           NUMBER(8),
    PLATFORM            VARCHAR2(50) not null
        constraint PLATFORM_MASTER_PLATFORM_UK
            unique
        constraint PLATFORM_MASTER_PLATFORM_NN
            check ("PLATFORM" IS NOT NULL),
    DATE_ADDED          DATE,
    PUBLISH             VARCHAR2(1)  not null
        constraint PLATFORM_MASTER_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    PREVIOUS_STATE      VARCHAR2(1),
    PREFIX              VARCHAR2(30),
    ICES_CODE           VARCHAR2(4),
    SOURCE_URI          VARCHAR2(255),
    ID                  NUMBER(19)   not null
        constraint PLATFORM_MASTER_PK
            primary key,
    PLATFORM_NORMALIZED VARCHAR2(50) not null
        constraint PLATFORM_MASTER_PLATFORM_NORMALIZED_UK
            unique
        constraint PLATFORM_MASTER_PLATFORM_NORMALIZED_NN
            check ("PLATFORM_NORMALIZED" IS NOT NULL)
)
/

create or replace trigger PLATFORM_MASTER_BI
    before insert
    on PLATFORM_MASTER
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.PLATFORM_MASTER_SEQ.NEXTVAL;
    end if;
    :new.PLATFORM_NORMALIZED := upper(:new.PLATFORM);
end;
/

create or replace trigger PLATFORM_MASTER_BU
    before update
    on PLATFORM_MASTER
    for each row
begin
    if :new.previous_state != :old.publish then
        :new.previous_state := :old.publish;
    end if;
    :new.PLATFORM_NORMALIZED := upper(:new.PLATFORM);
end;
/

create table CURATORS_ROCK_LITH
(
    ROCK_LITH_CODE VARCHAR2(2),
    ROCK_LITH      VARCHAR2(100) not null
        constraint CURATORS_RLITH_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create or replace trigger CURATORS_ROCK_LITH_BI
    before insert
    on CURATORS_ROCK_LITH
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_ROCK_LITH_BU
    before update of PUBLISH
    on CURATORS_ROCK_LITH
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_STORAGE_METH
(
    STORAGE_METH_CODE VARCHAR2(1),
    STORAGE_METH      VARCHAR2(35) not null
        constraint CURATORS_STORAGE_PK
            primary key,
    PUBLISH           VARCHAR2(1) default 'Y',
    PREVIOUS_STATE    VARCHAR2(1),
    SOURCE_URI        VARCHAR2(255)
)
/

create or replace trigger CURATORS_STORAGE_METH_BI
    before insert
    on CURATORS_STORAGE_METH
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_STORAGE_METH_BU
    before update of PUBLISH
    on CURATORS_STORAGE_METH
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_LITHOLOGY
(
    LITHOLOGY_CODE VARCHAR2(1),
    LITHOLOGY      VARCHAR2(40) not null
        constraint CUR_LITH_PK
            primary key,
    OLD_LITHOLOGY  VARCHAR2(40),
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create or replace trigger CURATORS_LITHOLOGY_BI
    before insert
    on CURATORS_LITHOLOGY
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_LITHOLOGY_BU
    before update of PUBLISH
    on CURATORS_LITHOLOGY
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_ROCK_MIN
(
    ROCK_MIN_CODE  VARCHAR2(1),
    ROCK_MIN       VARCHAR2(35) not null
        constraint CURATORS_MIN_PK
            primary key,
    PUBLISH        VARCHAR2(1) default 'Y',
    PREVIOUS_STATE VARCHAR2(1),
    SOURCE_URI     VARCHAR2(255)
)
/

create or replace trigger CURATORS_ROCK_MIN_BI
    before insert
    on CURATORS_ROCK_MIN
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_ROCK_MIN_BU
    before update of PUBLISH
    on CURATORS_ROCK_MIN
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

create table CURATORS_LAKE
(
    LAKE             VARCHAR2(255),
    CONTINENT        VARCHAR2(30),
    COUNTRY          VARCHAR2(30),
    STATE            VARCHAR2(30),
    COUNTY           VARCHAR2(30),
    ELEVATION        NUMBER(7),
    LAKE_TYPE        VARCHAR2(30),
    HYDROLOGY        VARCHAR2(30),
    SALINITY         VARCHAR2(30),
    TDS              VARCHAR2(30),
    BRINE_TYPE       VARCHAR2(30),
    TROPHIC_STATE    VARCHAR2(30),
    CATCHMENT        VARCHAR2(30),
    SURFACE_AREA     VARCHAR2(30),
    VOLUME           VARCHAR2(10),
    MAX_DEPTH        NUMBER(7),
    MEAN_DEPTH       NUMBER(7),
    SHORELINE_LENGTH NUMBER(10),
    PH               VARCHAR2(10),
    MIXING           VARCHAR2(20),
    PUBLISH          VARCHAR2(1) default 'Y',
    PREVIOUS_STATE   VARCHAR2(1)
)
/

create index CURLAKE_LAKE
    on CURATORS_LAKE (LAKE)
/

create or replace trigger CURATORS_LAKE_BI
    before insert
    on CURATORS_LAKE
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_LAKE_BU
    before update of PUBLISH
    on CURATORS_LAKE
    for each row
begin
    :new.previous_state := :old.publish;
end;
/


create table GEOSAMPLES_USER
(
    USER_NAME    VARCHAR2(200) not null
        constraint GEOSAMPLES_USER_PK
            primary key,
    VERSION      NUMBER(10),
    DISPLAY_NAME VARCHAR2(200)
)
/

create table GEOSAMPLES_AUTHORITY
(
    AUTHORITY_NAME VARCHAR2(100) not null
        constraint GEOSAMPLES_AUTHORITY_PK
            primary key
)
/

create table GEOSAMPLES_USER_AUTHORITY
(
    USER_NAME      VARCHAR2(200) not null
        constraint GEOSAMPLES_USER_AUTHORITY_USER_FK
            references GEOSAMPLES_USER,
    AUTHORITY_NAME VARCHAR2(100) not null
        constraint GEOSAMPLES_USER_AUTHORITY_AUTHORITY_FK
            references GEOSAMPLES_AUTHORITY,
    constraint GEOSAMPLES_USER_AUTHORITY_PK
        primary key (USER_NAME, AUTHORITY_NAME)
)
/

create table CURATORS_CRUISE
(
    ID          NUMBER(19)   not null
        constraint CURATORS_CRUISE_ID_PK
            primary key,
    CRUISE_NAME VARCHAR2(30) not null
        constraint CURATORS_CRUISE_PLATFORM_CRUISE_NAME_NN
            check ("CRUISE_NAME" IS NOT NULL),
    YEAR        NUMBER(5)    not null
        constraint CURATORS_CRUISE_PLATFORM_YEAR_NN
            check ("YEAR" IS NOT NULL),
    PUBLISH     VARCHAR2(1)  not null
        constraint CURATORS_CRUISE_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    constraint CURATORS_CRUISE_CRUISE_NAME_YEAR_UK
        unique (CRUISE_NAME, YEAR)
)
/

create or replace trigger CURATORS_CRUISE_BI
    before insert
    on CURATORS_CRUISE
    for each row
begin
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_CRUISE_SEQ.NEXTVAL;
    end if;
end;
/

create table CURATORS_CRUISE_FACILITY
(
    ID          NUMBER(19)  not null
        constraint CURATORS_CRUISE_FACILITY_PK
            primary key,
    CRUISE_ID   NUMBER(19)  not null
        constraint CURATORS_CRUISE_FACILITY_CRUISE_ID_FK
            references CURATORS_CRUISE
        constraint CURATORS_CRUISE_FACILITY_CRUISE_ID_NN
            check ("CRUISE_ID" IS NOT NULL),
    FACILITY_ID NUMBER(19)  not null
        constraint CURATORS_CRUISE_FACILITY_FACILITY_ID_FK
            references CURATORS_FACILITY
        constraint CURATORS_CRUISE_FACILITY_FACILITY_ID_NN
            check ("FACILITY_ID" IS NOT NULL),
    PUBLISH     VARCHAR2(1) not null
        constraint CURATORS_CRUISE_FACILITY_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    constraint CURATORS_CRUISE_FACILITY_CRUISE_ID_FACILITY_ID_UK
        unique (CRUISE_ID, FACILITY_ID)
)
/

create or replace trigger CURATORS_CRUISE_FACILITY_BI
    before insert
    on CURATORS_CRUISE_FACILITY
    for each row
begin
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_CRUISE_FACILITY_SEQ.NEXTVAL;
    end if;
end;
/

create table CURATORS_LEG
(
    ID        NUMBER(19)   not null
        constraint CURATORS_LEG_ID_PK
            primary key,
    CRUISE_ID NUMBER(19)   not null
        constraint CURATORS_LEG_CRUISE_ID_FK
            references CURATORS_CRUISE
        constraint CURATORS_LEG_CRUISE_ID_NN
            check ("CRUISE_ID" IS NOT NULL),
    LEG_NAME  VARCHAR2(30) not null
        constraint CURATORS_LEG_LEG_NAME_NN
            check ("LEG_NAME" IS NOT NULL),
    PUBLISH   VARCHAR2(1)  not null
        constraint CURATORS_LEG_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    constraint CURATORS_LEG_CRUISE_ID_LEG_NAME_UK
        unique (CRUISE_ID, LEG_NAME)
)
/

create or replace trigger CURATORS_LEG_BI
    before insert
    on CURATORS_LEG
    for each row
begin
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_LEG_SEQ.NEXTVAL;
    end if;
end;
/

create table CURATORS_CRUISE_PLATFORM
(
    ID          NUMBER(19)  not null
        constraint CURATORS_CRUISE_PLATFORM_PK
            primary key,
    CRUISE_ID   NUMBER(19)  not null
        constraint CURATORS_CRUISE_PLATFORM_CRUISE_ID_FK
            references CURATORS_CRUISE
        constraint CURATORS_CRUISE_PLATFORM_CRUISE_ID_NN
            check ("CRUISE_ID" IS NOT NULL),
    PLATFORM_ID NUMBER(19)  not null
        constraint CURATORS_CRUISE_PLATFORM_PLATFORM_ID_FK
            references PLATFORM_MASTER
        constraint CURATORS_CRUISE_PLATFORM_PLATFORM_ID_NN
            check ("PLATFORM_ID" IS NOT NULL),
    PUBLISH     VARCHAR2(1) not null
        constraint CURATORS_CRUISE_PLATFORM_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    constraint CURATORS_CRUISE_PLATFORM_CRUISE_ID_PLATFORM_ID_UK
        unique (CRUISE_ID, PLATFORM_ID)
)
/

create table CURATORS_CRUISE_LINKS
(
    DATALINK           VARCHAR2(500) not null
        constraint CURATORS_CRUISE_LINKS_DATALINK_NN
            check ("DATALINK" IS NOT NULL),
    LINK_LEVEL         VARCHAR2(30)  not null
        constraint CURATORS_CRUISE_LINKS_LINK_LEVEL_NN
            check ("LINK_LEVEL" IS NOT NULL),
    LINK_SOURCE        VARCHAR2(30)  not null
        constraint CURATORS_CRUISE_LINKS_LINK_SOURCE_NN
            check ("LINK_SOURCE" IS NOT NULL),
    LINK_TYPE          VARCHAR2(30)  not null
        constraint CURATORS_CRUISE_LINKS_LINK_TYPE_NN
            check ("LINK_TYPE" IS NOT NULL),
    PUBLISH            VARCHAR2(1)   not null
        constraint CURATORS_CRUISE_LINKS_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    PREVIOUS_STATE     VARCHAR2(1),
    ID                 NUMBER(19)    not null
        constraint CURATORS_CRUISE_LINKS_PK
            primary key,
    CRUISE_PLATFORM_ID NUMBER(19)    not null
        constraint CURATORS_CRUISE_LINKS_CRUISE_PLATFORM_ID_FK
            references CURATORS_CRUISE_PLATFORM
        constraint CURATORS_CRUISE_LINKS_CRUISE_PLATFORM_ID_NN
            check ("CRUISE_PLATFORM_ID" IS NOT NULL),
    LEG_ID             NUMBER(19)
        constraint CURATORS_CRUISE_LINKS_LEG_ID_FK
            references CURATORS_LEG
)
/

create or replace trigger CURATORS_CRUISE_LINKS_BI
    before insert
    on CURATORS_CRUISE_LINKS
    for each row
declare
    leg_cruise_id number(19);
    cruise_id number(19);
begin
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_CRUISE_LINKS_SEQ.NEXTVAL;
    end if;
    if :new.LEG_ID is not null then
        select LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG where :new.LEG_ID = LEG.ID;
        select CP.CRUISE_ID into cruise_id from ${schema_name}.CURATORS_CRUISE_PLATFORM CP where :new.CRUISE_PLATFORM_ID = CP.ID;
        if cruise_id != leg_cruise_id then
            RAISE_APPLICATION_ERROR(-20000, 'leg is not part of cruise');
        end if;
    end if;
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
end;
/

create or replace trigger CURATORS_CRUISE_LINKS_BU
    before update
    on CURATORS_CRUISE_LINKS
    for each row
declare
    leg_cruise_id number(19);
    cruise_id number(19);
begin
    if :new.LEG_ID is not null then
        select LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG where :new.LEG_ID = LEG.ID;
        select CP.CRUISE_ID into cruise_id from ${schema_name}.CURATORS_CRUISE_PLATFORM CP where :new.CRUISE_PLATFORM_ID = CP.ID;
        if cruise_id != leg_cruise_id then
            RAISE_APPLICATION_ERROR(-20000, 'leg is not part of cruise');
        end if;
    end if;
    if :new.previous_state != :old.publish then
        :new.previous_state := :old.publish;
    end if;
end;
/

create table CURATORS_SAMPLE_TSQP
(
    SAMPLE             VARCHAR2(30)
        constraint CURATORS_SAMPLE_TSQP_SAMPLE_NN
            check (SAMPLE is not null),
    DEVICE             VARCHAR2(30)
        constraint CURATORS_SAMPTSQP_DEVICE_FK
            references CURATORS_DEVICE
        constraint CURATORS_SAMPLE_TSQP_DEVICE_NN
            check (DEVICE is not null),
    BEGIN_DATE         VARCHAR2(8),
    END_DATE           VARCHAR2(8),
    LAT                NUMBER(9, 5)  not null
        constraint CURATORS_SAMPLE_LAT_NN
            check ("LAT" IS NOT NULL),
    END_LAT            NUMBER(9, 5),
    LON                NUMBER(10, 5) not null
        constraint CURATORS_SAMPLE_LON_NN
            check ("LON" IS NOT NULL),
    END_LON            NUMBER(10, 5),
    LATLON_ORIG        VARCHAR2(1),
    WATER_DEPTH        NUMBER(5),
    END_WATER_DEPTH    NUMBER(5),
    STORAGE_METH       VARCHAR2(35)
        constraint CURATORS_SAMPTSQP_STORAGE_FK
            references CURATORS_STORAGE_METH,
    CORED_LENGTH       NUMBER(6),
    CORED_LENGTH_MM    NUMBER(2),
    CORED_DIAM         NUMBER(3),
    CORED_DIAM_MM      NUMBER(2),
    PI                 VARCHAR2(255),
    PROVINCE           VARCHAR2(35)
        constraint CURATORS_SAMPTSQP_PROVINCE_FK
            references CURATORS_PROVINCE,
    LAKE               VARCHAR2(50),
    OTHER_LINK         VARCHAR2(500),
    IGSN               VARCHAR2(50)
        constraint CURATORS_SAMPLE_TSQP_IGSN_UK
            unique,
    SAMPLE_COMMENTS    VARCHAR2(2000),
    PUBLISH            VARCHAR2(1)   not null
        constraint CURATORS_SAMPLE_TSQP_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    PREVIOUS_STATE     VARCHAR2(1),
    SHAPE              MDSYS.SDO_GEOMETRY,
    SHOW_SAMPL         VARCHAR2(254),
    IMLGS              VARCHAR2(15)  not null
        constraint CURATORS_SAMPTSQP_PK
            primary key,
    CRUISE_ID          NUMBER(19)    not null
        constraint CURATORS_SAMPLE_TSQP_CRUISE_ID_FK
            references CURATORS_CRUISE
        constraint CURATORS_SAMPLE_TSQP_CRUISE_ID_NN
            check ("CRUISE_ID" IS NOT NULL),
    CRUISE_PLATFORM_ID NUMBER(19)    not null
        constraint CURATORS_SAMPLE_TSQP_CRUISE_PLATFORM_ID_FK
            references CURATORS_CRUISE_PLATFORM
        constraint CURATORS_SAMPLE_TSQP_CRUISE_PLATFORM_ID_NN
            check ("CRUISE_PLATFORM_ID" IS NOT NULL),
    CRUISE_FACILITY_ID NUMBER(19)    not null
        constraint CURATORS_SAMPLE_TSQP_CRUISE_FACILITY_ID_FK
            references CURATORS_CRUISE_FACILITY
        constraint CURATORS_SAMPLE_TSQP_CRUISE_FACILITY_ID_NN
            check ("CRUISE_FACILITY_ID" IS NOT NULL),
    LEG_ID             NUMBER(19)
        constraint CURATORS_SAMPLE_TSQP_LEG_ID_FK
            references CURATORS_LEG,
    LAST_UPDATE        TIMESTAMP(6)  not null
        constraint CURATORS_SAMPLE_TSQP_LAST_UPDATE_NN
            check ("LAST_UPDATE" IS NOT NULL)
)
/

create table CURATORS_SAMPLE_LINKS
(
    DATALINK       VARCHAR2(500) not null
        constraint CURATORS_SAMPLE_LINKS_DATALINK_NN
            check ("DATALINK" IS NOT NULL),
    LINK_LEVEL     VARCHAR2(30)  not null
        constraint CURATORS_SAMPLE_LINKS_LINK_LEVEL_NN
            check ("LINK_LEVEL" IS NOT NULL),
    LINK_SOURCE    VARCHAR2(30)  not null
        constraint CURATORS_SAMPLE_LINKS_LINK_SOURCE_NN
            check ("LINK_SOURCE" IS NOT NULL),
    LINK_TYPE      VARCHAR2(30)  not null
        constraint CURATORS_SAMPLE_LINKS_LINK_TYPE_NN
            check ("LINK_TYPE" IS NOT NULL),
    PUBLISH        VARCHAR2(1)   not null
        constraint CURATORS_SAMPLE_LINKS_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    PREVIOUS_STATE VARCHAR2(1),
    IMLGS          VARCHAR2(15)  not null
        constraint CURATORS_SAMPLE_LINKS_IMLGS_FK
            references CURATORS_SAMPLE_TSQP
        constraint CURATORS_SAMPLE_LINKS_IMLGS_NN
            check ("IMLGS" IS NOT NULL),
    ID             NUMBER(19)    not null
        constraint CURATORS_SAMPLE_LINKS_PK
            primary key
)
/

create or replace trigger CURATORS_SAMPLE_LINKS_BI
    before insert
    on CURATORS_SAMPLE_LINKS
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_SAMPLE_LINKS_SEQ.NEXTVAL;
    end if;
end;
/

create or replace trigger CURATORS_SAMPLE_LINKS_BU
    before update
    on CURATORS_SAMPLE_LINKS
    for each row
begin
    :new.previous_state := :old.publish;
end;
/

insert into MDSYS.USER_SDO_GEOM_METADATA (table_name, column_name, diminfo, srid)
values ('CURATORS_SAMPLE_TSQP', 'SHAPE',
        MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('Longitude', -180, 180, 0.05), MDSYS.SDO_DIM_ELEMENT('Latitude', -90, 90, 0.05)), 4326);


create index CURATORS_SAMPLE_TSQP_SPX
    on CURATORS_SAMPLE_TSQP (SHAPE)
    indextype is MDSYS.SPATIAL_INDEX
/

create or replace trigger CURATORS_SAMPLE_TSQP_BI
    before insert
    on CURATORS_SAMPLE_TSQP
    for each row
declare
    leg_cruise_id number(19);
    cp_cruise_id number(19);
    cf_cruise_id number(19);
begin
    select CP.CRUISE_ID into cp_cruise_id from ${schema_name}.CURATORS_CRUISE_PLATFORM CP
    where :new.CRUISE_PLATFORM_ID = CP.ID;
    if :new.CRUISE_ID != cp_cruise_id then
        RAISE_APPLICATION_ERROR(-20000, 'invalid cruise-platform mapping');
    end if;
    select CF.CRUISE_ID into cf_cruise_id from ${schema_name}.CURATORS_CRUISE_FACILITY CF
    where :new.CRUISE_FACILITY_ID = CF.ID;
    if :new.CRUISE_ID != cf_cruise_id then
        RAISE_APPLICATION_ERROR(-20000, 'invalid cruise-facility mapping');
    end if;
    if :new.LEG_ID is not null then
        select LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG
        where :new.LEG_ID = LEG.ID;
        if :new.CRUISE_ID != leg_cruise_id then
            RAISE_APPLICATION_ERROR(-20000, 'leg is not part of cruise');
        end if;
    end if;
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.IMLGS is null then
        :new.IMLGS := 'imlgs'||lpad(CURATORS_SEQ.NEXTVAL,7,'0');
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/

create or replace trigger CURATORS_SAMPLE_TSQP_BU
    before update
    on CURATORS_SAMPLE_TSQP
    for each row
declare
    leg_cruise_id number(19);
    cp_cruise_id number(19);
    cf_cruise_id number(19);
begin
    select CP.CRUISE_ID into cp_cruise_id from ${schema_name}.CURATORS_CRUISE_PLATFORM CP
    where :new.CRUISE_PLATFORM_ID = CP.ID;
    if :new.CRUISE_ID != cp_cruise_id then
        RAISE_APPLICATION_ERROR(-20000, 'invalid cruise-platform mapping');
    end if;
    select CF.CRUISE_ID into cf_cruise_id from ${schema_name}.CURATORS_CRUISE_FACILITY CF
    where :new.CRUISE_FACILITY_ID = CF.ID;
    if :new.CRUISE_ID != cf_cruise_id then
        RAISE_APPLICATION_ERROR(-20000, 'invalid cruise-facility mapping');
    end if;
    if :new.LEG_ID is not null then
        select LEG.CRUISE_ID into leg_cruise_id from ${schema_name}.CURATORS_LEG LEG
        where :new.LEG_ID = LEG.ID;
        if :new.CRUISE_ID != leg_cruise_id then
            RAISE_APPLICATION_ERROR(-20000, 'leg is not part of cruise');
        end if;
    end if;
    if :new.previous_state != :old.publish then
        :new.previous_state := :old.publish;
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/

create table CURATORS_INTERVAL
(
    INTERVAL          NUMBER(6)    not null
        constraint CURATORS_INTERVAL_INTERVAL_NN
            check ("INTERVAL" IS NOT NULL),
    DEPTH_TOP         NUMBER(6),
    DEPTH_TOP_MM      NUMBER(2),
    DEPTH_BOT         NUMBER(6),
    DEPTH_BOT_MM      NUMBER(2),
    DHCORE_ID         VARCHAR2(30),
    DHCORE_LENGTH     NUMBER(6),
    DHCORE_LENGTH_MM  NUMBER(2),
    DHCORE_INTERVAL   NUMBER(3),
    DTOP_IN_DHCORE    NUMBER(6),
    DTOP_MM_IN_DHCORE NUMBER(2),
    DBOT_IN_DHCORE    NUMBER(6),
    DBOT_MM_IN_DHCORE NUMBER(2),
    LITH1             VARCHAR2(40)
        constraint CURATORS_INTERVAL_LITH1_FK
            references CURATORS_LITHOLOGY,
    TEXT1             VARCHAR2(40)
        constraint CURATORS_INTERVAL_TEXT1_FK
            references CURATORS_TEXTURE,
    LITH2             VARCHAR2(40)
        constraint CURATORS_INTERVAL_LITH2_FK
            references CURATORS_LITHOLOGY,
    TEXT2             VARCHAR2(40)
        constraint CURATORS_INTERVAL_TEXT2_FK
            references CURATORS_TEXTURE,
    COMP1             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP1_FK
            references CURATORS_LITHOLOGY,
    COMP2             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP2_FK
            references CURATORS_LITHOLOGY,
    COMP3             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP3_FK
            references CURATORS_LITHOLOGY,
    COMP4             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP4_FK
            references CURATORS_LITHOLOGY,
    COMP5             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP5_FK
            references CURATORS_LITHOLOGY,
    COMP6             VARCHAR2(40)
        constraint CURATORS_INTERVAL_COMP6_FK
            references CURATORS_LITHOLOGY,
    DESCRIPTION       VARCHAR2(2000),
    AGE               VARCHAR2(20)
        constraint CURATORS_INTERVAL_AGE_FK
            references CURATORS_AGE,
    ABSOLUTE_AGE_TOP  VARCHAR2(50),
    ABSOLUTE_AGE_BOT  VARCHAR2(50),
    WEIGHT            NUMBER(8, 3),
    ROCK_LITH         VARCHAR2(100)
        constraint CURATORS_INTERVAL_RKLITH_FK
            references CURATORS_ROCK_LITH,
    ROCK_MIN          VARCHAR2(35)
        constraint CURATORS_INTERVAL_RKMIN_FK
            references CURATORS_ROCK_MIN,
    WEATH_META        VARCHAR2(30)
        constraint CURATORS_INTERVAL_WEATH_FK
            references CURATORS_WEATH_META,
    REMARK            VARCHAR2(70)
        constraint CURATORS_INTERVAL_REMARK_FK
            references CURATORS_REMARK,
    MUNSELL_CODE      VARCHAR2(10),
    MUNSELL           VARCHAR2(30),
    EXHAUST_CODE      VARCHAR2(1),
    PHOTO_LINK        VARCHAR2(500),
    LAKE              VARCHAR2(50),
    UNIT_NUMBER       VARCHAR2(50),
    INT_COMMENTS      VARCHAR2(2000),
    DHDEVICE          VARCHAR2(50),
    CMCD_TOP          NUMBER(6),
    MMCD_TOP          NUMBER(1),
    CMCD_BOT          NUMBER(6),
    MMCD_BOT          NUMBER(1),
    PUBLISH           VARCHAR2(1)  not null
        constraint CURATORS_INTERVAL_PUBLISH_NN
            check ("PUBLISH" IS NOT NULL),
    PREVIOUS_STATE    VARCHAR2(1),
    IGSN              VARCHAR2(50),
    IMLGS             VARCHAR2(15) not null
        constraint CURATORS_INTERVAL_IMLGS_FK
            references CURATORS_SAMPLE_TSQP
        constraint CURATORS_INTERVAL_IMLGS_NN
            check ("IMLGS" IS NOT NULL),
    LAST_UPDATE       TIMESTAMP(6) not null
        constraint CURATORS_INTERVAL_LAST_UPDATE_NN
            check ("LAST_UPDATE" IS NOT NULL),
    ID                NUMBER(19)   not null
        constraint CURATORS_INTERVAL_PK
            primary key,
    constraint CURATORS_INTERVAL_IMLGS_INTERVAL_UK
        unique (IMLGS, INTERVAL)
)
/

create or replace trigger CURATORS_INTERVAL_BI
    before insert
    on CURATORS_INTERVAL
    for each row
begin
    if :new.publish is null
    then
        :new.publish := 'Y';
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_INTERVAL_SEQ.NEXTVAL;
    end if;
end;
/

create or replace trigger CURATORS_INTERVAL_BU
    before update
    on CURATORS_INTERVAL
    for each row
begin
    if :new.previous_state != :old.publish then
        :new.previous_state := :old.publish;
    end if;
    if :new.LAST_UPDATE is null then
        :new.LAST_UPDATE := current_timestamp;
    end if;
end;
/

create or replace trigger CURATORS_CRUISE_PLATFORM_BI
    before insert
    on CURATORS_CRUISE_PLATFORM
    for each row
begin
    if :new.PUBLISH is null then
        :new.PUBLISH := 'Y';
    end if;
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_CRUISE_PLATFORM_SEQ.NEXTVAL;
    end if;
end;
/

create table GEOSAMPLES_USER_TOKEN
(
    ID          NUMBER(19)     not null
        constraint GEOSAMPLES_USER_TOKEN_PK
            primary key,
    USER_NAME   VARCHAR2(200)  not null
        constraint GEOSAMPLES_USER_TOKEN_USER_ID_FK
            references GEOSAMPLES_USER
        constraint GEOSAMPLES_USER_TOKEN_USER_ID_NN
            check ("USER_NAME" IS NOT NULL),
    TOKEN_ALIAS VARCHAR2(200)  not null
        constraint GEOSAMPLES_USER_TOKEN_TOKEN_ALIAS_NN
            check ("TOKEN_ALIAS" IS NOT NULL),
    TOKEN_HASH  VARCHAR2(1000) not null
        constraint GEOSAMPLES_USER_TOKEN_TOKEN_HASH_UK
            unique
        constraint GEOSAMPLES_USER_TOKEN_TOKEN_HASH_NN
            check ("TOKEN_HASH" IS NOT NULL),
    constraint GEOSAMPLES_USER_TOKEN_USER_ID_TOKEN_ALIAS_UK
        unique (USER_NAME, TOKEN_ALIAS)
)
/
