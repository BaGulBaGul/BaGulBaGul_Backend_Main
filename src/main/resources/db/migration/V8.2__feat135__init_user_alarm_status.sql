#기존 유저에 대한 alarm status 생성
#기존 유저의 알람 개수와 체크하지 않은 알람 개수를 집계해서 넣어줌
insert into `user_alarm_status` (user_id, total_alarm_count, unchecked_alarm_count)
select
    usr.user_id,
    coalesce(status.total_alarm_count, 0) as total_alarm_count,
    coalesce(status.unchecked_alarm_count, 0) as unchecked_alarm_count
from
    `user` usr
left outer join (
    select
        al.user_id,
        count(*) as total_alarm_count,
        count(case when al.checked = false then 1 ELSE 0 end) as unchecked_alarm_count
    from
        `alarm` al
    group by
        al.user_id
) status on usr.user_id = status.user_id;