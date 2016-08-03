drop table IF EXISTS t_jkn_user;
create table t_jkn_user(
  user_id int primary key,
  user_name varchar(60) not null,
  referrer int,
  telephone varchar(16),
  vip tinyint,
  user_level tinyint,
  area_level tinyint,
  consumed_count int default 0,
  count int default 0,
  count_one int default 0,
  count_two int default 0,
  count_three int default 0,
  reg_date timestamp default now()
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_order;
create table t_jkn_order(
  order_id int primary key,
  user_id int not null,
  year smallint,
  month tinyint,
  pay_type tinyint,
  trade_type tinyint,
  total_fee int,
  balance_fee int,
  trade_status tinyint,
  last_date timestamp default now()
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_order_item;
create table t_jkn_order_item(
  order_item_id int primary key,
  order_id int not null,
  good_id int not null
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_order_settlement;
create table t_jkn_order_settlement(
  order_id int  primary key,
  user_id int not null,
  user_id_one int,
  count_one int,
  user_id_two int,
  count_two int,
  user_id_three int,
  count_three int,
  settlement_status tinyint,
  last_date timestamp default now()
)ENGINE = INNODB;

drop table IF EXISTS t_jkn_order_item;
create table t_jkn_order_item(
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
  event_id int primary key,
  event_type tinyint not null,
  db_key int,
  db_int int,
  db_content varchar(32),
  event_status tinyint,
  try_count tinyint,
  trigger_date DATETIME not null
)ENGINE = INNODB;

delete from t_mod where id > 0;
insert into t_mod values(1,'用户信息',null,'jkn-UserManager','x-fa fa-users',0);
insert into t_mod values(2,'交易信息',null,'jkn-OrderManager','x-fa fa-reorder',0);
insert into t_mod values(3,'事件管理',null,'jkn-EventManager','x-fa fa-bar-chart',0);
insert into t_mod values(4,'结算明细',null,'jkn-Settlement','x-fa fa-dedent',0);


INSERT INTO xapp.t_jkn_user (user_id, user_name, referrer, telephone, vip, user_level, area_level, count, count_one, count_two, count_three, reg_date)
VALUES (1, 'a1', null, '123', 0, 2, 0, 0, 0, 0, 0, '2016-08-02 10:10:48');
INSERT INTO xapp.t_jkn_user (user_id, user_name, referrer, telephone, vip, user_level, area_level, count, count_one, count_two, count_three, reg_date)
VALUES (2, 'a2', 1, '1234', 0, 2, 0, 0, 0, 0, 0, '2016-08-02 10:27:49');
INSERT INTO xapp.t_jkn_user (user_id, user_name, referrer, telephone, vip, user_level, area_level, count, count_one, count_two, count_three, reg_date)
VALUES (3, 'a3', 2, '12345', 0, 1, 0, 0, 0, 0, 0, '2016-08-02 10:28:31');

insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('batch_event','100','批量抓取事件个数',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('gold_ucn','1','升级成黄金会员直推人数',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('gold_acn','0','升级成黄金会员伞下人数',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('white_ucn','10','升级成白金会员直推人数',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('white_acn','0','升级成白金会员伞下人数',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('diamond_ucn','10','升级成钻石会员直推人数',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('diamond_acn','30','升级成钻石会员伞下人数',0);

insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('membership_count','5900','成为会员最低消费单位（分）',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('vip_cost','59000','VIP最低消费单位（分）',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('settlement_one','0.07','一代分成比例',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('settlement_two','0.09','二代分成比例',0);
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('settlement_three','0.11','三代分成比例',0);

insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('settlement_period','60000','预算期时长（单位毫秒）',0);

