create sequence ${schema_name}.CURATORS_REMARK_ID_SEQ;
alter table ${schema_name}.CURATORS_REMARK
    add (ID number(19));
update ${schema_name}.CURATORS_REMARK
set ID = ${schema_name}.CURATORS_REMARK_ID_SEQ.nextval;

alter table ${schema_name}.CURATORS_INTERVAL
    drop constraint CURATORS_INTERVAL_REMARK_FK;
alter table ${schema_name}.CURATORS_REMARK drop primary key;
alter table ${schema_name}.CURATORS_REMARK
    modify (ID number(19) constraint CURATORS_REMARK_ID_PK primary key);

alter table ${schema_name}.CURATORS_INTERVAL
    add (REMARK_SWP number(19));
update ${schema_name}.CURATORS_INTERVAL
set REMARK_SWP = (select CURATORS_REMARK.ID from CURATORS_REMARK join CURATORS_INTERVAL CI on CURATORS_REMARK.REMARK = CI.REMARK);
alter table ${schema_name}.CURATORS_INTERVAL
    drop column REMARK;
alter table ${schema_name}.CURATORS_INTERVAL
    rename column REMARK_SWP to REMARK;
alter table ${schema_name}.CURATORS_INTERVAL
    modify (REMARK number(19) constraint CURATORS_INTERVAL_REMARK_FK references CURATORS_REMARK);



create or replace trigger ${schema_name}.CURATORS_REMARK_bi
    before insert on ${schema_name}.CURATORS_REMARK for each row
begin
    if :new.ID is null then
        :new.ID := ${schema_name}.CURATORS_REMARK_ID_SEQ.nextval;
    end if;
end;