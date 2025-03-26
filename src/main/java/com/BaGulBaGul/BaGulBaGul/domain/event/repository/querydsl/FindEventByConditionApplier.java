package com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.event.QEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/*
 * 조건 검색의 재사용을 위해 조건검색 로직은 따로 분리했다.
 * Post가 Event와 Recruitment에 포함되기 때문에
 * 각 ConditionApplier에서 FindPostByConditionApplier을 통해 Post조건 검색과 페이지 정렬을 위한 querydsl 로직을 재사용할 수 있다.
 */
public interface FindEventByConditionApplier {
    <T> JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            EventConditionalRequest eventConditionalRequest,
            QEvent event
    );

    List<OrderSpecifier> getOrderSpecifiers(
            Pageable pageable
    );
    Optional<OrderSpecifier> getOrderSpecifier(Sort.Order order);
}
