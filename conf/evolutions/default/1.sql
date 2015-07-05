# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table board_membership (
  club_id                   bigint,
  user_id                   varchar(255),
  title                     varchar(255),
  weight                    integer,
  constraint pk_board_membership primary key (club_id, user_id))
;

create table club (
  id                        bigint not null,
  name                      varchar(255) not null,
  short_name                varchar(255) not null,
  location_id               bigint,
  uploaded_file_id          varchar(40),
  description               varchar(10000),
  list_description          varchar(300),
  owner_id                  varchar(255),
  constraint uq_club_owner_id unique (owner_id),
  constraint pk_club primary key (id))
;

create table event (
  id                        bigint not null,
  privacy                   integer,
  name                      varchar(255),
  description               varchar(3000),
  start_time                timestamp,
  end_time                  timestamp,
  location                  varchar(255),
  cover_url                 varchar(255),
  host_id                   varchar(255),
  club_id                   bigint,
  constraint ck_event_privacy check (privacy in (0,1)),
  constraint pk_event primary key (id))
;

create table feed (
  id                        bigint not null,
  user_id                   varchar(255),
  club_id                   bigint,
  message                   varchar(1500),
  message_title             varchar(255),
  picture_url               varchar(255),
  Created                   timestamp,
  constraint pk_feed primary key (id))
;

create table initiation_event (
  id                        bigint not null,
  time                      timestamp,
  title                     varchar(255),
  description               varchar(300),
  location                  varchar(255),
  initiation_schedule_id    bigint,
  constraint pk_initiation_event primary key (id))
;

create table initiation_group (
  guardian_id               varchar(255),
  location_id               bigint,
  phone_number              varchar(255),
  group_number              integer,
  constraint uq_initiation_group_guardian_id unique (guardian_id),
  constraint pk_initiation_group primary key (guardian_id, location_id))
;

create table initiation_schedule (
  id                        bigint not null,
  campus_id                 bigint,
  year                      integer,
  constraint pk_initiation_schedule primary key (id))
;

create table location (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_location primary key (id))
;

create table membership (
  club_id                   bigint,
  user_id                   varchar(255),
  level                     integer,
  constraint ck_membership_level check (level in (0,1,2,3,4,5)),
  constraint pk_membership primary key (club_id, user_id))
;

create table participation (
  event_id                  bigint,
  user_id                   varchar(255),
  rvsp                      integer,
  constraint ck_participation_rvsp check (rvsp in (0,1,2)),
  constraint pk_participation primary key (event_id, user_id))
;

create table pending (
  club_id                   bigint,
  user_id                   varchar(255),
  application_message       varchar(100),
  constraint pk_pending primary key (club_id, user_id))
;

create table super_user (
  user_id                   varchar(255),
  constraint uq_super_user_user_id unique (user_id),
  constraint pk_super_user primary key (user_id))
;

create table uploaded_file (
  id                        varchar(40) not null,
  name                      varchar(255),
  constraint pk_uploaded_file primary key (id))
;

create table user (
  id                        varchar(255) not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255),
  picture_url               varchar(255),
  constraint pk_user primary key (id))
;

create sequence board_membership_seq;

create sequence club_seq;

create sequence event_seq;

create sequence feed_seq;

create sequence initiation_event_seq;

create sequence initiation_group_seq;

create sequence initiation_schedule_seq;

create sequence location_seq;

create sequence membership_seq;

create sequence participation_seq;

create sequence pending_seq;

create sequence super_user_seq;

create sequence user_seq;

alter table board_membership add constraint fk_board_membership_club_1 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_board_membership_club_1 on board_membership (club_id);
alter table board_membership add constraint fk_board_membership_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_board_membership_user_2 on board_membership (user_id);
alter table club add constraint fk_club_location_3 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_club_location_3 on club (location_id);
alter table club add constraint fk_club_uploadedFile_4 foreign key (uploaded_file_id) references uploaded_file (id) on delete restrict on update restrict;
create index ix_club_uploadedFile_4 on club (uploaded_file_id);
alter table club add constraint fk_club_owner_5 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_club_owner_5 on club (owner_id);
alter table event add constraint fk_event_host_6 foreign key (host_id) references user (id) on delete restrict on update restrict;
create index ix_event_host_6 on event (host_id);
alter table event add constraint fk_event_club_7 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_event_club_7 on event (club_id);
alter table feed add constraint fk_feed_user_8 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_feed_user_8 on feed (user_id);
alter table feed add constraint fk_feed_club_9 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_feed_club_9 on feed (club_id);
alter table initiation_event add constraint fk_initiation_event_initiatio_10 foreign key (initiation_schedule_id) references initiation_schedule (id) on delete restrict on update restrict;
create index ix_initiation_event_initiatio_10 on initiation_event (initiation_schedule_id);
alter table initiation_group add constraint fk_initiation_group_guardian_11 foreign key (guardian_id) references user (id) on delete restrict on update restrict;
create index ix_initiation_group_guardian_11 on initiation_group (guardian_id);
alter table initiation_group add constraint fk_initiation_group_location_12 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_initiation_group_location_12 on initiation_group (location_id);
alter table initiation_schedule add constraint fk_initiation_schedule_campus_13 foreign key (campus_id) references location (id) on delete restrict on update restrict;
create index ix_initiation_schedule_campus_13 on initiation_schedule (campus_id);
alter table membership add constraint fk_membership_club_14 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_membership_club_14 on membership (club_id);
alter table membership add constraint fk_membership_user_15 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_membership_user_15 on membership (user_id);
alter table participation add constraint fk_participation_event_16 foreign key (event_id) references event (id) on delete restrict on update restrict;
create index ix_participation_event_16 on participation (event_id);
alter table participation add constraint fk_participation_user_17 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_participation_user_17 on participation (user_id);
alter table pending add constraint fk_pending_club_18 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_pending_club_18 on pending (club_id);
alter table pending add constraint fk_pending_user_19 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_pending_user_19 on pending (user_id);
alter table super_user add constraint fk_super_user_user_20 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_super_user_user_20 on super_user (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists board_membership;

drop table if exists club;

drop table if exists event;

drop table if exists feed;

drop table if exists initiation_event;

drop table if exists initiation_group;

drop table if exists initiation_schedule;

drop table if exists location;

drop table if exists membership;

drop table if exists participation;

drop table if exists pending;

drop table if exists super_user;

drop table if exists uploaded_file;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists board_membership_seq;

drop sequence if exists club_seq;

drop sequence if exists event_seq;

drop sequence if exists feed_seq;

drop sequence if exists initiation_event_seq;

drop sequence if exists initiation_group_seq;

drop sequence if exists initiation_schedule_seq;

drop sequence if exists location_seq;

drop sequence if exists membership_seq;

drop sequence if exists participation_seq;

drop sequence if exists pending_seq;

drop sequence if exists super_user_seq;

drop sequence if exists user_seq;

