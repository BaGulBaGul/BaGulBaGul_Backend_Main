package com.BaGulBaGul.BaGulBaGul.domain.report.repository.querydsl;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.BaGulBaGul.BaGulBaGul.domain.report.QReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.FindReportStatusByConditionRequest;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class FindReportStatusByConditionImpl implements FindReportStatusByCondition {

    @PersistenceContext
    private EntityManager em;
    private final JPAQueryFactory queryFactory;

    private static final String[] orderNames = {"createdAt", "totalReportCount"};

    @Override
    @Transactional
    public ReportStatusIdsWithTotalCountOfPageResult findReportStatusIdsByConditionAndPageable(
            FindReportStatusByConditionRequest conditionRequest, Pageable pageable) {
        QReportStatus qReportStatus = QReportStatus.reportStatus;
        JPAQuery<ReportStatus> idQuery = queryFactory.select(qReportStatus).from(qReportStatus);
        applyCondition(idQuery, conditionRequest);
        applyPageable(idQuery, pageable);

        List<ReportStatus> reportStatuses = idQuery.fetch();
        Long count = getCountQuery(conditionRequest).fetchFirst();

        return ReportStatusIdsWithTotalCountOfPageResult.builder()
                .reportStatuses(reportStatuses)
                .totalCount(count)
                .build();
    }

    private <T> JPAQuery<T> applyCondition(JPAQuery<T> query, FindReportStatusByConditionRequest conditionRequest) {
        QReportStatus qReportStatus = QReportStatus.reportStatus;
        // 진행 상태 조건 적용
        if(conditionRequest.getReportStatusState() != null) {
            query.where(qReportStatus.state.eq(conditionRequest.getReportStatusState()));
        }
        // 게시물 타입 조건 적용
        List<ReportContentType> reportContentTypes = conditionRequest.getReportContentTypes();
        if(reportContentTypes != null && !reportContentTypes.isEmpty()) {
            List<String> reportContentTypeNames = reportContentTypes.stream().map(ReportContentType::name)
                    .collect(Collectors.toList());
            query.where(qReportStatus.dType.in(reportContentTypeNames));
        }
        return query;
    }

    private <T> JPAQuery<T> applyPageable(
            JPAQuery<T> query,
            Pageable pageable
    ){
        List<OrderSpecifier> orderSpecifiers = getOrderSpecifiers(pageable);
        query.orderBy(orderSpecifiers.stream().toArray(OrderSpecifier[]::new));
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        return query;
    }

    private List<OrderSpecifier> getOrderSpecifiers(
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

    private Optional<OrderSpecifier> getOrderSpecifier(Sort.Order order) {
        String propertyName = order.getProperty();
        //만약 지원하는 정렬이 있다면 OrderSpecifier을 반환
        if(Arrays.stream(orderNames).anyMatch(s -> s.equals(propertyName))){
            Order dir = Order.ASC;
            if(order.getDirection().isDescending()) {
                dir = Order.DESC;
            }
            return Optional.of(new OrderSpecifier(
                    dir,
                    Expressions.path(Object.class, QReportStatus.reportStatus, propertyName)
            ));
        }
        // 지원하는 정렬이 없다면 null 반환
        return null;
    }

    private JPAQuery<Long> getCountQuery(FindReportStatusByConditionRequest conditionRequest) {
        QReportStatus qReportStatus = QReportStatus.reportStatus;
        JPAQuery<Long> countQuery = queryFactory.select(qReportStatus.count()).from(qReportStatus);
        applyCondition(countQuery, conditionRequest);
        return countQuery;
    }
}
