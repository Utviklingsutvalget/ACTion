# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table activation (
  plugin_id                 varchar(255),
  club_id                   bigint,
  constraint pk_activation primary key (plugin_id, club_id))
;

create table club (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_club primary key (id))
;

create table membership (
  club_id                   bigint,
  user_id                   varchar(255),
  level                     integer,
  constraint ck_membership_level check (level in (0,1,2,3,4,5)),
  constraint pk_membership primary key (club_id, user_id))
;

create table plugin (
  id                        varchar(255) not null,
  constraint pk_plugin primary key (id))
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

create sequence membership_seq;

create sequence plugin_seq;

create sequence user_seq;

alter table activation add constraint fk_activation_club_1 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_activation_club_1 on activation (club_id);
alter table activation add constraint fk_activation_plugin_2 foreign key (plugin_id) references plugin (id) on delete restrict on update restrict;
create index ix_activation_plugin_2 on activation (plugin_id);
alter table membership add constraint fk_membership_club_3 foreign key (club_id) references club (id) on delete restrict on update restrict;
create index ix_membership_club_3 on membership (club_id);
alter table membership add constraint fk_membership_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_membership_user_4 on membership (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists activation;

drop table if exists club;

drop table if exists membership;

drop table if exists plugin;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists activation_seq;

drop sequence if exists club_seq;

drop sequence if exists membership_seq;

drop sequence if exists plugin_seq;

drop sequence if exists user_seq;

