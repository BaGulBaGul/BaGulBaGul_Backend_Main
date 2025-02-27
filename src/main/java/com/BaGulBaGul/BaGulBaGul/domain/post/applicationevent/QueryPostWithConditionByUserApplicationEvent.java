package com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostConditionalRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QueryPostWithConditionByUserApplicationEvent {
    private PostConditionalRequest postConditionalRequest;
}
