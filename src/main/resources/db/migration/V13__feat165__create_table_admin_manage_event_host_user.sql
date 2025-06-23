create table `admin_manage_event_host_user` (
    `admin_manage_event_host_user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT,
    PRIMARY KEY (`admin_manage_event_host_user_id`),
    CONSTRAINT `FK__ADMIN_MANAGE_EVENT_HOST_USER__USER_ID`
        FOREIGN KEY (`user_id`)
        REFERENCES `user`(`user_id`)
        ON DELETE CASCADE
);

alter table `event`
add column `event_host_user_id` BIGINT,
add constraint `FK__USER__EVENT_HOST_USER_ID`
    foreign key (`event_host_user_id`)
    references `user`(`user_id`)
    on delete set null,
add index `FK__USER__EVENT_HOST_USER_ID` (`event_host_user_id`);