create table `user_alarm_status` (
    `user_id` BIGINT NOT NULL,
    `total_alarm_count` BIGINT NOT NULL DEFAULT 0,
    `unchecked_alarm_count` BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`user_id`),
    CONSTRAINT `FK__USER_ALARM_STATUS__USER_ID`
        FOREIGN KEY (`user_id`)
        REFERENCES `user`(`user_id`)
        ON DELETE CASCADE
)