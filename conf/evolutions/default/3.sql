# --- !Ups
UPDATE `action`.`powerup` SET `friendly_name` = 'Om utvalget' WHERE `powerup`.`id` = 2;

# --- !Downs
UPDATE `action`.`powerup` SET `friendly_name` = 'Beskrivelse' WHERE `powerup`.`id` = 2;