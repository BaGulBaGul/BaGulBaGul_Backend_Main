package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantStatusRegisterRequest {
    //현재 인원
    @Min(value = 0, message = "현재 인원은 {value}명 이상이여야 합니다")
    private Integer currentHeadCount;
    //모집 인원
    @Min(value = 1, message = "모집 인원은 {value}명 이상이여야 합니다")
    private Integer maxHeadCount;

    @AssertTrue(message = "참여 인원은 0명 이상이여야 합니다")
    public boolean isCurrentHeadCountNonNegative() {
        //값null이거나 0명 이상이여야 함
        return currentHeadCount == null || currentHeadCount >= 0;
    }

    @AssertTrue(message = "모집 인원은 1명 이상이여야 합니다")
    private boolean isMaxHeadCountPositive() {
        //null이거나 1명 이상이여야 함
        return maxHeadCount == null || maxHeadCount >= 1;
    }

    @AssertTrue(message = "현재 인원이 모집 인원보다 클 수 없습니다")
    private boolean isCurrentHeadCount_LessThanOrEqual_MaxHeadCount() {
        //둘 중 하나라도 null일 경우 비교 무시
        return currentHeadCount == null || maxHeadCount == null || currentHeadCount <= maxHeadCount;
    }
}
