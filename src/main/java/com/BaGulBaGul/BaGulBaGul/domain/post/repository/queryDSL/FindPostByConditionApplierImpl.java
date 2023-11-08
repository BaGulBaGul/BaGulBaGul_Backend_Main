package com.BaGulBaGul.BaGulBaGul.domain.post.repository.queryDSL;
import com.BaGulBaGul.BaGulBaGul.domain.post.QPost;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Arrays;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FindPostByConditionApplierImpl implements FindPostByConditionApplier {

    @PersistenceContext
    private EntityManager em;
    private final JPAQueryFactory queryFactory;
    private static final String[] orderNames = {"createdAt", "views", "likeCount", "commentCount"};
    @Override
    public <T>JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            PostConditionalRequest postConditionalRequest,
            QPost post
    ){
        QUser user = QUser.user;
        if(postConditionalRequest.getTitle() != null) {
            query.where(post.title.contains(postConditionalRequest.getTitle()));
        }
        if(postConditionalRequest.getUsername() != null) {
            query.join(post.user, user);
            query.where(post.user.nickname.eq(postConditionalRequest.getUsername()));
        }
        if(postConditionalRequest.getTags() != null) {
            for(String tag : postConditionalRequest.getTags()) {
                query.where(post.tags.contains(tag));
            }
        }
        return query;
    }

    @Override
    public <T> JPAQuery<T> applyFetchJoin(
            JPAQuery<T> query,
            QPost post
    ) {
        return query;
    }

    @Override
    public Optional<OrderSpecifier> getOrderSpecifier(Sort.Order order) {
        String propertyName = order.getProperty();
        //만약 지원하는 정렬이 있다면 OrderSpecifier을 반환
        if(Arrays.stream(orderNames).anyMatch(s -> s.equals(propertyName))){
            Order dir = com.querydsl.core.types.Order.ASC;
            if(order.getDirection().isDescending()) {
                dir = com.querydsl.core.types.Order.DESC;
            }
            return Optional.of(new OrderSpecifier(
                    dir,
                    Expressions.path(Object.class, QPost.post, propertyName)
            ));
        }
        //없으면 null 반환
        return null;
    }
}
