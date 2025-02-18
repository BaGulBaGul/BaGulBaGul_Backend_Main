package com.BaGulBaGul.BaGulBaGul.domain.ranking.repository;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public interface EventViewRankingRepository {
    //최근 7일간의 실시간 랭킹에서 특정 이벤트의 조회수 증가
    void increase7DaysViewCount(Long eventId, EventType eventType, Long amount);
    //최근 7일간의 실시간 랭킹에서 특정 이벤트의 조회수 감소
    void decrease7DaysViewCount(Long eventId, EventType eventType, Long amount);
    //최근 7일간의 특정 이벤트 타입에 대한 실시간 랭킹 top k개를 가져옴
    List<Long> getTopKRankEventFrom7DaysViewCount(EventType eventType, int k);

    //특정 날짜에서 특정 이벤트의 조회수를 증가
    void increaseDayViewCount(Long eventId, EventType eventType, LocalDateTime time, Long amount);
    //특정 날짜의 특정 이벤트 타입에 대해 순회
    Iterator<Entry<String, String>> getDayViewCountIterator(EventType eventType, LocalDateTime time);
    //특정 날짜의 조회수 정보를 전부 삭제
    void deleteAllDayViewCountByTime(EventType eventType, LocalDateTime time);
}
