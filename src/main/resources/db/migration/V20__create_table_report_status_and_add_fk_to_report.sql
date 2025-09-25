CREATE TABLE report_status (
    report_status_id BIGINT NOT NULL AUTO_INCREMENT,

    -- 신고 게시물 타입(이벤트, 모집글, 댓글, 대댓글)
    dtype VARCHAR(200) NOT NULL,
    -- 기본 테이블 관리 날짜 정보
    created_at datetime,
    last_modified_at datetime,
    -- 신고 종류 통계
    total_report_count INT,
    not_relevant_report_count INT,
    offensive_content_report_count INT,
    defamatory_report_count INT,
    ect_report_count INT,
    -- 처리 상태
    state VARCHAR(200),
    -- 처리 결과
    is_reported_content_deleted BOOLEAN NOT NULL,
    is_reported_content_writer_suspended BOOLEAN NOT NULL,

    -- 연결된 게시물 id
    event_id BIGINT,
    recruitment_id BIGINT,
    post_comment_id BIGINT,
    post_comment_child_id BIGINT,

    -- 활성화 된 신고 상태를 게시물 당 1개로 제한하기 위한 칼럼. UNIQUE와 함께 쓰인다
    active_event_id BIGINT GENERATED ALWAYS AS (
        CASE WHEN dtype = 'Event' and state = 'PROCEEDING' THEN event_id ELSE NULL END
    ) STORED,
    active_recruitment_id BIGINT GENERATED ALWAYS AS (
        CASE WHEN dtype = 'Recruitment' and state = 'PROCEEDING' THEN recruitment_id ELSE NULL END
    ) STORED,
    active_post_comment_id BIGINT GENERATED ALWAYS AS (
        CASE WHEN dtype = 'Comment' and state = 'PROCEEDING' THEN post_comment_id ELSE NULL END
    ) STORED,
    active_post_comment_child_id BIGINT GENERATED ALWAYS AS (
        CASE WHEN dtype = 'CommentChild' and state = 'PROCEEDING' THEN post_comment_child_id ELSE NULL END
    ) STORED,

    -- PK
    PRIMARY KEY (report_status_id),

    -- FK
    CONSTRAINT FK__REPORT_STATUS__EVENT_ID
        FOREIGN KEY (event_id)
        REFERENCES event (event_id),
--        ON DELETE CASCADE,
    CONSTRAINT FK__REPORT_STATUS__RECRUITMENT_ID
        FOREIGN KEY (recruitment_id)
        REFERENCES recruitment (recruitment_id),
--        ON DELETE CASCADE,
    CONSTRAINT FK__REPORT_STATUS__POST_COMMENT_ID
        FOREIGN KEY (post_comment_id)
        REFERENCES post_comment (post_comment_id),
--        ON DELETE CASCADE,
    CONSTRAINT FK__REPORT_STATUS__POST_COMMENT_CHILD_ID
        FOREIGN KEY (post_comment_child_id)
        REFERENCES post_comment_child (post_comment_child_id),
--        ON DELETE CASCADE,

    -- UNIQUE
    UNIQUE INDEX `UK__REPORT_STATUS__ACTIVE_EVENT_ID` (`active_event_id` ASC) VISIBLE,
    UNIQUE INDEX `UK__REPORT_STATUS__ACTIVE_RECRUITMENT_ID` (`active_recruitment_id` ASC) VISIBLE,
    UNIQUE INDEX `UK__REPORT_STATUS__ACTIVE_POST_COMMENT_ID` (`active_post_comment_id` ASC) VISIBLE,
    UNIQUE INDEX `UK__REPORT_STATUS__ACTIVE_POST_COMMENT_CHILD_ID` (`active_post_comment_child_id` ASC) VISIBLE
);

-- Add report_status_id to report table
ALTER TABLE report
    ADD COLUMN report_status_id BIGINT,
    ADD CONSTRAINT FK__report__report_status_id
        FOREIGN KEY (report_status_id)
        REFERENCES report_status (report_status_id);
--        ON DELETE CASCADE;
