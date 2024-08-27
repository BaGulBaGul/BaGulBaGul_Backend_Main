package com.BaGulBaGul.BaGulBaGul.domain.post.repository.queryDSL;

import com.BaGulBaGul.BaGulBaGul.domain.post.QPost;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostConditionalRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Optional;
import org.springframework.data.domain.Sort.Order;

public interface FindPostByConditionApplier {
    public <T> JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            PostConditionalRequest postConditionalRequest,
            QPost post
    );

    <T> JPAQuery<T> applyFetchJoin(
            JPAQuery<T> query,
            QPost post
    );

    public Optional<OrderSpecifier> getOrderSpecifier(Order order);
}
