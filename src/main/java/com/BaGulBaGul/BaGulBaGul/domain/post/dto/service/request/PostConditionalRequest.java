package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import java.time.LocalDateTime;
import java.util.List;
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
public class PostConditionalRequest {
    //요청 제목을 포함하는 이벤트 검색
    private String title;
    //요청 태그를 모두 가진 이벤트 검색
    private List<String> tags;
    //작성자 닉네임이 일치하는 이벤트 검색
    private String username;
}
