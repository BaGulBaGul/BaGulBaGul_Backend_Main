package com.BaGulBaGul.BaGulBaGul.domain.upload.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private Long resourceId;
}
