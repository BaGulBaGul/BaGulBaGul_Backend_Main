package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentRegisterRequest {

    @Valid
    private PeriodRegisterRequest periodRegisterRequest;

    @Valid
    private ParticipantStatusRegisterRequest participantStatusRegisterRequest;

    @Valid
    private PostRegisterRequest postRegisterRequest;

}
