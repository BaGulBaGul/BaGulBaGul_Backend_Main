-- dtype 추가
ALTER TABLE post ADD COLUMN dtype VARCHAR(31) NULL;

-- 기존 Post 행에 타입 적용
UPDATE post p INNER JOIN event e ON p.post_id = e.post_id SET p.dtype = 'Event';
UPDATE post p INNER JOIN recruitment r ON p.post_id = r.post_id SET p.dtype = 'Recruitment';