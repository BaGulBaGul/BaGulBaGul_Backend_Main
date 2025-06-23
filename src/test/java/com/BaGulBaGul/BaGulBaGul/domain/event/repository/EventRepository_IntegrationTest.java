package com.BaGulBaGul.BaGulBaGul.domain.event.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl.FindEventByCondition.EventIdsWithTotalCountOfPageResult;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class EventRepository_IntegrationTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private EventService eventService;

    @Autowired
    private UserJoinService userJoinService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @BeforeEach
    void init() {
        //쓰기 권한 확인 무효화
        doNothing().when(eventService).checkWritePermission(any(), any());
    }

    @Nested
    @DisplayName("이벤트 조회 테스트")
    class ReadTest {
        @Test
        @DisplayName("정상 페이지 조회")
        @Transactional
        void normalPageReadTest() {
            //when
            int eventCount = 10;
            User eventHostUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            for(int i = 0; i< eventCount; i++) {
                EventRegisterRequest eventRegisterRequest = EventSample
                        .getNormalRegisterRequest(eventHostUser.getId());
                eventService.registerEvent(
                        eventHostUser.getId(),
                        eventRegisterRequest
                );
            }

            //when
            EventIdsWithTotalCountOfPageResult pageResult = eventRepository
                    .getEventIdsByConditionAndPageable(
                            EventConditionalRequest.builder().build(),
                            Pageable.ofSize(eventCount)
                    );

            //then
            assertThat(pageResult.getTotalCount()).isEqualTo(eventCount);
        }

        @Test
        @DisplayName("삭제된 유저가 작성한 이벤트 페이지 조회")
        @Transactional
        void pageReadWhenDeleteUserTest() {
            //when
            int eventCount = 10;
            User eventHostUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            for(int i = 0; i< eventCount; i++) {
                EventRegisterRequest eventRegisterRequest = EventSample
                        .getNormalRegisterRequest(eventHostUser.getId());
                eventService.registerEvent(
                        eventHostUser.getId(),
                        eventRegisterRequest
                );
            }

            //when
            userJoinService.deleteUser(eventHostUser.getId());
            EventIdsWithTotalCountOfPageResult pageResult = eventRepository
                    .getEventIdsByConditionAndPageable(
                            EventConditionalRequest.builder().build(),
                            Pageable.ofSize(eventCount)
                    );

            //then
            assertThat(pageResult.getTotalCount()).isEqualTo(eventCount);
        }

        @Test
        @DisplayName("삭제된 이벤트 페이지 조회")
        @Transactional
        void pageReadDeletedEventTest() {
            //when
            List<Long> eventIds = new ArrayList<>();
            int eventCount = 10;
            User eventHostUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            for(int i = 0; i< eventCount; i++) {
                EventRegisterRequest eventRegisterRequest = EventSample
                        .getNormalRegisterRequest(eventHostUser.getId());
                Long eventId = eventService.registerEvent(
                        eventHostUser.getId(),
                        eventRegisterRequest
                );
                eventIds.add(eventId);
            }

            //when
            for(int i=0; i<eventCount; i++) {
                eventService.deleteEvent(eventIds.get(i), eventHostUser.getId());
            }
            EventIdsWithTotalCountOfPageResult pageResult = eventRepository
                    .getEventIdsByConditionAndPageable(
                            EventConditionalRequest.builder().build(),
                            Pageable.ofSize(eventCount)
                    );

            //then
            assertThat(pageResult.getTotalCount()).isEqualTo(0);
        }
    }
}