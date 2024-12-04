package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostConditionalRequest {
    private String title;
    private List<String> tags;
    private String username;
}
