-- user가 삭제될 때 user image는 on delete cascade가 아닌 on delete set null로 변경(실제 파일을 삭제해야 하기 때문)
ALTER TABLE `user_image` DROP FOREIGN KEY `FK__USER_IMAGE__USER_ID`;
ALTER TABLE `user_image` ADD CONSTRAINT `FK__USER_IMAGE__USER_ID` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE SET NULL;