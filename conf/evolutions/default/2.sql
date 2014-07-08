# --- !Ups

INSERT INTO `action`.`location` (`id`, `name`)
VALUES (1, 'Campus Gallerigata'), (2, 'Campus Grønland'), (3, 'Campus Vulkan');

INSERT INTO `action`.`club` (`id`, `name`, `location_id`)
VALUES ('1', 'Utviklingsutvalget', '1'), ('2', 'Apple User Group', NULL), ('3', 'Forfatterutvalget', '2'),
  ('4', 'Kvinner og IT', '1'), ('5', 'Sosialutvalget i Gallerigata', '1'), ('6', 'Prosjektutvalg for Scene', '2'),
  ('7', 'Byråkratiutvalget', '3');

INSERT INTO `action`.`powerup` (`id`, `class_name`, `friendly_name`, `is_mandatory`, `has_menu_entry`)
VALUES ('1', 'core.descriptionpowerup.DescriptionPowerup', 'Beskrivelse', '1', '1'),
  ('2', 'core.clubimage.ClubImagePowerup', 'Utvalgsbilde', '1', '0');

INSERT INTO `action`.`activation` (`powerup_id`, `club_id`, `weight`)
VALUES ('1', '1', '0'), ('1', '2', '0'), ('2', '1', '1'), ('2', '2', '1');

INSERT INTO `action`.`pl_clubdescription` (`club_id`, `description`, `list_description`) VALUES ('1', 'Lang beskrivelse for Utviklingsutvalget. Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.', 'Veldig kort beskrivelse for Utviklingsutvalget.');

INSERT INTO `action`.`pl_clubdescription` (`club_id`, `description`, `list_description`) VALUES ('2', 'Lang beskrivelse for Utviklingsutvalget. Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.
Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.Lang beskrivelse for Utviklingsutvalget.', 'Veldig kort beskrivelse for Utviklingsutvalget.');