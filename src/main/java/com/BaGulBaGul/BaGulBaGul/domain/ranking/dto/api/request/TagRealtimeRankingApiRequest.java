package com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagRealtimeRankingApiRequest {
    @ApiModelProperty(value = "1위부터 count개수만큼 조회 | 필수")
    @Max(value = 100, message = "실시간 랭킹은 최대 {value}위까지 조회 가능합니다")
    private Integer count;
}
