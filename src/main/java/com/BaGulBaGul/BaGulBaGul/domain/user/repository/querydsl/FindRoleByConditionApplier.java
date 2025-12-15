package com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.user.QRole;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SearchRoleRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface FindRoleByConditionApplier {

    <T> JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            SearchRoleRequest searchRoleRequest,
            QRole role
    );
    List<OrderSpecifier> getOrderSpecifiers(
            Pageable pageable
    );
    Optional<OrderSpecifier> getOrderSpecifier(Sort.Order order);
}
