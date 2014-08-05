# --- !Ups

ALTER TABLE  `pl_ClubDescription` CHANGE  `description`  `description` VARCHAR( 10000 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL;
ALTER TABLE  `pl_ClubDescription` CHANGE  `list_description`  list_description VARCHAR( 300 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL;
UPDATE `action`.`location` SET `name` = 'Campus Galleriet' WHERE `location`.`id` = 1;

# --- !Downs

ALTER TABLE  `pl_ClubDescription` CHANGE  `description`  `description` VARCHAR( 255 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL;
ALTER TABLE  `pl_ClubDescription` CHANGE  `list_description`  `list_description` VARCHAR( 255 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL;