# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table student_group (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_student_group primary key (id))
;

create table user (
  id                        bigint not null,
  name                      varchar(255),
  gender                    integer,
  constraint ck_user_gender check (gender in (0,1)),
  constraint pk_user primary key (id))
;

create sequence student_group_seq;

create sequence user_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists student_group;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists student_group_seq;

drop sequence if exists user_seq;

