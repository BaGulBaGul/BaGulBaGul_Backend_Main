package com.BaGulBaGul.BaGulBaGul.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckDuplicateUsernameResponse {
    boolean duplicate;
}
