# --- !Ups

INSERT INTO `action`.`location` (`id`, `name`)
VALUES (1, 'Campus Gallerigata'), (2, 'Campus Grønland'), (3, 'Campus Vulkan');

INSERT INTO `action`.`club` (`id`, `name`, `short_name`)
VALUES ('1', 'Arbeidsutvalget', 'AU');

INSERT INTO `action`.`powerup` (`id`, `class_name`, `friendly_name`, `is_mandatory`, `has_menu_entry`, `default_weight`)
VALUES ('1', 'core.clubimage.ClubImagePowerup', 'Utvalgsbilde', '1', '0', '0'),
  ('2', 'core.descriptionpowerup.DescriptionPowerup', 'Beskrivelse', '1', '1', '1'),
  ('3', 'core.boardpowerup.BoardPowerup', 'Styret', '1', '1', '2'),
  ('4', 'core.eventpowerup.EventPowerup', 'Eventer', '1', '1', '3'),
  ('5', 'core.recruitpowerup.RecruitPowerup', 'Innmelding', '1', '1', '100');

INSERT INTO `action`.`user` (`id`, `first_name`, `last_name`, `gender`, `email`, `picture_url`) VALUES
  ('test1234', 'Ingen medlem', 'Ingen medlem', '1', 'test@test.com',
   'http://4.bp.blogspot.com/-6Y8blNYlkdo/UlXn4Xik9-I/AAAAAAAAvfQ/NpqSuXYD8Zg/s1600/profile.png');

INSERT INTO `action`.`board_post` (`id`, `title`, `is_mandatory`, `weight`)
VALUES ('1', 'Leder', '1', '0'), ('2', 'Nestleder', '1', '1'), ('3', 'Økonomiansvarlig', '1', '2'),
  ('4', 'Eventansvarlig', '1', '3'), ('5', 'Sekretær', '1', '4');