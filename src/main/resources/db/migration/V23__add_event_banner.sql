-- 이벤트 베너 테이블 생성
CREATE TABLE `event_banner` (
    event_banner_id BIGINT NOT NULL,
    event_id BIGINT NULL,
    event_banner_image_resource_id BIGINT NULL,
    PRIMARY KEY (event_banner_id),
    CONSTRAINT FK__EVENT_BANNER__EVENT_ID
        FOREIGN KEY (event_id)
        REFERENCES event (event_id)
        ON DELETE SET NULL,
    CONSTRAINT FK__EVENT_BANNER__EVENT_BANNER_IMAGE_RESOURCE_ID
        FOREIGN KEY (event_banner_image_resource_id)
        REFERENCES resource (resource_id)
);

-- 초기 메인 베너 세팅(5개)
INSERT INTO `event_banner` values(1, null, null);
INSERT INTO `event_banner` values(2, null, null);
INSERT INTO `event_banner` values(3, null, null);
INSERT INTO `event_banner` values(4, null, null);
INSERT INTO `event_banner` values(5, null, null);