-- CREATE DATABASE xapp DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
drop table IF EXISTS t_primary_key;
create table t_primary_key(
    table_name varchar(32) not null primary key,
    current_id int not null
)ENGINE = INNODB;

drop table IF EXISTS t_user;
create table t_user(
    id int not null primary key,
    name VARCHAR(50),
    password VARCHAR(32),
    mail varchar(20)
)ENGINE = INNODB;

drop table IF EXISTS t_role;
create table t_role(
    id int not null primary key,
    name VARCHAR(50)
)ENGINE = INNODB;

drop table IF EXISTS t_mod;
create table t_mod(
    id int not null primary key,
    name VARCHAR(50),
    url varchar(50),
    router_id varchar(50),
    addition VARCHAR(50),
    parent_id int
)ENGINE = INNODB;

drop table IF EXISTS t_user_role;
create table t_user_role(
    id int AUTO_INCREMENT primary key,
    user_id int not null,
    role_id int not null
)ENGINE = INNODB;

drop table IF EXISTS t_role_mod;
create table t_role_mod(
    id int AUTO_INCREMENT primary key,
    role_id int not null,
    mod_id int not null
)ENGINE = INNODB;

insert into t_user values(-10,'a','0cc175b9c0f1b6a831c399e269772661','a@a.com');
insert into t_user values(-9,'g','b2f5ff47436671b6e533d8dc3614845d','g@g.com');

insert into t_role values(-2,'administrator');
insert into t_role values(-1,'guest');

insert into t_user_role values(-2,-10,-2);-- administrator
insert into t_user_role values(-1,-9,-1); -- guest

insert into t_mod values(-100,'系统配置',null,null,'x-fa fa-user-secret',0);
insert into t_mod values(-99,'用户管理',null,'user-UserManager','x-fa fa-user',-100);
-- insert into t_mod values(-98,'权限管理',null,'user-RoleManager','x-fa fa-user-secret',-100);
-- insert into t_mod values(-97,'角色管理',null,'user-RoleManager','x-fa fa-user-secret',-100);

insert into t_role_mod values(-2,-1,-100);
insert into t_role_mod values(-1,-1,-99);




