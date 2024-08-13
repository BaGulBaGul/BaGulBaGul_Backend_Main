package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.querydsl;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.BaGulBaGul.BaGulBaGul.domain.event.QEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.QPost;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.queryDSL.FindPostByConditionApplier;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.QRecruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentConditionalRequest;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindRecruitmentByConditionApplierImpl implements FindRecruitmentByConditionApplier {
    @PersistenceContext
    private EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final FindPostByConditionApplier findPostByConditionApplier;

    private static final String[] orderNames = {"startDate", "endDate", "headCount"};

    @Override
    @Transactional
    public <T> JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            RecruitmentConditionalRequest recruitmentConditionalRequest,
            QRecruitment recruitment
    ) {
        //recruitment 자체 조건 적용
        //삭제되지 않은 모집글이여야 함
        query.where(recruitment.deleted.eq(false));
        //연결된 이벤트의 id 조건 적용
        if(recruitmentConditionalRequest.getEventId() != null) {
            QEvent event = QEvent.event;
            query.join(recruitment.event, event);
            query.where(event.id.eq(recruitmentConditionalRequest.getEventId()));
        }
        //남은 자리 수 조건 적용
        if(recruitmentConditionalRequest.getLeftHeadCount() != null) {
            //최대 인원이 null이라면 무시
            BooleanExpression exp1 = recruitment.maxHeadCount
                    .isNull();
            //모집 인원 - 참여 인원 = 수용 인원이 leftHeadCount(남은 자리 수) 이상일 때
            BooleanExpression exp2 = recruitment.maxHeadCount
                    .subtract(recruitment.currentHeadCount)
                    .goe(recruitmentConditionalRequest.getLeftHeadCount());
            //둘 중 하나라도 만족하면 true
            query.where(
                    exp1.or(exp2)
            );
        }

        //post 관련 조건 적용
        QPost post = QPost.post;
        query.join(recruitment.post, post);
        findPostByConditionApplier.applyCondition(
                query,
                recruitmentConditionalRequest.toPostConditionalRequest(),
                recruitment.post
        );
        return query;
    }

    @Override
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

    @Override
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
                    Expressions.path(Object.class, QRecruitment.recruitment, propertyName)
            ));
        }
        //없다면 post에서 찾음 post에도 없다면 null 반환
        return findPostByConditionApplier.getOrderSpecifier(order);
    }
}
