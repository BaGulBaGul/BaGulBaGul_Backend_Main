create table `admin_manage_password_login_user` (
    `admin_manage_password_login_user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `login_id` VARCHAR(50),
    PRIMARY KEY (`admin_manage_password_login_user_id`),
    CONSTRAINT `FK__ADMIN_MANAGE_PASSWORD_LOGIN_USER__LOGIN_ID`
        FOREIGN KEY (`login_id`)
        REFERENCES `password_login_user`(`login_id`)
        ON DELETE CASCADE
);