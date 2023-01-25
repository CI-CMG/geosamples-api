create table ${schema_name}.AGE_INTERVAL
(
    ID number(19) constraint AGE_INTERVAL_PK
            primary key,
    AGE         varchar2(20) not null
        constraint AGE_INTERVAL_AGE_FK
            references ${schema_name}.CURATORS_AGE,
    INTERVAL_ID number(19) not null
        constraint AGE_INTERVAL_INTERVAL_FK
            references ${schema_name}.CURATORS_INTERVAL
);

create sequence ${schema_name}.AGE_INTERVAL_ID_SEQ;

CREATE OR REPLACE TRIGGER ${schema_name}.AGE_INTERVAL_ON_INSERT
    BEFORE INSERT ON ${schema_name}.AGE_INTERVAL
    FOR EACH ROW
BEGIN
    SELECT ${schema_name}.AGE_INTERVAL_ID_SEQ.nextval
    INTO :new.ID
    FROM dual;
END;