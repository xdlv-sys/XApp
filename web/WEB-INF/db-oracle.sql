drop table t_primary_key;
create table t_primary_key(
    table_name varchar2(32) not null primary key,
    current_id number(8) not null
);
drop table t_user;
create table t_user
(
	id     int primary key, 
	name varchar(15) not null,
	pwd varchar(10),
	mail varchar(20),
	user_role int
);
insert into t_user values(-10,'a','a','a@a.com',0);
insert into t_user values(-9,'b','b','b@b.com',1);

drop table t_userdz;
create table t_userdz
(
	phone	varchar2(15) not null,
	user_name	varchar2(50),
	wxid	varchar2(50)
);

drop table t_user_validate;
create table t_user_validate
(
    phone	varchar2(15) not null,	
    validate_code varchar2(6),
    status char(1),
	validate_time date
);

drop table t_dzlist;
create table t_dzlist
(
	year number(5),
	month number(2),
	impdate date,
	userid Varchar2(15),
	Username Varchar2(200),
	Isok Char(1),
	Qmye Number(12,3),
	zdxsk1 Number(12,3),
	ysdsk1 Number(12,3),
	zdfwk1 Number(12,3),
	jb1 Number(12,3),
	fl1 Number(12,3),
	zdxsk2 Number(12,3),
	jb2 Number(12,3),
	fl2 Number(12,3),
	zdfwk2 Number(12,3),
	qtyfdk2 Number(12,3),
	credit_scope varchar(10)
);

drop table t_user_company;
create table t_user_company
(
	code varchar2(40),
	company_name varchar2(200),
	area varchar2(15),
	business char(1),
	wx_contract_name1 varchar2(20),
	wx_contract_phone1 varchar2(15),
	wx_contract_name2 varchar2(20),
	wx_contract_phone2 varchar2(15),
	manager_name varchar2(20),
	remark_content varchar2(100),
	credit_scope varchar2(10)
);

drop table t_import_dz_record;
create table t_import_dz_record
(
	year SMALLINT not null,
	month SMALLINT not null,
	notification char(1),
	import_date date
);
