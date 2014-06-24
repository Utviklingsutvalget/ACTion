# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table activation (
  powerup_id                bigint,
  club_id                   bigint,
  constraint pk_activation primary key (powerup_id, club_id))
;

create table club (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_club primary key (id))
;

create table pl_ClubDescription (
  club_id                   bigint not null,
  description               varchar(255),
  constraint pk_pl_ClubDescription primary key (club_id))
;

create table membership (
  club_id                   bigint,
  user_id                   varchar(255),
  level                     integer,
  constraint ck_membership_level check (level in (0,1,2,3,4,5)),
  constraint pk_membership primary key (club_id, user_id))
;

create table powerup (
  id                        bigint not null,
  class_name                varchar(255),
  friendly_name             varchar(255),
  is_mandatory              boolean,
  constraint pk_powerup primary key (id))
;

create table user (
  id                        varchar(255) not null,
  name                      varchar(255),
  gender                    integer,
  constraint ck_user_gender check (gender in (0,1)),
  constraint pk_user primary key (id))
;

create sequence activation_seq;

create sequence club_seq;

create sequence pl_ClubDescription_seq;

create sequence membership_seq;

create sequence powerup_seq;

create sequence user_seq;

alter table activation add constraint fk_activation_powerup_1 foreign key (powerup_id) references powerup (id) on delete restrict on update restrict;
create index ix_activation_powerup_1 on activation (powerup_id);
alter table activation add constraint fk_activation_club_2 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_activation_club_2 on activation (club_id);
alter table membership add constraint fk_membership_club_3 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_membership_club_3 on membership (club_id);
alter table membership add constraint fk_membership_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_membership_user_4 on membership (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists activation;

drop table if exists club;

drop table if exists pl_ClubDescription;

drop table if exists membership;

drop table if exists powerup;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists activation_seq;

drop sequence if exists club_seq;

drop sequence if exists pl_ClubDescription_seq;

drop sequence if exists membership_seq;

drop sequence if exists powerup_seq;

drop sequence if exists user_seq;

