package com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.QCategory;
import com.BaGulBaGul.BaGulBaGul.domain.event.QEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.QEventCategory;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.QPost;
import com.BaGulBaGul.BaGulBaGul.domain.user.QUser;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Page<EventSimpleResponse> getEventSimpleResponsePageByCondition(
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

        //fetch join으로 Event와 연관된 엔티티까지 한번에 받아옴
        JPAQuery<Event> fetchQuery = queryFactory.select(event)
                .from(event)
                .where(event.id.in(ids));
        applyFetchJoin(fetchQuery, event);
        fetchQuery.fetch();

        //응답 dto로 변환. 페이징이 적용된 ids의 순서대로 담아줌.
        List<EventSimpleResponse> result = ids.stream()
                .map(x -> em.find(Event.class, x))
                .map(EventSimpleResponse::of)
                .collect(Collectors.toList());

        //count를 따로 계산해줌
        Long count = getCountQuery(eventConditionalRequest).fetchFirst();

        //Page의 구현체로 조합해서 반환
        return new PageImpl<>(result, pageable, count);
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
    private <T> JPAQuery<T> applyFetchJoin(
            JPAQuery<T> query,
            QEvent event
    ){
        QEventCategory eventCategory = QEventCategory.eventCategory;
        QCategory category = QCategory.category;
        QPost post = QPost.post;
        QUser user = QUser.user;

        query.leftJoin(event.categories, eventCategory).fetchJoin();
        query.leftJoin(eventCategory.category, category).fetchJoin();
        query.join(event.post, post).fetchJoin();
        query.join(post.user, user).fetchJoin();

        return query;
    }

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
