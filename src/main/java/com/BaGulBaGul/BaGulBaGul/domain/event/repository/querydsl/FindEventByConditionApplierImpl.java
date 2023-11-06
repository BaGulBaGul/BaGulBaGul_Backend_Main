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
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    private static final String[] orderNames = {"likeCount","startDate"};

    @Override
    public <T> JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            EventConditionalRequest eventConditionalRequest,
            QEvent event
    ) {
        // 조건
        if(eventConditionalRequest.getType() != null) {
            query.where(event.type.eq(eventConditionalRequest.getType()));
        }
        if(eventConditionalRequest.getMinimumStartDate() != null) {
            query.where(event.startDate.goe(eventConditionalRequest.getMinimumStartDate()));
        }
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

//    @Override
//    public <T> JPAQuery<T> applyFetchJoin(
//            JPAQuery<T> query,
//            QEvent event
//    ){
//        QEventCategory eventCategory = QEventCategory.eventCategory;
//        QCategory category = QCategory.category;
//        QPost post = QPost.post;
//        query.leftJoin(event.categories, eventCategory).fetchJoin();
//        query.leftJoin(eventCategory.category, category).fetchJoin();
//        query.join(event.post, post).fetchJoin();
//        findPostByConditionApplier.applyFetchJoin(query, post);
//        return query;
//    }

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
