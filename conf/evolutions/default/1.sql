# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table club (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_club primary key (id))
;

create table user (
  id                        bigint not null,
  name                      varchar(255),
  gender                    integer,
  constraint ck_user_gender check (gender in (0,1)),
  constraint pk_user primary key (id))
;


create table club_user (
  club_id                        bigint not null,
  user_id                        bigint not null,
  constraint pk_club_user primary key (club_id, user_id))
;
create sequence club_seq;

create sequence user_seq;




alter table club_user add constraint fk_club_user_club_01 foreign key (club_id) references club (id) on delete restrict on update restrict;

alter table club_user add constraint fk_club_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists club;

drop table if exists club_user;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists club_seq;

drop sequence if exists user_seq;

