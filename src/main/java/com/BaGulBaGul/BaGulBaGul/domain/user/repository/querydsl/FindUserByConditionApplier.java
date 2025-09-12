package com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.event.QEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.QUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface FindUserByConditionApplier {
    <T> JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            UserSearchRequest userSearchRequest,
            QUser user
    );

    List<OrderSpecifier> getOrderSpecifiers(
            Pageable pageable
    );
    Optional<OrderSpecifier> getOrderSpecifier(Sort.Order order);
}
