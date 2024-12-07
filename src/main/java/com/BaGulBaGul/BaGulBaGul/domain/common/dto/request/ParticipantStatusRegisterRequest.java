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

    @AssertTrue(message = "현재 인원이 모집 인원보다 클 수 없습니다")
    private boolean isCurrentHeadCount_LessThanOrEqual_MaxHeadCount() {
        if(currentHeadCount == null || maxHeadCount == null) {
            return true;
        }
        return currentHeadCount <= maxHeadCount;
    }
}
