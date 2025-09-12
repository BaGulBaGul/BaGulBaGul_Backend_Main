package com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

public interface FindUserByCondition {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    class UserIdsWithTotalCount {
        private List<Long> eventIds;
        private Long TotalCount;
    }

    UserIdsWithTotalCount getUserIdsByCondition(UserSearchRequest userSearchRequest, Pageable pageable);
}
