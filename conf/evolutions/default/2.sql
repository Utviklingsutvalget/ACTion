# --- !Ups

INSERT INTO `action`.`location` (`id`, `name`)
VALUES (1, 'Campus Gallerigata'), (2, 'Campus Grønland'), (3, 'Campus Vulkan');

INSERT INTO `action`.`club` (`id`, `name`, `short_name`, `location_id`)
VALUES ('1', 'Utviklingsutvalget', 'UU', '1'), ('2', 'Apple User Group', 'AUG', '1'), ('3', 'Forfatterutvalget', 'FU', '2'),
  ('4', 'Kvinner og IT', 'KIT', '1'), ('5', 'Sosialutvalget i Gallerigata', 'SUG','1'), ('6', 'Prosjektutvalg for Scene', 'PUSc', '2'),
  ('7', 'Byråkratiutvalget', 'BU', '3');

INSERT INTO `action`.`powerup` (`id`, `class_name`, `friendly_name`, `is_mandatory`, `has_menu_entry`)
VALUES ('1', 'core.descriptionpowerup.DescriptionPowerup', 'Beskrivelse', '1', '1'),
  ('2', 'core.clubimage.ClubImagePowerup', NULL, '1', '0'),
  ('3', 'core.boardpowerup.BoardPowerup', 'Styremedlemmer', '1', '1'),
  ('4', 'core.recruitpowerup.RecruitPowerup', 'Innmelding', '1', '0'),
  ('5', 'core.eventpowerup.EventPowerup', 'Eventer', '1', '1');

INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`)
VALUES ('1', '1', '1'), ('1', '2', '3'), ('2', '1', '0'), ('2', '2', '4');

INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`)
VALUES ('4', '1', '10'), ('4', '2', '10');

INSERT INTO `action`.`pl_clubdescription` (`club_id`, `description`, `list_description`)
VALUES ('1', 'Lang beskrivelse for Utviklingsutvalget. Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.', 'Veldig kort beskrivelse for Utviklingsutvalget.');

INSERT INTO `action`.`pl_clubdescription` (`club_id`, `description`, `list_description`)
VALUES ('2', 'Lang beskrivelse for Apple user group. Lang beskrivelse for Apple user group.
Lang beskrivelse for Apple user group.
Lang beskrivelse for Apple user group.Lang beskrivelse for Apple user group.Lang beskrivelse for Apple user group.', 'Veldig kort beskrivelse for Apple user group.');


INSERT INTO `action`.`user` (`id`, `first_name`, `gender`, `email`, `picture_url`) VALUES ('1', 'ragnar', '1', NULL, 'http://www.catchwallpapers.com/wp-content/uploads/2013/03/HBO-drama-Game-of-Thrones-Season-3-HD-characters-wallpaper-1600x1200-10-765x400.jpg'), ('2', 'dsdsadsa', '0', NULL, NULL);

INSERT INTO `action`.`board` (`club_id`, `leader_id`, `vice_id`, `economy_id`) VALUES ('1', '1', '2', '1');
INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`) VALUES ('3', '1', '3');

INSERT INTO `action`.`user` (`id`, `first_name`, `gender`, `email`, `picture_url`) VALUES ('3', 'Kjell', '1', NULL, NULL), ('4', 'Berit', '0', NULL, NULL);
INSERT INTO `action`.`user` (`id`, `first_name`, `gender`, `email`, `picture_url`) VALUES ('5', 'Hanne', NULL, NULL, NULL), ('6', 'Arne', NULL, NULL, NULL);
INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`) VALUES ('3', '2', '0');
INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`) VALUES ('5', '1', '4');
INSERT INTO `action`.`board` (`club_id`, `leader_id`, `vice_id`, `economy_id`) VALUES ('2', '2', '3', '6');
INSERT INTO `action`.`membership` (`club_id`, `user_id`, `level`) VALUES ('1', '6', '2'), ('1', '4', '1');

UPDATE `action`.`board` SET `leader_id` = '3', `economy_id` = '6' WHERE `board`.`club_id` = 1;
UPDATE `action`.`user` SET `picture_url` = 'http://www.catchwallpapers.com/wp-content/uploads/2013/03/HBO-drama-Game-of-Thrones-Season-3-HD-characters-wallpaper-1600x1200-07.jpg' WHERE `user`.`id` = '3';
UPDATE `action`.`user` SET `picture_url` = 'http://i3.bebo.com/044/1/mediuml/2008/09/18/18/573747691a8917801913ml.jpg' WHERE `user`.`id` = '6';
UPDATE `action`.`user` SET `picture_url` = 'http://mobilwi.typepad.com/.a/6a0120a6dde087970b013485bca6ed970c-500wi' WHERE `user`.`id` = '2';
UPDATE `action`.`board` SET `leader_id` = '1' WHERE `board`.`club_id` = 2;
UPDATE `action`.`board` SET `event_id` = '4' WHERE `board`.`club_id` = 1;
UPDATE `action`.`board` SET `event_id` = '5' WHERE `board`.`club_id` = 2;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE `action`.`location`;
TRUNCATE TABLE `action`.`club`;
TRUNCATE TABLE `action`.`powerup`;
TRUNCATE TABLE `action`.`activation`;
TRUNCATE TABLE `action`.`pl_clubdescription`;


/*

*/