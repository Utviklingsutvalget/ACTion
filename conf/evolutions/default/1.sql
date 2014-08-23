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
  description               varchar(10000),
  list_description          varchar(300),
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
  description               varchar(3000),
  start_time                datetime,
  end_time                  datetime,
  location                  varchar(255),
  cover_url                 varchar(255),
  host_id                   varchar(255),
  club_id                   bigint,
  constraint ck_event_privacy check (privacy in (0,1)),
  constraint pk_event primary key (id))
;

create table feed (
  id                        bigint auto_increment not null,
  user_id                   varchar(255),
  club_id                   bigint,
  message                   varchar(255),
  message_title             varchar(255),
  picture_url               varchar(255),
  Created                   datetime,
  constraint pk_feed primary key (id))
;

create table initiation_group (
  guardian_id               varchar(255),
  location_id               bigint,
  phone_number              varchar(255),
  group_number              integer,
  constraint pk_initiation_group primary key (guardian_id, location_id))
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
  constraint ck_participation_rvsp check (rvsp in (0,1,2)),
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

create table club_image_file (
  id                        bigint auto_increment not null,
  club_id                   bigint not null,
  image_path                varchar(255) not null,
  filename                  varchar(255) not null,
  constraint pk_club_image_file_pk primary key (id),
  constraint fk_club foreign key (club_id) references club(id))
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
alter table event add constraint fk_event_host_8 foreign key (host_id) references user (id) on delete restrict on update restrict;
create index ix_event_host_8 on event (host_id);
alter table event add constraint fk_event_club_9 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_event_club_9 on event (club_id);
alter table feed add constraint fk_feed_user_10 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_feed_user_10 on feed (user_id);
alter table feed add constraint fk_feed_club_11 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_feed_club_11 on feed (club_id);
alter table initiation_group add constraint fk_initiation_group_guardian_12 foreign key (guardian_id) references user (id) on delete restrict on update restrict;
create index ix_initiation_group_guardian_12 on initiation_group (guardian_id);
alter table initiation_group add constraint fk_initiation_group_location_13 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_initiation_group_location_13 on initiation_group (location_id);
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

INSERT INTO `action`.`location` (`id`, `name`)
VALUES (1, 'Campus Galleriet'), (2, 'Campus Grønland'), (3, 'Campus Vulkan');

INSERT INTO `action`.`club` (`id`, `name`, `short_name`)
VALUES ('1', 'Arbeidsutvalget', 'AU');

INSERT INTO `action`.`powerup` (`id`, `class_name`, `friendly_name`, `is_mandatory`, `has_menu_entry`, `default_weight`)
VALUES ('1', 'core.clubimage.ClubImagePowerup', 'Utvalgsbilde', '1', '0', '0'),
  ('2', 'core.descriptionpowerup.DescriptionPowerup', 'Beskrivelse', '1', '1', '1'),
  ('3', 'core.boardpowerup.BoardPowerup', 'Styret', '1', '1', '2'),
  ('4', 'core.eventpowerup.EventPowerup', 'Eventer', '1', '1', '3'),
  ('5', 'core.recruitpowerup.RecruitPowerup', 'Innmelding', '1', '1', '100'),
  ('7', 'core.feedpowerup.FeedPowerup', 'Feed', '1', '1', '5');

INSERT INTO `action`.`user` (`id`, `first_name`, `last_name`, `gender`, `email`, `picture_url`) VALUES
  ('test1234', 'Ingen medlem', 'Ingen medlem', '1', 'test@test.com',
   'http://4.bp.blogspot.com/-6Y8blNYlkdo/UlXn4Xik9-I/AAAAAAAAvfQ/NpqSuXYD8Zg/s1600/profile.png');

INSERT INTO `action`.`board_post` (`id`, `title`, `is_mandatory`, `weight`)
VALUES ('1', 'Leder', '1', '0'), ('2', 'Nestleder', '1', '1'), ('3', 'Økonomiansvarlig', '1', '2'),
  ('4', 'Eventansvarlig', '1', '3'), ('5', 'Sekretær', '1', '4');

INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`) VALUES ('1', '1', '0');
INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`) VALUES ('2', '1', '1');
INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`) VALUES ('3', '1', '2');
INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`) VALUES ('4', '1', '3');
INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`) VALUES ('7', '1', '5');
INSERT INTO `action`.`club_image` (`club_id`, `image_url`) VALUES ('1', NULL);
INSERT INTO `action`.`pl_clubdescription` (`club_id`, `description`, `list_description`) VALUES ('1', ' ', ' ');


# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table activation;

drop table board_membership;

drop table board_post;

drop table club;

drop table pl_ClubDescription;

drop table club_image;

drop table event;

drop table feed;

drop table initiation_group;

drop table location;

drop table membership;

drop table participation;

drop table pending;

drop table powerup;

drop table super_user;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

