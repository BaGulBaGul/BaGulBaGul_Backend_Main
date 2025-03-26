package com.BaGulBaGul.BaGulBaGul.domain.common.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodModifyRequest {
    @Builder.Default
    private JsonNullable<LocalDateTime> startDate = JsonNullable.undefined();

    @Builder.Default
    private JsonNullable<LocalDateTime> endDate = JsonNullable.undefined();
}
