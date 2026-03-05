create table `password_login_user` (
    `login_id` VARCHAR(50) NOT NULL,
    `encoded_login_password` VARCHAR(50) NOT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`login_id`),
    CONSTRAINT `FK__PASSWORD_LOGIN_USER__USER_ID` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
);