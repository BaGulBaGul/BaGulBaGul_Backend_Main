package com.BaGulBaGul.BaGulBaGul.global.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtVerifyResult {
    private boolean success;
    private String id;
}
