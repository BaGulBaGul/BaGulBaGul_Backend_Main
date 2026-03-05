package com.BaGulBaGul.BaGulBaGul.domain.recruitment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.post.PostTestUtils;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleResponse;

public class RecruitmentTestUtils {
    public static void assertRecruitmentSimpleInfo(RecruitmentSimpleInfo recruitmentSimpleInfo, Recruitment recruitment) {
        assertThat(recruitmentSimpleInfo.getRecruitmentId()).isEqualTo(recruitment.getId());
        assertThat(recruitmentSimpleInfo.getState()).isEqualTo(recruitment.getState());
        assertThat(recruitmentSimpleInfo.getCurrentHeadCount()).isEqualTo(recruitment.getCurrentHeadCount());
        assertThat(recruitmentSimpleInfo.getMaxHeadCount()).isEqualTo(recruitment.getMaxHeadCount());
        assertThat(recruitmentSimpleInfo.getStartDate()).isEqualTo(recruitment.getStartDate());
        assertThat(recruitmentSimpleInfo.getEndDate()).isEqualTo(recruitment.getEndDate());
    }

    public static void assertRecruitmentSimpleResponse(RecruitmentSimpleResponse recruitmentSimpleResponse, Recruitment recruitment) {
        assertRecruitmentSimpleInfo(recruitmentSimpleResponse.getRecruitment(), recruitment);
        PostTestUtils.assertPostSimpleInfo(recruitmentSimpleResponse.getPost(), recruitment.getPost());
    }
}
