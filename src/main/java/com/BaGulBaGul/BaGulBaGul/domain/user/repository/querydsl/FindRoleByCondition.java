package com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SearchRoleRequest;
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
