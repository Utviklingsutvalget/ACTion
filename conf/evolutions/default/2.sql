# --- !Ups

INSERT INTO `action`.`user` (`id`, `first_name`, `last_name`, `gender`, `email`, `picture_url`) VALUES
  ('118106314260578391537', 'Eivind', 'Vegsundvåg', '0', 'vegeiv13@student.westerdals.no',
   'https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg');

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