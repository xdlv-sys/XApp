drop table IF EXISTS t_dl_order;
create table t_dl_order(
  out_trade_no VARCHAR(64) PRIMARY KEY not null,
  trade_no VARCHAR(64),
  user_no VARCHAR(32),
  total_fee float not null,
  pay_status SMALLINT not null DEFAULT 0,
  notify_status SMALLINT not null DEFAULT 0,
  pay_flag SMALLINT not null,
  code_url VARCHAR(128),
  time_stamp TIMESTAMP default now()
)ENGINE = INNODB;