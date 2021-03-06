# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table activation (
  powerup_id                bigint,
  club_id                   bigint,
  weight                    integer,
  constraint pk_activation primary key (powerup_id, club_id))
;

create table board_membership (
  club_id                   bigint,
  board_post_id             bigint,
  user_id                   varchar(255),
  weight                    integer,
  constraint uq_board_membership_user_id unique (user_id),
  constraint pk_board_membership primary key (club_id, board_post_id))
;

create table board_post (
  id                        bigint not null,
  title                     varchar(255),
  mandatory                 boolean,
  weight                    integer,
  constraint pk_board_post primary key (id))
;

create table club (
  id                        bigint not null,
  name                      varchar(255),
  short_name                varchar(255),
  location_id               bigint,
  constraint pk_club primary key (id))
;

create table pl_ClubDescription (
  club_id                   bigint,
  description               varchar(255),
  list_description          varchar(255),
  constraint uq_pl_ClubDescription_club_id unique (club_id),
  constraint pk_pl_ClubDescription primary key (club_id))
;

create table club_image (
  club_id                   bigint,
  image_url                 varchar(255),
  constraint uq_club_image_club_id unique (club_id),
  constraint pk_club_image primary key (club_id))
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

create table initiation_group (
  guardian_id               varchar(255),
  location_id               bigint,
  phone_number              varchar(255),
  group_number              integer,
  constraint uq_initiation_group_guardian_id unique (guardian_id),
  constraint pk_initiation_group primary key (guardian_id, location_id))
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

create table powerup (
  id                        bigint not null,
  class_name                varchar(255),
  friendly_name             varchar(255),
  is_mandatory              boolean,
  has_menu_entry            boolean,
  default_weight            integer,
  constraint pk_powerup primary key (id))
;

create table super_user (
  user_id                   varchar(255),
  constraint uq_super_user_user_id unique (user_id),
  constraint pk_super_user primary key (user_id))
;

create table user (
  id                        varchar(255) not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255),
  picture_url               varchar(255),
  constraint pk_user primary key (id))
;

create sequence activation_seq;

create sequence board_membership_seq;

create sequence board_post_seq;

create sequence club_seq;

create sequence pl_ClubDescription_seq;

create sequence club_image_seq;

create sequence event_seq;

create sequence feed_seq;

create sequence initiation_group_seq;

create sequence location_seq;

create sequence membership_seq;

create sequence participation_seq;

create sequence pending_seq;

create sequence powerup_seq;

create sequence super_user_seq;

create sequence user_seq;

alter table activation add constraint fk_activation_club_1 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_activation_club_1 on activation (club_id);
alter table activation add constraint fk_activation_powerup_2 foreign key (powerup_id) references powerup (id) on delete restrict on update restrict;
create index ix_activation_powerup_2 on activation (powerup_id);
alter table board_membership add constraint fk_board_membership_club_3 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_board_membership_club_3 on board_membership (club_id);
alter table board_membership add constraint fk_board_membership_boardPost_4 foreign key (board_post_id) references board_post (id) on delete restrict on update restrict;
create index ix_board_membership_boardPost_4 on board_membership (board_post_id);
alter table board_membership add constraint fk_board_membership_user_5 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_board_membership_user_5 on board_membership (user_id);
alter table club add constraint fk_club_location_6 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_club_location_6 on club (location_id);
alter table pl_ClubDescription add constraint fk_pl_ClubDescription_club_7 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_pl_ClubDescription_club_7 on pl_ClubDescription (club_id);
alter table club_image add constraint fk_club_image_club_8 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_club_image_club_8 on club_image (club_id);
alter table event add constraint fk_event_host_9 foreign key (host_id) references user (id) on delete restrict on update restrict;
create index ix_event_host_9 on event (host_id);
alter table event add constraint fk_event_club_10 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_event_club_10 on event (club_id);
alter table feed add constraint fk_feed_user_11 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_feed_user_11 on feed (user_id);
alter table feed add constraint fk_feed_club_12 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_feed_club_12 on feed (club_id);
alter table initiation_group add constraint fk_initiation_group_guardian_13 foreign key (guardian_id) references user (id) on delete restrict on update restrict;
create index ix_initiation_group_guardian_13 on initiation_group (guardian_id);
alter table initiation_group add constraint fk_initiation_group_location_14 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_initiation_group_location_14 on initiation_group (location_id);
alter table membership add constraint fk_membership_club_15 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_membership_club_15 on membership (club_id);
alter table membership add constraint fk_membership_user_16 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_membership_user_16 on membership (user_id);
alter table participation add constraint fk_participation_event_17 foreign key (event_id) references event (id) on delete restrict on update restrict;
create index ix_participation_event_17 on participation (event_id);
alter table participation add constraint fk_participation_user_18 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_participation_user_18 on participation (user_id);
alter table pending add constraint fk_pending_club_19 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_pending_club_19 on pending (club_id);
alter table pending add constraint fk_pending_user_20 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_pending_user_20 on pending (user_id);
alter table super_user add constraint fk_super_user_user_21 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_super_user_user_21 on super_user (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists activation;

drop table if exists board_membership;

drop table if exists board_post;

drop table if exists club;

drop table if exists pl_ClubDescription;

drop table if exists club_image;

drop table if exists event;

drop table if exists feed;

drop table if exists initiation_group;

drop table if exists location;

drop table if exists membership;

drop table if exists participation;

drop table if exists pending;

drop table if exists powerup;

drop table if exists super_user;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists activation_seq;

drop sequence if exists board_membership_seq;

drop sequence if exists board_post_seq;

drop sequence if exists club_seq;

drop sequence if exists pl_ClubDescription_seq;

drop sequence if exists club_image_seq;

drop sequence if exists event_seq;

drop sequence if exists feed_seq;

drop sequence if exists initiation_group_seq;

drop sequence if exists location_seq;

drop sequence if exists membership_seq;

drop sequence if exists participation_seq;

drop sequence if exists pending_seq;

drop sequence if exists powerup_seq;

drop sequence if exists super_user_seq;

drop sequence if exists user_seq;

