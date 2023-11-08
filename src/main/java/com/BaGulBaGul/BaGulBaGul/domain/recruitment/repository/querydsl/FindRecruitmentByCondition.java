package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface FindRecruitmentByCondition {
    @Transactional
    Page<RecruitmentSimpleResponse> getRecruitmentSimpleResponsePageByCondition(
            RecruitmentConditionalRequest recruitmentConditionalRequest,
            Pageable pageable
    );
}
