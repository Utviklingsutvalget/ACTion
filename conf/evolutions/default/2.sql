# --- !Ups

INSERT INTO location (id, name)
VALUES (1, 'Campus Galleriet'), (2, 'Campus Grønland'), (3, 'Campus Vulkan');

INSERT INTO club (id, name, short_name)
VALUES ('1', 'Arbeidsutvalget', 'AU');

INSERT INTO powerup (id, class_name, friendly_name, is_mandatory, has_menu_entry, default_weight)
VALUES ('1', 'core.clubimage.ClubImagePowerup', 'Utvalgsbilde', '1', '0', '0'),
  ('2', 'core.descriptionpowerup.DescriptionPowerup', 'Om utvalget', '1', '1', '1'),
  ('3', 'core.boardpowerup.BoardPowerup', 'Styret', '1', '1', '2'),
  ('4', 'core.eventpowerup.EventPowerup', 'Eventer', '1', '1', '3'),
  ('5', 'core.recruitpowerup.RecruitPowerup', 'Innmelding', '1', '1', '100'),
  ('7', 'core.feedpowerup.FeedPowerup', 'Feed', '1', '1', '5');

INSERT INTO user (id, first_name, last_name, email, picture_url) VALUES
  ('test1234', 'Ingen medlem', 'Ingen medlem','test@test.com',
   'http://4.bp.blogspot.com/-6Y8blNYlkdo/UlXn4Xik9-I/AAAAAAAAvfQ/NpqSuXYD8Zg/s1600/profile.png');

INSERT INTO board_post (id, title, mandatory, weight)
VALUES ('1', 'Leder', '1', '0'), ('2', 'Nestleder', '1', '1'), ('3', 'Økonomiansvarlig', '1', '2'),
  ('4', 'Eventansvarlig', '1', '3'), ('5', 'Sekretær', '1', '4');

INSERT INTO activation (powerup_id, club_id, weight) VALUES ('1', '1', '0');
INSERT INTO activation (powerup_id, club_id, weight) VALUES ('2', '1', '1');
INSERT INTO activation (powerup_id, club_id, weight) VALUES ('3', '1', '2');
INSERT INTO activation (powerup_id, club_id, weight) VALUES ('4', '1', '3');
INSERT INTO activation (powerup_id, club_id, weight) VALUES ('7', '1', '5');
INSERT INTO club_image (club_id, image_url) VALUES ('1', NULL);
INSERT INTO pl_clubdescription (club_id, description, list_description) VALUES ('1', ' ', ' ');

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE location;

TRUNCATE TABLE club;

TRUNCATE TABLE powerup;

TRUNCATE TABLE user;

TRUNCATE TABLE board_post;

TRUNCATE TABLE activation;

TRUNCATE TABLE pl_clubdescription;

SET REFERENTIAL_INTEGRITY TRUE;