# --- !Ups

INSERT INTO `action`.`location` (`id`, `name`)
VALUES (1, 'Campus Gallerigata'), (2, 'Campus Grønland'), (3, 'Campus Vulkan');

INSERT INTO `action`.`club` (`id`, `name`, `short_name`, `location_id`)
VALUES ('1', 'Utviklingsutvalget', 'UU', '1'), ('2', 'Apple User Group', 'AUG', NULL), ('3', 'Forfatterutvalget', 'FU', '2'),
  ('4', 'Kvinner og IT', 'KIT', '1'), ('5', 'Sosialutvalget i Gallerigata', 'SUG','1'), ('6', 'Prosjektutvalg for Scene', 'PUSc', '2'),
  ('7', 'Byråkratiutvalget', 'BU', '3');

INSERT INTO `action`.`powerup` (`id`, `class_name`, `friendly_name`, `is_mandatory`, `has_menu_entry`)
VALUES ('1', 'core.descriptionpowerup.DescriptionPowerup', 'Beskrivelse', '1', '1'),
  ('2', 'core.clubimage.ClubImagePowerup', NULL, '1', '0'),
  ('3', 'core.boardpowerup.BoardPowerup', 'Styremedlemmer', '1', '1');

INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`)
VALUES ('1', '1', '0'), ('1', '2', '0'), ('2', '1', '1'), ('2', '2', '1');

INSERT INTO `action`.`pl_clubdescription` (`club_id`, `description`, `list_description`) VALUES ('1', 'Lang beskrivelse for Utviklingsutvalget. Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.', 'Veldig kort beskrivelse for Utviklingsutvalget.');

INSERT INTO `action`.`pl_clubdescription` (`club_id`, `description`, `list_description`) VALUES ('2', 'Lang beskrivelse for Utviklingsutvalget. Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.', 'Veldig kort beskrivelse for Utviklingsutvalget.');


INSERT INTO `action`.`user` (`id`, `name`, `gender`, `email`, `picture_url`) VALUES ('1', 'ragnar', '1', NULL, NULL), ('2', 'dsdsadsa', '0', NULL, NULL);

INSERT INTO `action`.`board` (`club_id`, `leader_id`, `vice_id`, `economy_id`) VALUES ('1', '1', '2', '1');
INSERT INTO `action`.`board_extras` (`club_id`, `extras_id`, `member_id`, `title`) VALUES ('1', '1', '1', 'løytnant');
INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`) VALUES ('3', '1', '4');

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE `action`.`location`;
TRUNCATE TABLE `action`.`club`;
TRUNCATE TABLE `action`.`powerup`;
TRUNCATE TABLE `action`.`activation`;
TRUNCATE TABLE `action`.`pl_clubdescription`;
