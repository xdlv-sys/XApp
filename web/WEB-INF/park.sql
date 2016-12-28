drop table IF EXISTS t_park_info;
create table t_park_info(
    park_id VARCHAR(32) PRIMARY KEY not null,
    park_name VARCHAR (32),
    app_id VARCHAR(32) ,
    secret VARCHAR(32) ,
    mch_id VARCHAR (32),
    wx_key VARCHAR(32),
    limit_pay VARCHAR (32) DEFAULT 'no_credit',
    partner_id VARCHAR(32),
    ali_app_id VARCHAR (32),
    ali_sha_rsa_key VARCHAR(1024),
    proxy_state SMALLINT DEFAULT 0,
    free_count int DEFAULT -1,
    proxy_version int DEFAULT 0,
    last_update TIMESTAMP default now()
)ENGINE = INNODB;


insert into t_park_info(park_id, app_id, secret, mch_id,wx_key,partner_id,ali_app_id,ali_sha_rsa_key)  values('001'
  ,'wxc2b584fcefc93605','5bf062b4754ac5f05783a8804977958e','1360170902','njfzcrjyxgs201606292120101234567'
  ,'2088421331911805','2016070301577028','MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALrtH2L7OgX9cruVzqvRp3ASYF0KDBBt34Dksn/3wgLHqy3doh6/aKDKMmu0aHzpwk03ymg5fLuA7BrfEZPe0wdpIyy/K9IVQ9WcwutKXeJ/vfAHR6YImLsEHeF9xKtS9DvDW0XirJ9JUL2dnrDvLqLF27A6PCHW1Eyv9i5cRxJrAgMBAAECgYBDOtOmEvtsehqQ0zGQ5IfXLBTSgbGTGyFex2JX2jFCNLQhe+w6KNeltPNrf2hxPNXwVdeLotl0ysqgY4h76ZF2JHVTivePKejgeRn0Gb8tlr+fKEkF+aVBUI9dzXm3ITkCpseu8UtpCq/ZfoATeCkhCKTZw8G6alxAiQIbHtV2GQJBAODGLh8I3gp6v2H5hwN5+5N+Et1pGyTb34vqbA7Et+DPHNCgO3geNqK42dCwTJsycYs+vsmH76NVjdsr84QFhk0CQQDU5O2t80tvFaa+ZK1iXXPXstOYoDWlkWdWQ7PaM4AovD1Ob4SGROvC5dMzj0MwbmQ713IiWUSAKkVv6zDlm8eXAkAwb0P8+AIwq+aVjBllzGFDlQUnpMBuntp64dbLD+S3kqmY4w4tggUv5zk4WOUJBEOnR6wA7UPcJFFfCwd8SVx5AkBYrtRjdcmiiH5hKUcghpVO8Os71OEEC0HkqWcuKe3lCiTvm3y1AdjD40DLZY4zioiudNzSeiUSzokGSg6gVvgZAkBJAUGxNue1q3lh0KQK4LWRMTvIYUL0PRAxoLfn7qvPUJ/aUBwsDqFEKkiB4bU1e84nWauzYzwvENSWKfreNEl/');

  insert into t_park_info(park_id, app_id, secret, mch_id,wx_key,partner_id,ali_app_id,ali_sha_rsa_key)  values('111'
  ,'wxc2b584fcefc93605','5bf062b4754ac5f05783a8804977958e','1360170902','njfzcrjyxgs201606292120101234567'
  ,'2088421331911805','2016070301577028','MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALrtH2L7OgX9cruVzqvRp3ASYF0KDBBt34Dksn/3wgLHqy3doh6/aKDKMmu0aHzpwk03ymg5fLuA7BrfEZPe0wdpIyy/K9IVQ9WcwutKXeJ/vfAHR6YImLsEHeF9xKtS9DvDW0XirJ9JUL2dnrDvLqLF27A6PCHW1Eyv9i5cRxJrAgMBAAECgYBDOtOmEvtsehqQ0zGQ5IfXLBTSgbGTGyFex2JX2jFCNLQhe+w6KNeltPNrf2hxPNXwVdeLotl0ysqgY4h76ZF2JHVTivePKejgeRn0Gb8tlr+fKEkF+aVBUI9dzXm3ITkCpseu8UtpCq/ZfoATeCkhCKTZw8G6alxAiQIbHtV2GQJBAODGLh8I3gp6v2H5hwN5+5N+Et1pGyTb34vqbA7Et+DPHNCgO3geNqK42dCwTJsycYs+vsmH76NVjdsr84QFhk0CQQDU5O2t80tvFaa+ZK1iXXPXstOYoDWlkWdWQ7PaM4AovD1Ob4SGROvC5dMzj0MwbmQ713IiWUSAKkVv6zDlm8eXAkAwb0P8+AIwq+aVjBllzGFDlQUnpMBuntp64dbLD+S3kqmY4w4tggUv5zk4WOUJBEOnR6wA7UPcJFFfCwd8SVx5AkBYrtRjdcmiiH5hKUcghpVO8Os71OEEC0HkqWcuKe3lCiTvm3y1AdjD40DLZY4zioiudNzSeiUSzokGSg6gVvgZAkBJAUGxNue1q3lh0KQK4LWRMTvIYUL0PRAxoLfn7qvPUJ/aUBwsDqFEKkiB4bU1e84nWauzYzwvENSWKfreNEl/');

drop table IF EXISTS t_pay_order;
create table t_pay_order(
  out_trade_no VARCHAR(32) PRIMARY KEY not null,
  trade_no VARCHAR(32),
  park_id VARCHAR(32) not null,
  car_number VARCHAR (16),
  total_fee float,
  pay_status SMALLINT DEFAULT 0,
  notify_status SMALLINT DEFAULT 0,
  pay_flag SMALLINT,
  watch_id VARCHAR(32),
  car_type tinyint,
  time_stamp TIMESTAMP default now()
)ENGINE = INNODB;

drop table IF EXISTS t_wx_user;
create table t_wx_user(
  trade_no int AUTO_INCREMENT primary key,
  openid VARCHAR(32),
  car_number VARCHAR(16) not null,
  time_stamp TIMESTAMP
)ENGINE = INNODB;

drop table IF EXISTS t_notify_proxy;
create table t_notify_proxy(
  out_trade_no VARCHAR(32) PRIMARY KEY not null,
  park_id VARCHAR(32) not null,
  car_number VARCHAR(16) not null,
  total_fee float not null,
  status SMALLINT DEFAULT 0,
  try_count SMALLINT DEFAULT 0,
  time_stamp TIMESTAMP DEFAULT now()
)ENGINE = INNODB;

delete from t_mod where id > 0;
insert into t_mod values(1, '停车管理',null,null,'x-fa fa-car',0);
insert into t_mod values(2, '停车场管理',null,'park-ParkManager','x-fa fa-cube',1);
insert into t_mod values(3, '订单管理',null,'park-OrderManager','x-fa fa-reorder',1);

-- --
alter table t_park_info add column `ali_public_key` varchar(512) after `ali_sha_rsa_key`;
update t_park_info set ali_public_key='MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB' where park_id in('001','111','112');

-- -------
insert into t_dynamic_conf(conf_name, conf_value, conf_desc, dirty) VALUE ('proxy_pic_scale','0.13','代理进场图片清晰度',0);
-- ---
alter table t_pay_order add column `s_id` VARCHAR(32) after `car_type`;
alter table t_pay_order add column `enter_time` VARCHAR(32) after `s_id`;

-- charge manager --
insert into t_mod values(4, '充值管理',null,'park-ChargeManager','x-fa fa-bars',1);

drop table IF EXISTS t_charge;
create table t_charge(
  out_trade_no VARCHAR(32) PRIMARY KEY not null,
  trade_no VARCHAR(32),
  park_id VARCHAR(32) not null,
  total_fee float,
  car_number varchar(32),
  room_number varchar(32),
  car_ports varchar(256),
  months TINYINT,
  user_name varchar(12),
  pay_status SMALLINT DEFAULT 0,
  notify_status SMALLINT DEFAULT 0,
  pay_flag SMALLINT,
  time_stamp TIMESTAMP default now()
)ENGINE = INNODB;