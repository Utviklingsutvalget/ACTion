# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table club (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  location_id               bigint,
  constraint pk_club primary key (id))
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

create table user (
  id                        varchar(255) not null,
  name                      varchar(255),
  gender                    integer,
  email                     varchar(255),
  picture_url               varchar(255),
  constraint ck_user_gender check (gender in (0,1)),
  constraint pk_user primary key (id))
;

alter table club add constraint fk_club_location_1 foreign key (location_id) references location (id) on delete restrict on update restrict;
create index ix_club_location_1 on club (location_id);
alter table membership add constraint fk_membership_club_2 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_membership_club_2 on membership (club_id);
alter table membership add constraint fk_membership_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_membership_user_3 on membership (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table club;

drop table location;

drop table membership;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

