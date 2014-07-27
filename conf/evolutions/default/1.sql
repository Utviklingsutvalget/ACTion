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
  constraint pk_board_membership primary key (club_id, board_post_id))
;

create table board_post (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  is_mandatory              tinyint(1) default 0,
  weight                    integer,
  constraint pk_board_post primary key (id))
;

create table club (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  short_name                varchar(255),
  location_id               bigint,
  constraint pk_club primary key (id))
;

create table pl_ClubDescription (
  club_id                   bigint auto_increment not null,
  description               varchar(255),
  list_description          varchar(255),
  constraint pk_pl_ClubDescription primary key (club_id))
;

create table club_image (
  club_id                   bigint,
  image_url                 varchar(255),
  constraint pk_club_image primary key (club_id))
;

create table event (
  id                        bigint auto_increment not null,
  privacy                   integer,
  name                      varchar(255),
  description               varchar(255),
  start_time                datetime,
  end_time                  datetime,
  location                  varchar(255),
  cover_url                 varchar(255),
  club_id                   bigint,
  constraint ck_event_privacy check (privacy in (0,1)),
  constraint pk_event primary key (id))
;

create table location (
  id                        bigint auto_increment not null,
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
  constraint ck_participation_rvsp check (rvsp in (0,1)),
  constraint pk_participation primary key (event_id, user_id))
;

create table pending (
  user_id                   varchar(255),
  club_id                   bigint,
  application_message       varchar(100),
  constraint pk_pending primary key (user_id, club_id))
;

create table powerup (
  id                        bigint auto_increment not null,
  class_name                varchar(255),
  friendly_name             varchar(255),
  is_mandatory              tinyint(1) default 0,
  has_menu_entry            tinyint(1) default 0,
  default_weight            integer,
  constraint pk_powerup primary key (id))
;

create table super_user (
  user_id                   varchar(255),
  constraint pk_super_user primary key (user_id))
;

create table user (
  id                        varchar(255) not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  gender                    integer,
  email                     varchar(255),
  picture_url               varchar(255),
  constraint ck_user_gender check (gender in (0,1)),
  constraint pk_user primary key (id))
;

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
alter table club_image add constraint fk_club_image_club_7 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_club_image_club_7 on club_image (club_id);
alter table event add constraint fk_event_club_8 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_event_club_8 on event (club_id);
alter table membership add constraint fk_membership_club_9 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_membership_club_9 on membership (club_id);
alter table membership add constraint fk_membership_user_10 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_membership_user_10 on membership (user_id);
alter table participation add constraint fk_participation_event_11 foreign key (event_id) references event (id) on delete restrict on update restrict;
create index ix_participation_event_11 on participation (event_id);
alter table participation add constraint fk_participation_user_12 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_participation_user_12 on participation (user_id);
alter table pending add constraint fk_pending_club_13 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_pending_club_13 on pending (club_id);
alter table pending add constraint fk_pending_user_14 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_pending_user_14 on pending (user_id);
alter table super_user add constraint fk_super_user_user_15 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_super_user_user_15 on super_user (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table activation;

drop table board_membership;

drop table board_post;

drop table club;

drop table pl_ClubDescription;

drop table club_image;

drop table event;

drop table location;

drop table membership;

drop table participation;

drop table pending;

drop table powerup;

drop table super_user;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

