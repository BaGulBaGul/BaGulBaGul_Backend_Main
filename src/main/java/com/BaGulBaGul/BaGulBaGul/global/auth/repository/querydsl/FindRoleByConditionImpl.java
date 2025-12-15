package com.BaGulBaGul.BaGulBaGul.global.auth.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.global.auth.QRole;
import com.BaGulBaGul.BaGulBaGul.global.auth.QRolePermission;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class FindRoleByConditionImpl implements FindRoleByCondition {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;
    private final FindRoleByConditionApplier findRoleByConditionApplier;

    @Override
    public RoleNamesWithTotalCount findRoleNamesByCondition(SearchRoleRequest request, Pageable pageable) {
        QRole role = QRole.role;
        JPAQuery<String> nameQuery = queryFactory.select(role.name).from(role);
        findRoleByConditionApplier.applyCondition(nameQuery, request, role);
        //페이징 적용
        applyPageable(nameQuery, pageable);
        //id(이름)을 받아옴
        List<String> roleNames = nameQuery.fetch();

        long totalCount = getCountQuery(request, pageable).fetchFirst();

        return RoleNamesWithTotalCount.builder()
                .roleNames(roleNames)
                .totalCount(totalCount)
                .build();
    }

    private <T> JPAQuery<T> applyPageable(
            JPAQuery<T> query,
            Pageable pageable
    ){
        List<OrderSpecifier> orderSpecifiers = findRoleByConditionApplier.getOrderSpecifiers(pageable);
        query.orderBy(orderSpecifiers.stream().toArray(OrderSpecifier[]::new));
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        return query;
    }

    private JPAQuery<Long> getCountQuery(SearchRoleRequest request, Pageable pageable) {
        QRole role = QRole.role;
        JPAQuery<Long> countQuery = queryFactory.select(role.count()).from(role);
        findRoleByConditionApplier.applyCondition(countQuery, request, role);
        applyPageable(countQuery, pageable);
        return countQuery;
    }
}
