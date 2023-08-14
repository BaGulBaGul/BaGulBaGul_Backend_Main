package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import com.BaGulBaGul.BaGulBaGul.domain.post.constant.PostType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
public class PostConditionalRequest {
    private PostType type;
    private String title;
    private List<String> tags;
    private List<String> categories;
    private String username;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime minimumStartDate;
}
