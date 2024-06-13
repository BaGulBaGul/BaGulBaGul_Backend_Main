-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bagulbagul
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bagulbagul
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bagulbagul` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `bagulbagul` ;

-- -----------------------------------------------------
-- Table `bagulbagul`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`user` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `last_modified_at` DATETIME NULL DEFAULT NULL,
  `deleted` BIT(1) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `image_uri` VARCHAR(255) NULL DEFAULT NULL,
  `nickname` VARCHAR(255) NULL DEFAULT NULL,
  `profile_message` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `UK_n4swgcf30j6bmtb4l4cjryuym` (`nickname` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`alarm`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`alarm` (
  `alarm_id` BIGINT NOT NULL AUTO_INCREMENT,
  `checked` BIT(1) NULL DEFAULT NULL,
  `message` VARCHAR(255) NULL DEFAULT NULL,
  `subject_id` VARCHAR(255) NULL DEFAULT NULL,
  `time` DATETIME NULL DEFAULT NULL,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`alarm_id`),
  INDEX `FKd6g1gp6sn8nt3ku8y2mgu41vs` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FKd6g1gp6sn8nt3ku8y2mgu41vs`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`category` (
  `category_id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`post` (
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
  INDEX `FK72mt33dhhs48hf9gcqrq4fxte` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK72mt33dhhs48hf9gcqrq4fxte`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`event` (
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
  INDEX `FK94te67pf2d3omau6ge33eyve4` (`post_id` ASC) VISIBLE,
  CONSTRAINT `FK94te67pf2d3omau6ge33eyve4`
    FOREIGN KEY (`post_id`)
    REFERENCES `bagulbagul`.`post` (`post_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`event_calendar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`event_calendar` (
  `event_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`event_id`, `user_id`),
  INDEX `FKlogutb93yd4py8gdpybb696bt` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FKe1pikvydqc9go89qotgmvgkj3`
    FOREIGN KEY (`event_id`)
    REFERENCES `bagulbagul`.`event` (`event_id`),
  CONSTRAINT `FKlogutb93yd4py8gdpybb696bt`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`event_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`event_category` (
  `category_id` BIGINT NOT NULL,
  `event_id` BIGINT NOT NULL,
  PRIMARY KEY (`category_id`, `event_id`),
  INDEX `FK24ud2uucu4h8ga1ois1mnalo8` (`event_id` ASC) VISIBLE,
  CONSTRAINT `FK24ud2uucu4h8ga1ois1mnalo8`
    FOREIGN KEY (`event_id`)
    REFERENCES `bagulbagul`.`event` (`event_id`),
  CONSTRAINT `FKpwl2b1ylc09urqr0c4n18io`
    FOREIGN KEY (`category_id`)
    REFERENCES `bagulbagul`.`category` (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`post_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`post_comment` (
  `post_comment_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `last_modified_at` DATETIME NULL DEFAULT NULL,
  `comment_child_count` INT NULL DEFAULT NULL,
  `contnet` VARCHAR(255) NULL DEFAULT NULL,
  `like_count` INT NULL DEFAULT NULL,
  `post_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_comment_id`),
  INDEX `FKna4y825fdc5hw8aow65ijexm0` (`post_id` ASC) VISIBLE,
  INDEX `FKtc1fl97yq74q7j8i08ds731s1` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FKna4y825fdc5hw8aow65ijexm0`
    FOREIGN KEY (`post_id`)
    REFERENCES `bagulbagul`.`post` (`post_id`),
  CONSTRAINT `FKtc1fl97yq74q7j8i08ds731s1`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`post_comment_child`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`post_comment_child` (
  `post_comment_child_id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `last_modified_at` DATETIME NULL DEFAULT NULL,
  `content` VARCHAR(255) NULL DEFAULT NULL,
  `like_count` INT NULL DEFAULT NULL,
  `post_comment_id` BIGINT NULL DEFAULT NULL,
  `reply_target_user_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_comment_child_id`),
  INDEX `FK1gtj05924yg1ghgdmhk20dyo4` (`post_comment_id` ASC) VISIBLE,
  INDEX `FKae81bvqnyn47hde4ai2f1ntc7` (`reply_target_user_id` ASC) VISIBLE,
  INDEX `FKgwfillo5clhjc31rphprov1lo` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK1gtj05924yg1ghgdmhk20dyo4`
    FOREIGN KEY (`post_comment_id`)
    REFERENCES `bagulbagul`.`post_comment` (`post_comment_id`),
  CONSTRAINT `FKae81bvqnyn47hde4ai2f1ntc7`
    FOREIGN KEY (`reply_target_user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`),
  CONSTRAINT `FKgwfillo5clhjc31rphprov1lo`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`post_comment_child_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`post_comment_child_like` (
  `post_comment_child_like_id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_comment_child_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_comment_child_like_id`),
  UNIQUE INDEX `UK_POST_LIKE` (`post_comment_child_id` ASC, `user_id` ASC) VISIBLE,
  INDEX `FK9jbx5hbuw5o94vooomi5ygut4` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK9j9a7bogvqtbpydvo1pcxu09d`
    FOREIGN KEY (`post_comment_child_id`)
    REFERENCES `bagulbagul`.`post_comment_child` (`post_comment_child_id`),
  CONSTRAINT `FK9jbx5hbuw5o94vooomi5ygut4`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`post_comment_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`post_comment_like` (
  `post_comment_like_id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_comment_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_comment_like_id`),
  UNIQUE INDEX `UK_POST_LIKE` (`post_comment_id` ASC, `user_id` ASC) VISIBLE,
  INDEX `FKcflst7s3kfhrepifbsibaqr6b` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FKcflst7s3kfhrepifbsibaqr6b`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`),
  CONSTRAINT `FKolgmkn6p5x2cvhf9gav99mr1o`
    FOREIGN KEY (`post_comment_id`)
    REFERENCES `bagulbagul`.`post_comment` (`post_comment_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`resource`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`resource` (
  `resource_id` BIGINT NOT NULL AUTO_INCREMENT,
  `resource_key` VARCHAR(255) NULL DEFAULT NULL,
  `storage_vendor` VARCHAR(255) NULL DEFAULT NULL,
  `upload_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`resource_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`post_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`post_image` (
  `resource_id` BIGINT NOT NULL,
  `image_order` INT NULL DEFAULT NULL,
  `post_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`resource_id`),
  INDEX `FKsip7qv57jw2fw50g97t16nrjr` (`post_id` ASC) VISIBLE,
  CONSTRAINT `FKjsyul1kl9ix208m0bur3ref4j`
    FOREIGN KEY (`resource_id`)
    REFERENCES `bagulbagul`.`resource` (`resource_id`),
  CONSTRAINT `FKsip7qv57jw2fw50g97t16nrjr`
    FOREIGN KEY (`post_id`)
    REFERENCES `bagulbagul`.`post` (`post_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`post_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`post_like` (
  `post_like_id` BIGINT NOT NULL AUTO_INCREMENT,
  `post_id` BIGINT NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`post_like_id`),
  UNIQUE INDEX `UK_POST_LIKE` (`post_id` ASC, `user_id` ASC) VISIBLE,
  INDEX `FKhuh7nn7libqf645su27ytx21m` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FKhuh7nn7libqf645su27ytx21m`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`),
  CONSTRAINT `FKj7iy0k7n3d0vkh8o7ibjna884`
    FOREIGN KEY (`post_id`)
    REFERENCES `bagulbagul`.`post` (`post_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`recruitment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`recruitment` (
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
  INDEX `FKgbvmqhem7n9ck38ue2rbqpt96` (`event_id` ASC) VISIBLE,
  INDEX `FKd0drim1jyofjkmk72qxgmna7m` (`post_id` ASC) VISIBLE,
  CONSTRAINT `FKd0drim1jyofjkmk72qxgmna7m`
    FOREIGN KEY (`post_id`)
    REFERENCES `bagulbagul`.`post` (`post_id`),
  CONSTRAINT `FKgbvmqhem7n9ck38ue2rbqpt96`
    FOREIGN KEY (`event_id`)
    REFERENCES `bagulbagul`.`event` (`event_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`s3temp_resource`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`s3temp_resource` (
  `resource_id` BIGINT NOT NULL,
  `upload_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`resource_id`),
  CONSTRAINT `FKoad6srvomeiy11qcmnirm4lee`
    FOREIGN KEY (`resource_id`)
    REFERENCES `bagulbagul`.`resource` (`resource_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`social_login_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`social_login_user` (
  `social_login_user_id` VARCHAR(255) NOT NULL,
  `provider` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`social_login_user_id`),
  INDEX `FK8cgs1qx1ajef2oxcuswq7qloh` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK8cgs1qx1ajef2oxcuswq7qloh`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bagulbagul`.`user_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bagulbagul`.`user_image` (
  `resource_id` BIGINT NOT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`resource_id`),
  INDEX `FK5m3lhx7tcj9h9ju10xo4ruqcn` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK5m3lhx7tcj9h9ju10xo4ruqcn`
    FOREIGN KEY (`user_id`)
    REFERENCES `bagulbagul`.`user` (`user_id`),
  CONSTRAINT `FKgo2ka2x4bihf60mdi93q2mwb`
    FOREIGN KEY (`resource_id`)
    REFERENCES `bagulbagul`.`resource` (`resource_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
