# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table club (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_club primary key (id))
;

create table membership (
  name                      varchar(255),
  description               varchar(255),
  gender                    integer,
  user_id                   bigint not null,
  level                     integer,
  constraint ck_membership_gender check (gender in (0,1)),
  constraint ck_membership_level check (level in (0,1,2,3,4,5)))
;

create table user (
  id                        bigint not null,
  name                      varchar(255),
  gender                    integer,
  constraint ck_user_gender check (gender in (0,1)),
  constraint pk_user primary key (id))
;

create sequence club_seq;

create sequence membership_seq;

create sequence user_seq;

alter table membership add constraint fk_membership_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_membership_user_1 on membership (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists club;

drop table if exists membership;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists club_seq;

drop sequence if exists membership_seq;

drop sequence if exists user_seq;

