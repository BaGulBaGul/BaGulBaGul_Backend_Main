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
    @Builder.Default
    private Optional<EventSimpleResponse> eventInfo = Optional.empty();
    @Builder.Default
    private Optional<RecruitmentSimpleResponse> recruitmentInfo = Optional.empty();
    @Builder.Default
    private Optional<PostCommentInfo> commentInfo = Optional.empty();
    @Builder.Default
    private Optional<PostCommentChildInfo> commentChildInfo = Optional.empty();
}
