ALTER TABLE `members`
ADD `profile_id` char(36) NULL;

ALTER TABLE `members`
ADD CONSTRAINT FK_members_profile_id FOREIGN KEY (`profile_id`) REFERENCES profiles (`profile_id`);
