ALTER TABLE `mythicallogin_users` ADD `blocked` ENUM ('false', 'true') NOT NULL DEFAULT 'false' AFTER `username`;