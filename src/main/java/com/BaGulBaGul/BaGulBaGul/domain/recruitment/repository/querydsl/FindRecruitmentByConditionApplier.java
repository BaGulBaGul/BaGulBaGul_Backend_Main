package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.QRecruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentConditionalRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface FindRecruitmentByConditionApplier {
    <T> JPAQuery<T> applyCondition(
            JPAQuery<T> query,
            RecruitmentConditionalRequest recruitmentConditionalRequest,
            QRecruitment recruitment
    );

    List<OrderSpecifier> getOrderSpecifiers(
            Pageable pageable
    );
    Optional<OrderSpecifier> getOrderSpecifier(Sort.Order order);
}
