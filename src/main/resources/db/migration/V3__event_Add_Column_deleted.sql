-- event의 soft delete를 위해서 deleted필드를 추가
ALTER TABLE `event` ADD COLUMN deleted BIT NOT NULL DEFAULT 0 AFTER `event_id`;