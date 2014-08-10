# --- !Ups
ALTER TABLE `feed` CHANGE `message_title` `message_title` VARCHAR(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL;
ALTER TABLE `feed` CHANGE `message` `message` VARCHAR(1500) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL;

# --- !Downs
ALTER TABLE `feed` CHANGE `message_title` `message_title` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL;
ALTER TABLE `feed` CHANGE `message` `message` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL;