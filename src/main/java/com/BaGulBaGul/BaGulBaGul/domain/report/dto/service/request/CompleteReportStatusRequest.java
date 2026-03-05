package com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import java.time.LocalDateTime;
import javax.validation.Valid;
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
public class CompleteReportStatusRequest {
    /**
     * 신고 대상 게시물 삭제 여부
     */
    boolean deleteTargetContent;
    /**
     * 신고 대상 게시물 작성자 정지 요청
     * null이면 정지하지 않음.
     */
    @Valid
    SuspendUserRequest suspendUserRequest;
}
