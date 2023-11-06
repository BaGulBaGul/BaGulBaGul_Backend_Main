package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventRegisterRequest {
    @NotNull
    private EventType type;
    @NotBlank
    private String title;
    private int headCount;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    private List<String> tags;
    private List<String> categories;
    private String image_url;

    public PostRegisterRequest toPostRegisterRequest() {
        return PostRegisterRequest.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .image_url(image_url)
                .build();
    }
}
