package com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.QCategory;
import com.BaGulBaGul.BaGulBaGul.domain.event.QEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.QEventCategory;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.QPost;
import com.BaGulBaGul.BaGulBaGul.domain.user.QUser;
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
public class FindEventByConditionImpl implements FindEventByCondition {
    @PersistenceContext
    private EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final FindEventByConditionApplier findEventByConditionApplier;

    @Override
    @Transactional
    public EventIdsWithTotalCountOfPageResult getEventIdsWithFetchJoinByConditionAndPageable(
            EventConditionalRequest eventConditionalRequest,
            Pageable pageable
    ) {
        QEvent event = QEvent.event;

        //조건에 만족하는 Event들의 id를 받아오는 쿼리
        JPAQuery<Long> idQuery = queryFactory.select(event.id).from(event);
        //조건 적용
        findEventByConditionApplier.applyCondition(idQuery, eventConditionalRequest, event);
        //페이징 적용
        applyPageable(idQuery, pageable);
        //id를 받아옴
        List<Long> ids = idQuery.fetch();

        //count를 따로 계산해줌
        Long totalCount = getCountQuery(eventConditionalRequest).fetchFirst();

        return EventIdsWithTotalCountOfPageResult.builder()
                .eventIds(ids)
                .TotalCount(totalCount)
                .build();
    }

    private <T> JPAQuery<T> applyPageable(
            JPAQuery<T> query,
            Pageable pageable
    ){
        List<OrderSpecifier> orderSpecifiers = findEventByConditionApplier.getOrderSpecifiers(pageable);
        query.orderBy(orderSpecifiers.stream().toArray(OrderSpecifier[]::new));
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        return query;
    }

    //    @Override
//    private <T> JPAQuery<T> applyFetchJoin(
//            JPAQuery<T> query,
//            QEvent event
//    ){
//        QEventCategory eventCategory = QEventCategory.eventCategory;
//        QCategory category = QCategory.category;
//        QPost post = QPost.post;
//        QUser user = QUser.user;
//
//        query.leftJoin(event.categories, eventCategory).fetchJoin();
//        query.leftJoin(eventCategory.category, category).fetchJoin();
//        query.join(event.post, post).fetchJoin();
//        query.join(post.user, user).fetchJoin();
//
//        return query;
//    }

    /*
     * count query는 fetch join이나 페이징이 필요 없이 조건만 적용하면 됨
     */
    private JPAQuery<Long> getCountQuery(EventConditionalRequest eventConditionalRequest) {
        QEvent event = QEvent.event;
        JPAQuery<Long> countQuery = queryFactory.select(event.count()).from(event);
        findEventByConditionApplier.applyCondition(countQuery, eventConditionalRequest, event);
        return countQuery;
    }
}
