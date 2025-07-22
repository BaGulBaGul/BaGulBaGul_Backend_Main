create table `role` (
    `role_name` VARCHAR(50) NOT NULL,
    PRIMARY KEY(`role_name`)
);

create table `permission` (
    `permission_name` VARCHAR(50) NOT NULL,
    PRIMARY KEY(`permission_name`)
);

create table `user_role` (
    `user_id` BIGINT NOT NULL,
    `role_name` VARCHAR(50) NOT NULL,
    PRIMARY KEY(`user_id`, `role_name`),
    CONSTRAINT `FK__USER__USER_ID`
        FOREIGN KEY (`user_id`)
        REFERENCES `user`(`user_id`)
        ON DELETE CASCADE,
    CONSTRAINT `FK__ROLE__ROLE_NAME`
        FOREIGN KEY (`role_name`)
        REFERENCES `role`(`role_name`)
        ON DELETE CASCADE
);

create table `role_permission` (
    `role_name` VARCHAR(50) NOT NULL,
    `permission_name` VARCHAR(50) NOT NULL,
    PRIMARY KEY(`role_name`, `permission_name`),
    CONSTRAINT `FK__ROLE_PERMISSION__ROLE_NAME`
        FOREIGN KEY (`role_name`)
        REFERENCES `role`(`role_name`)
        ON DELETE CASCADE,
    CONSTRAINT `FK__ROLE_PERMISSION__PERMISSION_NAME`
        FOREIGN KEY (`permission_name`)
        REFERENCES `permission`(`permission_name`)
        ON DELETE CASCADE
);

-- 기본 역할, 권한 추가
insert into `role` values
    ("ADMIN"),
    ("EVENT_HOST"),
    ("USER");
insert into `permission` values
    ("MANAGE_USER"),
    ("MANAGE_EVENT"),
    ("MANAGE_RECRUITMENT"),
    ("MANAGE_REPORT"),
    ("WRITE_EVENT_FESTIVAL"),
    ("WRITE_EVENT_LOCAL_EVENT");
insert into `role_permission` (`role_name`, `permission_name`) values
    ("ADMIN", "MANAGE_USER"),
    ("ADMIN", "MANAGE_EVENT"),
    ("ADMIN", "MANAGE_RECRUITMENT"),
    ("ADMIN", "MANAGE_REPORT");
insert into `role_permission` (`role_name`, `permission_name`) values
    ("EVENT_HOST", "WRITE_EVENT_FESTIVAL"),
    ("EVENT_HOST", "WRITE_EVENT_LOCAL_EVENT");