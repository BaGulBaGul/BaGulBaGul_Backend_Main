package com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface FindEventByCondition {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    class EventIdsWithTotalCountOfPageResult {
        private List<Long> eventIds;
        private Long TotalCount;
    }


    /*
     * 원래 Page<EventSimpleResponse>를 바로 반환하도록 설계했으나 복잡한 변환 로직이 필요할 경우(ex 연결된 이미지를 정렬 순서대로 검색하고 url로 변환) 다른 서비스 계층에 의존해야 하기 때문에
     * 이 메서드를 호출한 서비스 계층에서 필요한 응답으로 바꿔주도록 설계를 변경했다.
     * 조건이 복잡해서 나중에 fetch join과 연계하기 위해 id를 반환한다(컬렉션 정합성). 호출한 서비스에서 이를 where in을 통해 fetch join해줘야 한다.
     */
    @Transactional
    EventIdsWithTotalCountOfPageResult getEventIdsByConditionAndPageable(
            EventConditionalRequest eventConditionalRequest,
            Pageable pageable
    );
}
