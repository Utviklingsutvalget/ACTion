# --- !Ups

INSERT INTO location (id, name)
VALUES (1, 'Campus Galleriet'), (2, 'Campus Vaterland'), (3, 'Campus Vulkan');

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE location;

SET REFERENTIAL_INTEGRITY TRUE;