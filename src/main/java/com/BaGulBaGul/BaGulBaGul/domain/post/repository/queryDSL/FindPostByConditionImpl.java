package com.BaGulBaGul.BaGulBaGul.domain.post.repository.queryDSL;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.QCategory;
import com.BaGulBaGul.BaGulBaGul.domain.post.QPost;
import com.BaGulBaGul.BaGulBaGul.domain.post.QPostCategory;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

public class FindPostByConditionImpl extends QuerydslRepositorySupport implements FindPostByCondition {

    public FindPostByConditionImpl() {
        super(Post.class);
    }


    /*
        Post에 대해 조건 검색하고 페이징을 적용한 결과를 반환
     */
    @Override
    @Transactional
    public Page<PostSimpleResponse> findPostSimpleResponsePageByCondition(
            PostConditionalRequest postConditionalRequest,
            Pageable pageable
    ) {
        QPost post = QPost.post;
        QPostCategory postCategory = QPostCategory.postCategory;
        QCategory category = QCategory.category;

        // select
        JPQLQuery<Post> query = from(post).select(post);

        // 조건 적용
        applyCondition(query, postConditionalRequest);
        // 페이징 적용
        applyPageable(query, pageable);
        // fetch join
        query.join(post.categories, postCategory).fetchJoin();
        query.join(postCategory.category, category).fetchJoin();
        // fetch 후 응답 dto로 변환
        List<PostSimpleResponse> result = query.fetch().stream().map(PostSimpleResponse::of).collect(Collectors.toList());
        Long count = getCountQuery(postConditionalRequest).fetchFirst();
        return new PageImpl<>(result, pageable, count);
    }

    /*
        쿼리에 조건 검색 구현
     */
    private <T> JPQLQuery<T> applyCondition(
            JPQLQuery<T> query,
            PostConditionalRequest postConditionalRequest
    ){
        QPost post = QPost.post;
        QUser user = QUser.user;
        QPostCategory postCategory = QPostCategory.postCategory;
        // 조건
        if(postConditionalRequest.getType() != null) {
            query.where(post.type.eq(postConditionalRequest.getType()));
        }
        if(postConditionalRequest.getTitle() != null) {
            query.where(post.title.contains(postConditionalRequest.getTitle()));
        }
        if(postConditionalRequest.getUsername() != null) {
            query.join(post.user, user);
            query.where(post.user.nickname.eq(postConditionalRequest.getUsername()));
        }
        if(postConditionalRequest.getMinimumStartDate() != null) {
            query.where(post.startDate.goe(postConditionalRequest.getMinimumStartDate()));
        }
        if(postConditionalRequest.getTags() != null) {
            for(String tag : postConditionalRequest.getTags()) {
                query.where(post.tags.contains(tag));
            }
        }
        if(postConditionalRequest.getCategories() != null) {
            int categoryNum = 0;
            // group by보다 join을 여러번 하는 것이 성능이 3배 이상 높게 나왔음(특히 카테고리 수가 적을 수록)
            for(String categoryName : postConditionalRequest.getCategories()) {
                QCategory category = new QCategory("category" + categoryNum++);
                query.join(post.categories, postCategory);
                query.join(postCategory.category, category);
                query.where(category.name.eq(categoryName));
            }
        }
        return query;
    }

    /*
        Spring의 Pageable의 정렬, offset, limit 정보를 이용해 페이징 수행
     */
    private <T> JPQLQuery<T> applyPageable(
            JPQLQuery<T> query,
            Pageable pageable
    ){
        //쿼리에 페이징 적용
        query.orderBy(getOrderSpecifiers(pageable).stream().toArray(OrderSpecifier[]::new));
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        return query;
    }

    /*
        Spring의 Pageable의 Sort를 queryDSL의 OrderSpecifier로 변환
     */
    private List<OrderSpecifier> getOrderSpecifiers(
            Pageable pageable
    ) {
        final String[] orderNames = {"createdAt","likeCount","startDate"};
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if(!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order dir = Order.ASC;
                if(order.getDirection().isDescending()) {
                    dir = Order.DESC;
                }

                String propertyName = order.getProperty();
                if(Arrays.stream(orderNames).anyMatch(s -> s.equals(propertyName))){
                        orderSpecifiers.add(
                                new OrderSpecifier(
                                        dir,
                                        Expressions.path(Object.class, QPost.post, propertyName)
                                )
                        );
                        break;
                }
            }
        }
        return orderSpecifiers;
    }

    /*
        조건 검색만 적용해서 count 전용 쿼리를 반환
     */
    private JPQLQuery<Long> getCountQuery(PostConditionalRequest postConditionalRequest) {
        QPost post = QPost.post;
        JPQLQuery<Long> jpqlQuery = from(post).select(post.count());
        applyCondition(jpqlQuery, postConditionalRequest);
        return jpqlQuery;
    }
}
