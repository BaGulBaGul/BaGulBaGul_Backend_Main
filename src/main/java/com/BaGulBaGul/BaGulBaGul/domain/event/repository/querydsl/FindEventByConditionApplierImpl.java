package com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.BaGulBaGul.BaGulBaGul.domain.event.QCategory;
import com.BaGulBaGul.BaGulBaGul.domain.event.QEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.QEventCategory;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.QPost;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.queryDSL.FindPostByConditionApplier;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindEventByConditionApplierImpl implements FindEventByConditionApplier {
    @PersistenceContext
    private EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final FindPostByConditionApplier findPostByConditionApplier;
    private static final String[] orderNames = {"startDate", "endDate", "headCount"};

    @Override
    public <T> JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            EventConditionalRequest eventConditionalRequest,
            QEvent event
    ) {
        //이벤트 조건 적용
        //이벤트 타입
        if(eventConditionalRequest.getType() != null) {
            query.where(event.type.eq(eventConditionalRequest.getType()));
        }
        //장소 검색
        if(eventConditionalRequest.getLocation() != null) {
            query.where(event.fullLocation.contains(eventConditionalRequest.getLocation()));
        }
        //검색기간 적용
        if(eventConditionalRequest.getStartDate() != null && eventConditionalRequest.getEndDate() != null) {
            query.where(
                getDateSearchExpression(event, eventConditionalRequest.getStartDate(), eventConditionalRequest.getEndDate())
            );
        }
        //카테고리
        if(eventConditionalRequest.getCategories() != null) {
            QEventCategory postCategory = QEventCategory.eventCategory;
            int categoryNum = 0;
            // group by보다 join을 여러번 하는 것이 성능이 3배 이상 높게 나왔음(특히 카테고리 수가 적을 수록)
            for(String categoryName : eventConditionalRequest.getCategories()) {
                QCategory category = new QCategory("category" + categoryNum++);
                query.join(event.categories, postCategory);
                query.join(postCategory.category, category);
                query.where(category.name.eq(categoryName));
            }
        }
        //post 관련 조건 적용
        QPost post = QPost.post;
        query.join(event.post, post);
        findPostByConditionApplier.applyCondition(query, eventConditionalRequest.toPostConditionalRequest(), post);
        return query;
    }

    private BooleanExpression getDateSearchExpression(QEvent event, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {
        /*
         * 검색기간 사이에 진행되는 모든 이벤트를 검색
         * ss = 검색시작시간 se = 검색종료시간 es = 이벤트시작시간 ee = 이벤트종료시간
         * (ss<=es and es<=se) or (es<=ss and se<=ee) or (ss<=ee and ee<=se) or (ss<=es and ee<=se)
         * = not(se<es or ee<ss)
         * = (se>=es and ee>=ss)
         */
        //se>=es
        BooleanExpression condition1 = event.startDate.loe(searchEndDate);
        //ee>=ss
        BooleanExpression condition2 = event.endDate.goe(searchStartDate);
        return condition1.and(condition2);
    }

    public List<OrderSpecifier> getOrderSpecifiers(
            Pageable pageable
    ) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if(!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                getOrderSpecifier(order).ifPresent(orderSpecifiers::add);
            }
        }
        return orderSpecifiers;
    }

    public Optional<OrderSpecifier> getOrderSpecifier(Sort.Order order) {
        String propertyName = order.getProperty();
        //만약 지원하는 정렬이 있다면 OrderSpecifier을 반환
        if(Arrays.stream(orderNames).anyMatch(s -> s.equals(propertyName))){
            Order dir = Order.ASC;
            if(order.getDirection().isDescending()) {
                dir = Order.DESC;
            }
            return Optional.of(new OrderSpecifier(
                    dir,
                    Expressions.path(Object.class, QEvent.event, propertyName)
            ));
        }
        //없다면 post에서 찾음 post에도 없다면 null 반환
        return findPostByConditionApplier.getOrderSpecifier(order);
    }
}
