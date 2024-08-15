-- recruitment의 soft delete를 위해서 deleted필드를 추가
ALTER TABLE `recruitment` ADD COLUMN deleted BIT NOT NULL DEFAULT 0 AFTER `recruitment_id`;