package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentConditionalRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface FindRecruitmentByCondition {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    class RecruitmentIdsWithTotalCountOfPageResult {
        private List<Long> recruitmentIds;
        private Long totalCount;
    }

    @Transactional
    RecruitmentIdsWithTotalCountOfPageResult getRecruitmentIdsByConditionAndPageable(
            RecruitmentConditionalRequest recruitmentConditionalRequest,
            Pageable pageable
    );
}
