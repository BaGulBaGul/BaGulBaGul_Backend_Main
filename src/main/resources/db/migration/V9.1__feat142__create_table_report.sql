create table `report` (
    `report_id` BIGINT NOT NULL AUTO_INCREMENT,
    `dtype` VARCHAR(15) NOT NULL,
    `solved` BIT(1) NOT NULL,
    `event_id` BIGINT NULL,
    `recruitment_id` BIGINT NULL,
    `post_comment_id` BIGINT NULL,
    `post_comment_child_id` BIGINT NULL,
    `report_type` VARCHAR(50) NOT NULL,
    `message` VARCHAR(1000) NULL,
    `reported_user_id` BIGINT NOT NULL,
    `reporting_user_id` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_modified_at` DATETIME NOT NULL,
    PRIMARY KEY(`report_id`),
    -- 해결되지 않은 신고를 최신순으로 조회
    INDEX `SOLVED__CREATED_AT` (`solved` ASC, `created_at` DESC),
    CONSTRAINT
        FOREIGN KEY(`event_id`)
        REFERENCES `event`(`event_id`)
        ON DELETE SET NULL,
    CONSTRAINT
        FOREIGN KEY(`recruitment_id`)
        REFERENCES `recruitment`(`recruitment_id`)
        ON DELETE SET NULL,
    CONSTRAINT
        FOREIGN KEY(`post_comment_id`)
        REFERENCES `post_comment`(`post_comment_id`)
        ON DELETE SET NULL,
    CONSTRAINT
        FOREIGN KEY(`post_comment_child_id`)
        REFERENCES `post_comment_child`(`post_comment_child_id`)
        ON DELETE SET NULL,
    CONSTRAINT
        FOREIGN KEY(`reported_user_id`)
        REFERENCES `user`(`user_id`)
        ON DELETE CASCADE,
    CONSTRAINT
        FOREIGN KEY(`reporting_user_id`)
        REFERENCES `user`(`user_id`)
        ON DELETE CASCADE
);