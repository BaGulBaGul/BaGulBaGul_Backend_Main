package com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface FindEventByCondition {

    @Transactional
    Page<EventSimpleResponse> getEventSimpleResponsePageByCondition(
            EventConditionalRequest eventConditionalRequest,
            Pageable pageable
    );
}
