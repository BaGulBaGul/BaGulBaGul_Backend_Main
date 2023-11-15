package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
public class EventConditionalRequest {
    private EventType type;
    private String title;
    private List<String> tags;
    private List<String> categories;
    private String username;
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    public PostConditionalRequest toPostConditionalRequest() {
        return PostConditionalRequest.builder()
                .title(title)
                .username(username)
                .tags(tags)
                .build();
    }
}
