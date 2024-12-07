package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import javax.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantStatusModifyRequest {
    //현재 인원
    @Builder.Default
    private JsonNullable<Integer> currentHeadCount = JsonNullable.undefined();

    //모집 인원
    @Builder.Default
    private JsonNullable<Integer> maxHeadCount = JsonNullable.undefined();

    @AssertTrue(message = "참여 인원은 0명 이상이여야 합니다")
    public boolean isCurrentHeadCountNonNegative() {
        //값이 존재하지 않거나(json 필드에 명시하지 않음 = patch에 반영하지 않음)
        //값이 존재한다면 null이거나 0명 이상이여야 함
        if(!currentHeadCount.isPresent()) {
            return true;
        }
        return currentHeadCount.get() == null || currentHeadCount.get() >= 0;
    }

    @AssertTrue(message = "모집 인원은 1명 이상이여야 합니다")
    private boolean isMaxHeadCountPositive() {
        //값이 존재하지 않거나(json 필드에 명시하지 않음 = patch에 반영하지 않음)
        //값이 존재한다면 null이거나 1명 이상이여야 함
        if(!maxHeadCount.isPresent()) {
            return true;
        }
        return maxHeadCount.get() == null || maxHeadCount.get() >= 1;
    }
}
