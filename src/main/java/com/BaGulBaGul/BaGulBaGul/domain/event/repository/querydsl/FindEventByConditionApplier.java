package com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.event.QEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventConditionalRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface FindEventByConditionApplier {
    <T> JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            EventConditionalRequest eventConditionalRequest,
            QEvent event
    );

//    <T> JPAQuery<T> applyFetchJoin(
//            JPAQuery<T> query,
//            QEvent event
//    );

    List<OrderSpecifier> getOrderSpecifiers(
            Pageable pageable
    );
    Optional<OrderSpecifier> getOrderSpecifier(Sort.Order order);
}
