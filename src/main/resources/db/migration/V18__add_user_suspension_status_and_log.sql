-- Add is_suspended column to user table
ALTER TABLE `user` ADD COLUMN is_suspended BOOLEAN NOT NULL DEFAULT FALSE;

-- Create user_suspension_status table
CREATE TABLE `user_suspension_status` (
    user_id BIGINT NOT NULL,
    end_date datetime,
    reason VARCHAR(255),
    PRIMARY KEY (user_id),
    CONSTRAINT FK__USER_SUSPENSION_STATUS__USER_ID FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
);

-- Create user_suspension_log table
CREATE TABLE `user_suspension_log` (
    user_suspension_log_id BIGINT NOT NULL AUTO_INCREMENT,
    created_at datetime,
    last_modified_at datetime,
    user_id BIGINT,
    reason VARCHAR(255),
    start_date datetime,
    end_date datetime,
    admin_id BIGINT,
    action_type VARCHAR(255),
    PRIMARY KEY (user_suspension_log_id),
    CONSTRAINT `FK__USER_SUSPENSION_LOG__USER_ID`
        FOREIGN KEY (user_id)
        REFERENCES user (user_id)
        ON DELETE CASCADE,
    CONSTRAINT `FK__USER_SUSPENSION_LOG__ADMIN_ID`
        FOREIGN KEY (admin_id)
        REFERENCES user (user_id)
        ON DELETE SET NULL
);
