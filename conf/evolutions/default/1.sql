# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table club (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  location_id               bigint,
  constraint pk_club primary key (id))
;

create table location (
  id                        bigint not null,
  name                      varchar(255),
  version                   timestamp not null,
  constraint pk_location primary key (id))
;

create table membership (
  club_id                   bigint,
  user_id                   varchar(255),
  level                     integer,
  constraint ck_membership_level check (level in (0,1,2,3,4,5)),
  constraint pk_membership primary key (club_id, user_id))
;

create table user (
  id                        varchar(255) not null,
  name                      varchar(255),
  gender                    integer,
  constraint ck_user_gender check (gender in (0,1)),
  constraint pk_user primary key (id))
;

create sequence club_seq;

create sequence location_seq;

create sequence membership_seq;

create sequence user_seq;

alter table club add constraint fk_club_location_1 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_club_location_1 on club (location_id);
alter table membership add constraint fk_membership_club_2 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_membership_club_2 on membership (club_id);
alter table membership add constraint fk_membership_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_membership_user_3 on membership (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists club;

drop table if exists location;

drop table if exists membership;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists club_seq;

drop sequence if exists location_seq;

drop sequence if exists membership_seq;

drop sequence if exists user_seq;

