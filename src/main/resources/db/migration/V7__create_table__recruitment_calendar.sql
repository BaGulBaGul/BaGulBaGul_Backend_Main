create table `recruitment_calendar` (
	`user_id` BIGINT NOT NULL,
    `recruitment_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `recruitment_id`),
    INDEX `FK__RECRUITMENT_CALENDAR__RECRUITMENT_ID` (`recruitment_id` ASC) VISIBLE,
    CONSTRAINT `FK__RECRUITMENT_CALENDAR__USER_ID`
		FOREIGN KEY (`user_id`)
        REFERENCES `user`(`user_id`)
        ON DELETE CASCADE,
	CONSTRAINT `FK__RECRUITMENT_CALENDAR__RECRUITMENT_ID`
		FOREIGN KEY (`recruitment_id`)
        REFERENCES `recruitment`(`recruitment_id`)
);