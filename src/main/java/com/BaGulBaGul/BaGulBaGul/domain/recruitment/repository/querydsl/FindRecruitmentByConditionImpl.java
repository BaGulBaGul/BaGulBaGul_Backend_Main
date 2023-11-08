package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.post.QPost;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.QRecruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentSimpleResponse;
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
public class FindRecruitmentByConditionImpl implements FindRecruitmentByCondition {
    @PersistenceContext
    private EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final FindRecruitmentByConditionApplier findRecruitmentByConditionApplier;

    @Override
    @Transactional
    public Page<RecruitmentSimpleResponse> getRecruitmentSimpleResponsePageByCondition(
            RecruitmentConditionalRequest recruitmentConditionalRequest,
            Pageable pageable
    ) {
        QRecruitment recruitment = QRecruitment.recruitment;

        //조건에 만족하는 Recruitment들의 id를 받아오는 쿼리
        JPAQuery<Long> idQuery = queryFactory.select(recruitment.id).from(recruitment);
        //조건 적용
        findRecruitmentByConditionApplier.applyCondition(idQuery, recruitmentConditionalRequest, recruitment);
        //페이징 적용
        applyPageable(idQuery, pageable);
        //id를 받아옴
        List<Long> ids = idQuery.fetch();

        //fetch join으로 Recruitment와 연관된 엔티티까지 한번에 받아옴
        JPAQuery<Recruitment> fetchQuery = queryFactory.select(recruitment)
                .from(recruitment)
                .where(recruitment.id.in(ids));
        applyFetchJoin(fetchQuery, recruitment);
        fetchQuery.fetch();

        //응답 dto로 변환. 페이징이 적용된 ids의 순서대로 담아줌.
        List<RecruitmentSimpleResponse> result = ids.stream()
                .map(x -> em.find(Recruitment.class, x))
                .map(RecruitmentSimpleResponse::of)
                .collect(Collectors.toList());

        //count를 따로 계산해줌
        Long count = getCountQuery(recruitmentConditionalRequest).fetchFirst();

        //Page의 구현체로 조합해서 반환
        return new PageImpl<>(result, pageable, count);
    }

    private <T> JPAQuery<T> applyPageable(
            JPAQuery<T> query,
            Pageable pageable
    ) {
        List<OrderSpecifier> orderSpecifiers = findRecruitmentByConditionApplier.getOrderSpecifiers(pageable);
        query.orderBy(orderSpecifiers.stream().toArray(OrderSpecifier[]::new));
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        return query;
    }

    private <T> JPAQuery<T> applyFetchJoin(
            JPAQuery<T> query,
            QRecruitment recruitment
    ) {
        QPost post = QPost.post;
        QUser user = QUser.user;
        query.join(recruitment.post, post).fetchJoin();
        query.join(post.user, user).fetchJoin();
        return query;
    }

    private JPAQuery<Long> getCountQuery(RecruitmentConditionalRequest recruitmentConditionalRequest) {
        QRecruitment recruitment = QRecruitment.recruitment;
        JPAQuery<Long> countQuery = queryFactory.select(recruitment.count()).from(recruitment);
        findRecruitmentByConditionApplier.applyCondition(countQuery, recruitmentConditionalRequest, recruitment);
        return countQuery;
    }
}
