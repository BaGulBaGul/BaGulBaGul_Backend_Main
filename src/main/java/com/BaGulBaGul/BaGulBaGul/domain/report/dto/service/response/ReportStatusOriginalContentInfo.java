package com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostCommentChildInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostCommentInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleResponse;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatusOriginalContentInfo {
    private Optional<EventSimpleResponse> eventInfo;
    private Optional<RecruitmentSimpleResponse> recruitmentInfo;
    private Optional<PostCommentInfo> commentInfo;
    private Optional<PostCommentChildInfo> commentChildInfo;
}
