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

create or replace trigger ${schema_name}.AGE_INTERVAL_ON_INSERT
    before insert on ${schema_name}.AGE_INTERVAL for each row
begin
    if :new.ID is null then
        :new.ID := ${schema_name}.AGE_INTERVAL_ID_SEQ.nextval;
    end if;
end;