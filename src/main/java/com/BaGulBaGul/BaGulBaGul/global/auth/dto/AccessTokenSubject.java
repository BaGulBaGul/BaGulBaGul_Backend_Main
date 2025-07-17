package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenSubject {
    private Long userId;
    private List<String> roles;
}
