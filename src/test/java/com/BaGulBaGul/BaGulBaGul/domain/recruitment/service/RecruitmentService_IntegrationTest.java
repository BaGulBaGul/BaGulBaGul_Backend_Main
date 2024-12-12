package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.sampledata.RecruitmentSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test2")
class RecruitmentService_IntegrationTest {

    @Autowired
    RecruitmentService recruitmentService;

    @Autowired
    EventService eventService;

    @MockBean
    PostService postService;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Nested
    @DisplayName("모집글 등록 테스트")
    class RecruitmentRegisterTest {

        @BeforeEach
        void init() {
            when(postService.registerPost(any(), any())).thenReturn(null);
        }

        @Test
        @DisplayName("정상 등록")
        @Transactional
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            Long eventId = eventService.registerEvent(user.getId(), EventSample.getNormalRegisterRequest());

            RecruitmentRegisterRequest recruitmentRegisterRequest = RecruitmentSample.getNormalRegisterRequest();

            //when
            Long recruitmentId = recruitmentService.registerRecruitment(eventId, user.getId(), recruitmentRegisterRequest);
            Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);

            //then
            assertThat(recruitment.getState()).isEqualTo(RecruitmentState.PROCEEDING);
            assertThat(recruitment.getDeleted()).isEqualTo(false);
            assertThat(recruitment.getEvent().getId()).isEqualTo(eventId);
            //참가자
            assertThat(recruitment.getCurrentHeadCount()).isEqualTo(recruitmentRegisterRequest
                    .getParticipantStatusRegisterRequest().getCurrentHeadCount());
            assertThat(recruitment.getMaxHeadCount()).isEqualTo(recruitmentRegisterRequest
                    .getParticipantStatusRegisterRequest().getMaxHeadCount());
            //기간
            assertThat(recruitment.getStartDate()).isEqualTo(recruitmentRegisterRequest
                    .getPeriodRegisterRequest().getStartDate());
            assertThat(recruitment.getEndDate()).isEqualTo(recruitmentRegisterRequest
                    .getPeriodRegisterRequest().getEndDate());
            //게시글 등록 서비스를 호출했는지(이벤트, 모집글 두번)
            verify(postService, times(2)).registerPost(eq(user), any());
        }
    }

    @Nested
    @DisplayName("모집글 수정 테스트")
    class RecruitmentModifyTest {

        @BeforeEach
        void init() {
            doNothing().when(postService).modifyPost(any(), any());
        }

        @Test
        @DisplayName("전부 수정해야 함")
        @Transactional
        void shouldChangeAll() {
            //given
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            Long eventId = eventService.registerEvent(user.getId(), EventSample.getNormalRegisterRequest());

            RecruitmentRegisterRequest recruitmentRegisterRequest = RecruitmentSample.getNormalRegisterRequest();
            RecruitmentModifyRequest recruitmentModifyRequest = RecruitmentSample.getNormal2ModifyRequest();
            Long recruitmentId = recruitmentService.registerRecruitment(
                    eventId, user.getId(), recruitmentRegisterRequest);

            //when
            recruitmentService.modifyRecruitment(recruitmentId, user.getId(), recruitmentModifyRequest);
            entityManager.flush();
            entityManager.clear();

            Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);

            //then
            assertThat(recruitment.getState()).isEqualTo(RecruitmentState.PROCEEDING);
            assertThat(recruitment.getDeleted()).isEqualTo(false);
            assertThat(recruitment.getEvent().getId()).isEqualTo(eventId);
            //참가자
            assertThat(recruitment.getCurrentHeadCount()).isEqualTo(recruitmentModifyRequest
                    .getParticipantStatusModifyRequest().getCurrentHeadCount().get());
            assertThat(recruitment.getMaxHeadCount()).isEqualTo(recruitmentModifyRequest
                    .getParticipantStatusModifyRequest().getMaxHeadCount().get());
            //기간
            assertThat(recruitment.getStartDate()).isEqualTo(recruitmentModifyRequest
                    .getPeriodModifyRequest().getStartDate().get());
            assertThat(recruitment.getEndDate()).isEqualTo(recruitmentModifyRequest
                    .getPeriodModifyRequest().getEndDate().get());
            //게시글 서비스에 수정요청을 보냈는지
            verify(postService, times(1)).modifyPost(any(), any());
        }

        @Test
        @DisplayName("아무것도 수정하지 말아야 함")
        @Transactional
        void shouldChangeNothing() {
            //given
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            Long eventId = eventService.registerEvent(user.getId(), EventSample.getNormalRegisterRequest());

            RecruitmentRegisterRequest recruitmentRegisterRequest = RecruitmentSample.getNormalRegisterRequest();

            RecruitmentModifyRequest recruitmentModifyRequest = RecruitmentModifyRequest.builder()
                    .build();
            Long recruitmentId = recruitmentService.registerRecruitment(eventId,
                    user.getId(), recruitmentRegisterRequest);

            //when
            recruitmentService.modifyRecruitment(recruitmentId, user.getId(), recruitmentModifyRequest);
            entityManager.flush();
            entityManager.clear();

            Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElse(null);

            //then
            assertThat(recruitment.getState()).isEqualTo(RecruitmentSample.NORMAL_RECRUITMENT_STATE);
            assertThat(recruitment.getDeleted()).isEqualTo(false);
            assertThat(recruitment.getEvent().getId()).isEqualTo(eventId);
            //참가자
            assertThat(recruitment.getMaxHeadCount()).isEqualTo(recruitmentRegisterRequest
                    .getParticipantStatusRegisterRequest().getMaxHeadCount());
            //기간
            assertThat(recruitment.getStartDate()).isEqualTo(recruitmentRegisterRequest
                    .getPeriodRegisterRequest().getStartDate());
            assertThat(recruitment.getEndDate()).isEqualTo(recruitmentRegisterRequest
                    .getPeriodRegisterRequest().getEndDate());
            //게시글 서비스에 수정요청을 보냈는지
            verify(postService, times(1)).modifyPost(any(), any());
        }
    }
}