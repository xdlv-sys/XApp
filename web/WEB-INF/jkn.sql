drop table IF EXISTS t_jkn_user;
create table t_jkn_user(
  user_id int primary key,
  user_name varchar(60) not null,
  referrer int,
  telephone varchar(16),
  vip tinyint,
  user_level tinyint,
  area_level tinyint,
  count int,
  count_one int,
  count_two int,
  count_three int,
  reg_date timestamp default now()
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_order;
create table t_jkn_order(
  order_id int primary key,
  user_id int not null,
  year smallint,
  month tinyint,
  pay_type tinyint,
  total_fee int,
  trade_status tinyint,
  [] timestamp default now()
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_order_item;
create table t_jkn_order(
  order_item_id int primary key,
  order_id int not null,
  good_id int not null
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_order_item;
create table t_jkn_order(
  order_item_id int primary key,
  order_id int not null,
  good_id int not null
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_account_item;
create table t_jkn_account_item(
  item_id int primary key,
  order_id int not null,
  user_one int,
  user_two int,
  user_three int,
  item_status tinyint,
  last_update TIMESTAMP default now()
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_config;
create table t_jkn_config(
  config_name varchar(32) primary key,
  config_value varchar(512) not null
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_event;
create table t_jkn_event(
  event_id int auto_increment primary key,
  event_type tinyint not null,
  db_key int,
  db_content varchar(32),
  event_status tinyint
)ENGINE = INNODB;

delete from t_mod where id > 0;
insert into t_mod values(1,'用户信息',null,'jkn-UserManager','x-fa fa-users',0);

insert into t_mod values(2,'交易信息',null,'jkn-OrderManager','x-fa fa-reorder',0);
