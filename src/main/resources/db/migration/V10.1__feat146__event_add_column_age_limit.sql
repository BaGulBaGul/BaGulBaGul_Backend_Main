-- event에 나이 제한 여부 추가
ALTER TABLE `event` ADD COLUMN `age_limit` BIT NOT NULL DEFAULT 0;