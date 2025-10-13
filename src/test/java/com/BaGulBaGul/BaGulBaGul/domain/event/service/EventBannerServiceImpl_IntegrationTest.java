
package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.EventBanner;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventBannerModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventBannerRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ResourceService;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.TransactionResourceService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.amazonaws.services.s3.AmazonS3;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("이벤트 베너 서비스 테스트")
class EventBannerServiceImpl_IntegrationTest {

    @MockBean
    AmazonS3 amazonS3;

    @SpyBean
    TransactionResourceService transactionResourceService;

    @SpyBean
    ResourceService resourceService;

    @Autowired
    EventBannerService eventBannerService;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    EventService eventService;
    @Autowired
    EntityManager em;
    @Autowired
    EventBannerRepository eventBannerRepository;

    User user;
    AuthenticatedUserInfo authenticatedUserInfo;
    List<Long> eventIds = new ArrayList<>();
    List<Long> resourceIds = new ArrayList<>();

    @Nested
    @DisplayName("배너 설정 성공 테스트")
    class SetEventBannersSuccessTest {

        @BeforeEach
        void init() throws IOException {
            //유저 생성
            UserRegisterRequest userRegisterRequest = UserSample.getAdminUserRegisterRequest();
            user = userJoinService.registerUser(userRegisterRequest);
            authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.ADMIN.name(), GeneralRoleType.EVENT_HOST.name()))
                    .build();

            //이벤트 5개 생성
            for(int i=0;i<5;i++) {
                EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(user.getId());
                Long eventId = eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest);
                eventIds.add(eventId);
            }
            //임시 리소스 5개 생성
            for(int i=0;i<5;i++) {
                Long resourceId = resourceService.uploadResource("", new MockMultipartFile("test"+i, ("test"+i+".jpg"), "image/jpeg", "test".getBytes()));
                resourceIds.add(resourceId);
            }

            em.flush();
            em.clear();
            reset(amazonS3, transactionResourceService, resourceService);
        }

        @Test
        @DisplayName("대체(replacement) 테스트")
        void replacement_success() {
            // given
            Long targetEventBannerId = 1L;
            Long event1Id = eventIds.get(0);
            Long resource1Id = resourceIds.get(0);
            Long event2Id = eventIds.get(1);
            Long resource2Id = resourceIds.get(1);

            // when: 1차 호출 (x -> A)
            List<EventBannerModifyRequest> requests1 = Collections.singletonList(
                    new EventBannerModifyRequest(targetEventBannerId, event1Id, resource1Id)
            );
            eventBannerService.setEventBanners(requests1);
            em.flush();
            em.clear();

            // then: 1차 검증
            EventBanner banner1 = eventBannerRepository.findById(targetEventBannerId).get();
            assertThat(banner1.getEvent().getId()).isEqualTo(event1Id);
            assertThat(banner1.getBannerImageResource().getId()).isEqualTo(resource1Id);

            ArgumentCaptor<Collection<Long>> cancelCaptor1 = ArgumentCaptor.forClass(Collection.class);
            verify(resourceService).cancelTempResources(cancelCaptor1.capture());
            assertThat(cancelCaptor1.getValue()).containsExactlyInAnyOrder(resource1Id);

            ArgumentCaptor<Collection<Long>> deleteCaptor1 = ArgumentCaptor.forClass(Collection.class);
            verify(transactionResourceService, times(0)).deleteResourcesAsyncAfterCommit(any());
//            verify(transactionResourceService).deleteResourcesAsyncAfterCommit(deleteCaptor1.capture());
//            assertThat(deleteCaptor1.getValue()).isEmpty();

            em.flush();
            em.clear();
            reset(resourceService, transactionResourceService);

            // when: 2차 호출 (A -> B)
            List<EventBannerModifyRequest> requests2 = Collections.singletonList(
                    new EventBannerModifyRequest(targetEventBannerId, event2Id, resource2Id)
            );
            eventBannerService.setEventBanners(requests2);
            em.flush();
            em.clear();

            // then: 2차 검증
            banner1 = eventBannerRepository.findById(targetEventBannerId).get();
            assertThat(banner1.getEvent().getId()).isEqualTo(event2Id);
            assertThat(banner1.getBannerImageResource().getId()).isEqualTo(resource2Id);

            ArgumentCaptor<Collection<Long>> cancelCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(resourceService).cancelTempResources(cancelCaptor2.capture());
            assertThat(cancelCaptor2.getValue()).containsExactlyInAnyOrder(resource2Id);

            ArgumentCaptor<Collection<Long>> deleteCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(transactionResourceService).deleteResourcesAsyncAfterCommit(deleteCaptor2.capture());
            assertThat(deleteCaptor2.getValue()).containsExactlyInAnyOrder(resource1Id);
        }

        @Test
        @DisplayName("할당(assignment) 테스트")
        void assignment_success() {
            // given
            Long targetEventBannerId = 1l;
            Long event1Id = eventIds.get(0);
            Long resource1Id = resourceIds.get(0);

            // when: 1차 호출 (x -> A)
            List<EventBannerModifyRequest> requests2 = Collections.singletonList(
                    new EventBannerModifyRequest(targetEventBannerId, event1Id, resource1Id)
            );
            eventBannerService.setEventBanners(requests2);
            em.flush();
            em.clear();

            // then: 1차 검증
            EventBanner banner1 = eventBannerRepository.findById(targetEventBannerId).get();
            assertThat(banner1.getEvent().getId()).isEqualTo(event1Id);
            assertThat(banner1.getBannerImageResource().getId()).isEqualTo(resource1Id);

            ArgumentCaptor<Collection<Long>> cancelCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(resourceService).cancelTempResources(cancelCaptor2.capture());
            assertThat(cancelCaptor2.getValue()).containsExactlyInAnyOrder(resource1Id);

            ArgumentCaptor<Collection<Long>> deleteCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(transactionResourceService, times(0)).deleteResourcesAsyncAfterCommit(any());
//            verify(transactionResourceService).deleteResourcesAsyncAfterCommit(deleteCaptor2.capture());
//            assertThat(deleteCaptor2.getValue()).isEmpty();
        }

        @Test
        @DisplayName("제거(removal) 테스트")
        void removal_success() {
            // given
            long targetEventBannerId = 1L;
            Long event1Id = eventIds.get(0);
            Long resource1Id = resourceIds.get(0);

            // when: 1차 호출 (x -> A)
            List<EventBannerModifyRequest> requests1 = Collections.singletonList(
                    new EventBannerModifyRequest(targetEventBannerId, event1Id, resource1Id)
            );
            eventBannerService.setEventBanners(requests1);
            em.flush();
            em.clear();

            // then: 1차 검증
            EventBanner banner1 = eventBannerRepository.findById(targetEventBannerId).get();
            assertThat(banner1.getEvent().getId()).isEqualTo(event1Id);
            assertThat(banner1.getBannerImageResource().getId()).isEqualTo(resource1Id);

            em.flush();
            em.clear();
            reset(resourceService, transactionResourceService);

            // when: 2차 호출 (A -> x)
            List<EventBannerModifyRequest> requests2 = Collections.singletonList(
                    new EventBannerModifyRequest(targetEventBannerId, null, null)
            );
            eventBannerService.setEventBanners(requests2);
            em.flush();
            em.clear();

            // then: 2차 검증
            banner1 = eventBannerRepository.findById(targetEventBannerId).get();
            assertThat(banner1.getEvent()).isNull();
            assertThat(banner1.getBannerImageResource()).isNull();

            ArgumentCaptor<Collection<Long>> cancelCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(resourceService, times(0)).cancelTempResources(any());
//            verify(resourceService).cancelTempResources(cancelCaptor2.capture());
//            assertThat(cancelCaptor2.getValue()).isEmpty();

            ArgumentCaptor<Collection<Long>> deleteCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(transactionResourceService).deleteResourcesAsyncAfterCommit(deleteCaptor2.capture());
            assertThat(deleteCaptor2.getValue()).containsExactlyInAnyOrder(resource1Id);
        }

        @Test
        @DisplayName("유지(no change) 테스트")
        void noChange_success() {
            // given
            Long targetEventBannerId = 1L;
            Long event1Id = eventIds.get(0);
            Long resource1Id = resourceIds.get(0);

            // when: 1차 호출 (x -> A)
            List<EventBannerModifyRequest> requests1 = Collections.singletonList(
                    new EventBannerModifyRequest(targetEventBannerId, event1Id, resource1Id)
            );
            eventBannerService.setEventBanners(requests1);
            em.flush();
            em.clear();

            // then: 1차 검증
            EventBanner banner1 = eventBannerRepository.findById(targetEventBannerId).get();
            assertThat(banner1.getEvent().getId()).isEqualTo(event1Id);
            assertThat(banner1.getBannerImageResource()).isNotNull();
            assertThat(banner1.getBannerImageResource().getId()).isEqualTo(resource1Id);

            em.flush();
            em.clear();
            reset(resourceService, transactionResourceService);

            // when: 2차 호출 (A -> A)
            List<EventBannerModifyRequest> requests2 = Collections.singletonList(
                    new EventBannerModifyRequest(targetEventBannerId, event1Id, resource1Id)
            );
            eventBannerService.setEventBanners(requests2);
            em.flush();
            em.clear();

            // then: 2차 검증
            banner1 = eventBannerRepository.findById(targetEventBannerId).get();
            assertThat(banner1.getEvent().getId()).isEqualTo(event1Id);
            assertThat(banner1.getBannerImageResource()).isNotNull();
            assertThat(banner1.getBannerImageResource().getId()).isEqualTo(resource1Id);

            ArgumentCaptor<Collection<Long>> cancelCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(resourceService).cancelTempResources(cancelCaptor2.capture());
            assertThat(cancelCaptor2.getValue()).containsExactlyInAnyOrder(resource1Id);

            ArgumentCaptor<Collection<Long>> deleteCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(transactionResourceService, times(0)).deleteResourcesAsyncAfterCommit(any());
//            verify(transactionResourceService).deleteResourcesAsyncAfterCommit(deleteCaptor2.capture());
//            assertThat(deleteCaptor2.getValue()).isEmpty();
        }

        @Test
        @DisplayName("교환(swap) 테스트")
        void swap_success() {
            // given
            long targetEventBanner1Id = 1L;
            Long event1Id = eventIds.get(0);
            Long resource1Id = resourceIds.get(0);
            long targetEventBanner2Id = 2L;
            Long event2Id = eventIds.get(1);
            Long resource2Id = resourceIds.get(1);

            // when: 1차 호출 (x -> A, B)
            List<EventBannerModifyRequest> requests1 = Arrays.asList(
                    new EventBannerModifyRequest(targetEventBanner1Id, event1Id, resource1Id),
                    new EventBannerModifyRequest(targetEventBanner2Id, event2Id, resource2Id)
            );
            eventBannerService.setEventBanners(requests1);
            em.flush();
            em.clear();

            // then: 1차 검증
            EventBanner banner1 = eventBannerRepository.findById(targetEventBanner1Id).get();
            assertThat(banner1.getEvent().getId()).isEqualTo(event1Id);
            assertThat(banner1.getBannerImageResource().getId()).isEqualTo(resource1Id);
            EventBanner banner2 = eventBannerRepository.findById(targetEventBanner2Id).get();
            assertThat(banner2.getEvent().getId()).isEqualTo(event2Id);
            assertThat(banner2.getBannerImageResource().getId()).isEqualTo(resource2Id);

            em.flush();
            em.clear();
            reset(resourceService, transactionResourceService);

            // when: 2차 호출 (A,B -> B,A)
            List<EventBannerModifyRequest> requests2 = Arrays.asList(
                    new EventBannerModifyRequest(targetEventBanner1Id, event2Id, resource2Id),
                    new EventBannerModifyRequest(targetEventBanner2Id, event1Id, resource1Id)
            );
            eventBannerService.setEventBanners(requests2);
            em.flush();
            em.clear();

            // then: 2차 검증
            banner1 = eventBannerRepository.findById(targetEventBanner1Id).get();
            assertThat(banner1.getEvent().getId()).isEqualTo(event2Id);
            assertThat(banner1.getBannerImageResource().getId()).isEqualTo(resource2Id);
            banner2 = eventBannerRepository.findById(targetEventBanner2Id).get();
            assertThat(banner2.getEvent().getId()).isEqualTo(event1Id);
            assertThat(banner2.getBannerImageResource().getId()).isEqualTo(resource1Id);

            ArgumentCaptor<Collection<Long>> cancelCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(resourceService).cancelTempResources(cancelCaptor2.capture());
            assertThat(cancelCaptor2.getValue()).containsExactlyInAnyOrder(resource1Id, resource2Id);

            ArgumentCaptor<Collection<Long>> deleteCaptor2 = ArgumentCaptor.forClass(Collection.class);
            verify(transactionResourceService, times(0)).deleteResourcesAsyncAfterCommit(any());
//            verify(transactionResourceService).deleteResourcesAsyncAfterCommit(deleteCaptor2.capture());
//            assertThat(deleteCaptor2.getValue()).isEmpty();
        }
    }
}
