-- CREATE DATABASE xapp DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
drop table IF EXISTS t_primary_key;
create table t_primary_key(
    table_name varchar(32) not null primary key,
    current_id int not null
);

drop table IF EXISTS T_User;
create table T_User(
    id int not null primary key,
    name VARCHAR(50),
    password VARCHAR(20),
    mail varchar(20)
);

drop table IF EXISTS T_Role;
create table T_Role(
    id int not null primary key,
    name VARCHAR(50)
);

drop table IF EXISTS T_Mod;
create table T_Mod(
    id int not null primary key,
    name VARCHAR(50),
    url varchar(50),
    routerId varchar(50),
    addition VARCHAR(50),
    parentId int
);

drop table IF EXISTS T_UserRole;
create table T_UserRole(
    id int not null primary key,
    userId int not null,
    roleId int not null
);

drop table IF EXISTS T_RoleMod;
create table T_RoleMod(
    id int not null primary key,
    roleId int not null,
    modId int not null
);

insert into t_user values(-10,'a','a','a@a.com');
insert into t_user values(-9,'g','g','g@g.com');

insert into t_role values(-2,'administrator');
insert into t_role values(-1,'guest');

insert into t_userrole values(-2,-10,-2);-- administrator
insert into t_userrole values(-1,-9,-1); -- guest

insert into t_mod values(-100,'系统配置',null,null,'x-fa fa-user',0);
insert into t_mod values(-99,'用户管理',null,'user-UserManager','x-fa fa-user',-100);
insert into t_mod values(-98,'权限管理',null,'user-RoleManager','x-fa fa-user-secret',-100);
insert into t_mod values(-97,'角色管理',null,'user-RoleManager','x-fa fa-user-secret',-100);

insert into t_rolemod values(-2,-1,-100);
insert into t_rolemod values(-1,-1,-99);


select * from t_role r , T_UserRole ur where r.id = ur.roleId and ur.userId=-9



