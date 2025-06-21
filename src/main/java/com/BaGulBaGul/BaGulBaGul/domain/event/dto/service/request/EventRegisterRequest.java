package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class EventRegisterRequest {

    //이벤트 타입
    @NotNull(message = "이벤트 종류는 null일 수 없습니다.")
    private EventType type;

    //주최자 유저 id
    private Long eventHostUserId;

    //연령 제한 여부
    @NotNull(message = "연령 제한 여부는 true 이거나 false 여야 합니다.")
    private Boolean ageLimit;

    //카테고리들
    @Size(max = 2, message = "카테고리 개수는 {max}개 이하여야 합니다.")
    private List<String> categories;

    //위치 정보 등록 정보
    @Valid
    private LocationRegisterRequest locationRegisterRequest;

    //기간 정보 등록 정보
    @Valid
    private PeriodRegisterRequest periodRegisterRequest;

    //참여자 모집 정보
    @Valid
    private ParticipantStatusRegisterRequest participantStatusRegisterRequest;

    //게시글 등록 정보
    @Valid
    private PostRegisterRequest postRegisterRequest;

}
