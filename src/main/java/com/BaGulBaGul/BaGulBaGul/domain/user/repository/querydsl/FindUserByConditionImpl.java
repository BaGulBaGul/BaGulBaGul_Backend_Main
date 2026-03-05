package com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.user.QUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FindUserByConditionImpl implements FindUserByCondition {
    @PersistenceContext
    private EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final FindUserByConditionApplier findUserByConditionApplier;

    @Override
    public UserIdsWithTotalCount getUserIdsByCondition(UserSearchRequest userSearchRequest, Pageable pageable) {
        QUser qUser = QUser.user;
        JPAQuery<Long> idQuery = queryFactory.select(qUser.id).from(qUser);
        findUserByConditionApplier.applyCondition(idQuery, userSearchRequest, qUser);
        //페이징 적용
        applyPageable(idQuery, pageable);
        //id를 받아옴
        List<Long> ids = idQuery.fetch();

        Long totalCount = getCountQuery(userSearchRequest).fetchFirst();
        return UserIdsWithTotalCount.builder()
                .userIds(ids)
                .TotalCount(totalCount)
                .build();
    }

    private <T> JPAQuery<T> applyPageable(
            JPAQuery<T> query,
            Pageable pageable
    ){
        List<OrderSpecifier> orderSpecifiers = findUserByConditionApplier.getOrderSpecifiers(pageable);
        query.orderBy(orderSpecifiers.stream().toArray(OrderSpecifier[]::new));
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        return query;
    }

    private JPAQuery<Long> getCountQuery(UserSearchRequest userSearchRequest) {
        QUser qUser = QUser.user;
        JPAQuery<Long> countQuery = queryFactory.select(qUser.count()).from(qUser);
        findUserByConditionApplier.applyCondition(countQuery, userSearchRequest, qUser);
        return countQuery;
    }
}
