package com.BaGulBaGul.BaGulBaGul.global.auth.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.SearchRoleRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

public interface FindRoleByCondition {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    class RoleNamesWithTotalCount {
        private List<String> roleNames;
        private Long totalCount;
    }

    RoleNamesWithTotalCount findRoleNamesByCondition(SearchRoleRequest request, Pageable pageable);
}
