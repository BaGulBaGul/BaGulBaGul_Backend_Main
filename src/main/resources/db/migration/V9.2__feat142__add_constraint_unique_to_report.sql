ALTER TABLE `report`
ADD CONSTRAINT `UK__REPORT__reporting_user__event` UNIQUE (reporting_user_id, event_id),
ADD CONSTRAINT `UK__REPORT__reporting_user__recruitment` UNIQUE (reporting_user_id, recruitment_id),
ADD CONSTRAINT `UK__REPORT__reporting_user__post_comment` UNIQUE (reporting_user_id, post_comment_id),
ADD CONSTRAINT `UK__REPORT__reporting_user__post_comment_child` UNIQUE (reporting_user_id, post_comment_child_id);