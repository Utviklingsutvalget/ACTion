# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table activation (
  powerup_id                bigint,
  club_id                   bigint,
  weight                    integer,
  constraint pk_activation primary key (powerup_id, club_id))
;

create table board (
  club_id                   bigint auto_increment not null,
  leader_id                 varchar(255),
  vice_id                   varchar(255),
  economy_id                varchar(255),
  constraint pk_board primary key (club_id))
;

create table board_extras (
  club_id                   bigint,
  extras_id                 integer,
  board_club_id             bigint,
  member_id                 varchar(255),
  title                     varchar(255),
  constraint pk_board_extras primary key (club_id, extras_id))
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
  club_id                   bigint auto_increment not null,
  constraint pk_club_image primary key (club_id))
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

create table pending (
  pending_id                bigint auto_increment not null,
  constraint pk_pending primary key (pending_id))
;

create table powerup (
  id                        bigint auto_increment not null,
  class_name                varchar(255),
  friendly_name             varchar(255),
  is_mandatory              tinyint(1) default 0,
  has_menu_entry            tinyint(1) default 0,
  constraint pk_powerup primary key (id))
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
alter table board add constraint fk_board_club_3 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_board_club_3 on board (club_id);
alter table board add constraint fk_board_leader_4 foreign key (leader_id) references user (id) on delete restrict on update restrict;
create index ix_board_leader_4 on board (leader_id);
alter table board add constraint fk_board_vice_5 foreign key (vice_id) references user (id) on delete restrict on update restrict;
create index ix_board_vice_5 on board (vice_id);
alter table board add constraint fk_board_economy_6 foreign key (economy_id) references user (id) on delete restrict on update restrict;
create index ix_board_economy_6 on board (economy_id);
alter table board_extras add constraint fk_board_extras_board_7 foreign key (board_club_id) references board (club_id) on delete restrict on update restrict;
create index ix_board_extras_board_7 on board_extras (board_club_id);
alter table board_extras add constraint fk_board_extras_member_8 foreign key (member_id) references user (id) on delete restrict on update restrict;
create index ix_board_extras_member_8 on board_extras (member_id);
alter table club add constraint fk_club_location_9 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_club_location_9 on club (location_id);
alter table membership add constraint fk_membership_club_10 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_membership_club_10 on membership (club_id);
alter table membership add constraint fk_membership_user_11 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_membership_user_11 on membership (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table activation;

drop table board;

drop table board_extras;

drop table club;

drop table pl_ClubDescription;

drop table club_image;

drop table location;

drop table membership;

drop table pending;

drop table powerup;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

