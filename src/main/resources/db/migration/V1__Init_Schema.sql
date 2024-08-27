-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


-- -----------------------------------------------------
-- Table `resource`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `resource` (
  `resource_id` BIGINT NOT NULL AUTO_INCREMENT,
  `resource_key` VARCHAR(255) NULL DEFAULT NULL,
  `storage_vendor` VARCHAR(255) NULL DEFAULT NULL,
  `upload_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`resource_id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `s3temp_resource`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `s3temp_resource` (
  `resource_id` BIGINT NOT NULL,
  `upload_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`resource_id`),
  CONSTRAINT `FK__S3TEMP_RESOURCE__RESOURCE_ID`
    FOREIGN KEY (`resource_id`)
    REFERENCES `resource` (`resource_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `last_modified_at` DATETIME NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `image_uri` VARCHAR(255) NULL DEFAULT NULL,
  `nickname` VARCHAR(255) NULL DEFAULT NULL,
  `profile_message` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `UK__USER__NICKNAME` (`nickname` ASC) VISIBLE
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social_login_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social_login_user` (
  `social_login_user_id` VARCHAR(255) NOT NULL,
  `provider` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`social_login_user_id`),
  INDEX `FK__SOCIAL_LOGIN_USER__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__SOCIAL_LOGIN_USER__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_image` (
  `resource_id` BIGINT NOT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`resource_id`),
  INDEX `FK__USER_IMAGE__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__USER_IMAGE__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE,
  CONSTRAINT `FK__USER_IMAGE__RESOURCE_ID`
    FOREIGN KEY (`resource_id`)
    REFERENCES `resource` (`resource_id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `alarm`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alarm` (
  `alarm_id` BIGINT NOT NULL AUTO_INCREMENT,
  `checked` BIT(1) NULL DEFAULT NULL,
  `message` VARCHAR(255) NULL DEFAULT NULL,
  `subject_id` VARCHAR(255) NULL DEFAULT NULL,
  `time` DATETIME NULL DEFAULT NULL,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`alarm_id`),
  INDEX `FK__ALARM__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__ALARM__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `post` (
  `post_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `last_modified_at` DATETIME NULL DEFAULT NULL,
  `comment_count` INT NULL DEFAULT NULL,
  `content` VARCHAR(255) NULL DEFAULT NULL,
  `image_url` VARCHAR(255) NULL DEFAULT NULL,
  `like_count` INT NULL DEFAULT NULL,
  `tags` VARCHAR(255) NULL DEFAULT NULL,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `views` INT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  INDEX `FK__POST__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__POST__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE SET NULL
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `post_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `post_image` (
  `resource_id` BIGINT NOT NULL,
  `image_order` INT NULL DEFAULT NULL,
  `post_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`resource_id`),
  INDEX `FK__POST_IMAGE__POST_ID` (`post_id` ASC) VISIBLE,
  CONSTRAINT `FK__POST_IMAGE__RESOURCE_ID`
    FOREIGN KEY (`resource_id`)
    REFERENCES `resource` (`resource_id`),
  CONSTRAINT `FK__POST_IMAGE__POST_ID`
    FOREIGN KEY (`post_id`)
    REFERENCES `post` (`post_id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `post_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `post_comment` (
  `post_comment_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `last_modified_at` DATETIME NULL DEFAULT NULL,
  `comment_child_count` INT NULL DEFAULT NULL,
  `contnet` VARCHAR(255) NULL DEFAULT NULL,
  `like_count` INT NULL DEFAULT NULL,
  `post_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_comment_id`),
  INDEX `FK__POST_COMMENT__POST_ID` (`post_id` ASC) VISIBLE,
  INDEX `FK__POST_COMMENT__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__POST_COMMENT__POST_ID`
    FOREIGN KEY (`post_id`)
    REFERENCES `post` (`post_id`),
  CONSTRAINT `FK__POST_COMMENT__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE SET NULL
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `post_comment_child`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `post_comment_child` (
  `post_comment_child_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `last_modified_at` DATETIME NULL DEFAULT NULL,
  `content` VARCHAR(255) NULL DEFAULT NULL,
  `like_count` INT NULL DEFAULT NULL,
  `post_comment_id` BIGINT NULL DEFAULT NULL,
  `reply_target_user_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_comment_child_id`),
  INDEX `FK__POST_COMMENT_CHILD__POST_COMMENT_ID` (`post_comment_id` ASC) VISIBLE,
  INDEX `FK__POST_COMMENT_CHILD__REPLY_TARGET_USER_ID` (`reply_target_user_id` ASC) VISIBLE,
  INDEX `FK__POST_COMMENT_CHILD__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__POST_COMMENT_CHILD__POST_COMMENT_ID`
    FOREIGN KEY (`post_comment_id`)
    REFERENCES `post_comment` (`post_comment_id`),
  CONSTRAINT `FK__POST_COMMENT_CHILD__REPLY_TARGET_USER_ID`
    FOREIGN KEY (`reply_target_user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE SET NULL,
  CONSTRAINT `FK__POST_COMMENT_CHILD__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE SET NULL
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `post_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `post_like` (
  `post_like_id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_like_id`),
  UNIQUE INDEX `UK_POST_LIKE` (`post_id` ASC, `user_id` ASC) VISIBLE,
  INDEX `FK__POST_LIKE__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__POST_LIKE__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE SET NULL,
  CONSTRAINT `FK__POST_LIKE__POST_ID`
    FOREIGN KEY (`post_id`)
    REFERENCES `post` (`post_id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `post_comment_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `post_comment_like` (
  `post_comment_like_id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_comment_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_comment_like_id`),
  UNIQUE INDEX `UK_POST_COMMENT_LIKE` (`post_comment_id` ASC, `user_id` ASC) VISIBLE,
  INDEX `FK__POST_COMMENT_LIKE__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__POST_COMMENT_LIKE__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE SET NULL,
  CONSTRAINT `FK__POST_COMMENT_LIKE__POST_COMMENT_ID`
    FOREIGN KEY (`post_comment_id`)
    REFERENCES `post_comment` (`post_comment_id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `post_comment_child_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `post_comment_child_like` (
  `post_comment_child_like_id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_comment_child_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_comment_child_like_id`),
  UNIQUE INDEX `UK_POST_COMMENT_CHILD_LIKE` (`post_comment_child_id` ASC, `user_id` ASC) VISIBLE,
  INDEX `FK__POST_COMMENT_CHILD_LIKE__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__POST_COMMENT_CHILD_LIKE__POST_COMMENT_CHILD_ID`
    FOREIGN KEY (`post_comment_child_id`)
    REFERENCES `post_comment_child` (`post_comment_child_id`),
  CONSTRAINT `FK__POST_COMMENT_CHILD_LIKE__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE SET NULL
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event` (
  `event_id` BIGINT NOT NULL AUTO_INCREMENT,
  `location_abstract` VARCHAR(255) NULL DEFAULT NULL,
  `headcount_current` INT NULL DEFAULT NULL,
  `enddate` DATETIME NULL DEFAULT NULL,
  `location_full` VARCHAR(255) NULL DEFAULT NULL,
  `location_latitude` FLOAT NULL DEFAULT NULL,
  `location_longitude` FLOAT NULL DEFAULT NULL,
  `startdate` DATETIME NULL DEFAULT NULL,
  `headcount_total` INT NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `post_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  INDEX `FK__EVENT__POST_ID` (`post_id` ASC) VISIBLE,
  CONSTRAINT `FK__EVENT__POST_ID`
    FOREIGN KEY (`post_id`)
    REFERENCES `post` (`post_id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `category` (
  `category_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`category_id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `event_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event_category` (
  `category_id` BIGINT NOT NULL,
  `event_id` BIGINT NOT NULL,
  PRIMARY KEY (`category_id`, `event_id`),
  INDEX `FK__EVENT_CATEGORY__EVENT_ID` (`event_id` ASC) VISIBLE,
  CONSTRAINT `FK__EVENT_CATEGORY__EVENT_ID`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`event_id`),
  CONSTRAINT `FK__EVENT_CATEGORY__CATEGORY_ID`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`category_id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `event_calendar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `event_calendar` (
  `event_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`event_id`, `user_id`),
  INDEX `FK__EVENT_CALENDAR__USER_ID` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK__EVENT_CALENDAR__EVENT_ID`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`event_id`),
  CONSTRAINT `FK__EVENT_CALENDAR__USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `recruitment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `recruitment` (
  `recruitment_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `last_modified_at` DATETIME NULL DEFAULT NULL,
  `headcount_current` INT NULL DEFAULT NULL,
  `enddate` DATETIME NULL DEFAULT NULL,
  `startdate` DATETIME NULL DEFAULT NULL,
  `state` INT NULL DEFAULT NULL,
  `headcount_total` INT NULL DEFAULT NULL,
  `event_id` BIGINT NULL DEFAULT NULL,
  `post_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`recruitment_id`),
  INDEX `FK__RECRUITMENT__EVENT_ID` (`event_id` ASC) VISIBLE,
  INDEX `FK__RECRUITMENT__POST_ID` (`post_id` ASC) VISIBLE,
  CONSTRAINT `FK__RECRUITMENT__POST_ID`
    FOREIGN KEY (`post_id`)
    REFERENCES `post` (`post_id`),
  CONSTRAINT `FK__RECRUITMENT__EVENT_ID`
    FOREIGN KEY (`event_id`)
    REFERENCES `event` (`event_id`)
)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
