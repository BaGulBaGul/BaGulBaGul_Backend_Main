package com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.AdminManageEventHostUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl.FindUserByCondition.UserIdsWithTotalCount;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.AdminManageEventHostUserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.PasswordLoginUserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.PasswordLoginUserService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.SocialLoginUserService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.UserSubType;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant.OAuth2Provider;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FindUserByConditionImpl_IntegrationTest {

    @Autowired
    FindUserByCondition findUserByCondition;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    EntityManager em;

    @Autowired
    SocialLoginUserService socialLoginUserService;

    @Autowired
    PasswordLoginUserService passwordLoginUserService;

    @Autowired
    AdminManageEventHostUserRepository adminManageEventHostUserRepository;

    @Test
    @DisplayName("조건 없이 검색")
    void getUserIdsByCondition_noCondition() {
        //given
        User user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        User user3 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
        UserSearchRequest request = UserSearchRequest.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        //when
        UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

        //then

        assertThat(result.getTotalCount()).isEqualTo(3);
        assertThat(result.getUserIds()).hasSize(3);
        assertThat(result.getUserIds()).contains(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    @DisplayName("유저 이름으로 검색")
    void getUserIdsByCondition_byUserName() {
        //given
        User user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        User user3 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
        UserSearchRequest request = UserSearchRequest.builder().userName(user2.getNickname()).build();
        Pageable pageable = PageRequest.of(0, 10);

        //when
        UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

        //then
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getUserIds()).hasSize(1);
        assertThat(result.getUserIds()).contains(user2.getId());
    }

    @Nested
    @DisplayName("가입 일시로 검색")
    class JoinDateSearchTest {
        User user1;
        User user2;
        User user3; 
        LocalDateTime searchStartDate;
        LocalDateTime searchEndDate;

        @BeforeEach
        void init() {
            user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            user3 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
            searchStartDate = LocalDateTime.now().minusHours(3);
            searchEndDate = searchStartDate.plusHours(6);
            em.createNativeQuery("UPDATE user SET created_at = :createdAt WHERE user_id = :userId")
                    .setParameter("createdAt", searchStartDate.minusDays(1))
                    .setParameter("userId", user1.getId())
                    .executeUpdate();
            em.createNativeQuery("UPDATE user SET created_at = :createdAt WHERE user_id = :userId")
                    .setParameter("createdAt", searchEndDate.plusDays(1))
                    .setParameter("userId", user3.getId())
                    .executeUpdate();
            em.flush();
            em.clear();
        }

        @Test
        @DisplayName("가입 시작일로 검색")
        void getUserIdsByCondition_byJoinDateStart() {
            //given
            //when
            UserSearchRequest request = UserSearchRequest.builder().joinDateSearchStart(searchStartDate).build();
            Pageable pageable = PageRequest.of(0, 10);
            UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

            //then
            assertThat(result.getTotalCount()).isEqualTo(2);
            assertThat(result.getUserIds()).hasSize(2);
            assertThat(result.getUserIds()).contains(user2.getId(), user3.getId());
        }

        @Test
        @DisplayName("가입 종료일로 검색")
        void getUserIdsByCondition_byJoinDateEnd() {
            //given
            //when
            UserSearchRequest request = UserSearchRequest.builder().joinDateSearchEnd(searchEndDate).build();
            Pageable pageable = PageRequest.of(0, 10);
            UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

            //then
            assertThat(result.getTotalCount()).isEqualTo(2);
            assertThat(result.getUserIds()).hasSize(2);
            assertThat(result.getUserIds()).contains(user1.getId(), user2.getId());
        }

        @Test
        @DisplayName("가입 시작일, 종료일로 검색")
        void getUserIdsByCondition_byJoinDateStartEnd() {
            //given
            UserSearchRequest request = UserSearchRequest.builder()
                    .joinDateSearchStart(searchStartDate)
                    .joinDateSearchEnd(searchEndDate)
                    .build();
            Pageable pageable = PageRequest.of(0, 10);

            //when
            UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

            //then
            assertThat(result.getTotalCount()).isEqualTo(1);
            assertThat(result.getUserIds()).hasSize(1);
            assertThat(result.getUserIds()).contains(user2.getId());
        }

    }

    @Nested
    @DisplayName("페이지네이션, 정렬 테스트")
    class PaginationAndSortTest {
        User user1;
        User user2;
        User user3;

        @BeforeEach
        void init() {
            user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            user3 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
        }

        @Test
        @DisplayName("페이지네이션 테스트")
        void getUserIdsByCondition_pagination() {
            //given
            //when
            UserSearchRequest request = UserSearchRequest.builder().build();
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Order.asc("id")));
            Pageable pageable2 = PageRequest.of(1, 2, Sort.by(Sort.Order.asc("id")));
            UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);
            UserIdsWithTotalCount result2 = findUserByCondition.getUserIdsByCondition(request, pageable2);

            //then
            assertThat(result.getTotalCount()).isEqualTo(3);
            assertThat(result.getUserIds()).hasSize(2);
            assertThat(result.getUserIds().get(0)).isEqualTo(user1.getId());
            assertThat(result.getUserIds().get(1)).isEqualTo(user2.getId());
            assertThat(result2.getTotalCount()).isEqualTo(3);
            assertThat(result2.getUserIds()).hasSize(1);
            assertThat(result2.getUserIds().get(0)).isEqualTo(user3.getId());
        }

        @Test
        @DisplayName("ID 내림차순 정렬 테스트")
        void getUserIdsByCondition_sortByIdDesc() {
            //given
            //when
            UserSearchRequest request = UserSearchRequest.builder().build();
            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("id")));
            UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

            //then
            assertThat(result.getUserIds()).isEqualTo(Arrays.asList(user3.getId(), user2.getId(), user1.getId()));
        }
    }

    @Nested
    @DisplayName("서브 타입으로 검색")
    class ConditionBySubTypeTest{

        User passwordUser;
        User socialUser;
        User adminHostUser;
        User socialAndPasswordUser;

        @BeforeEach
        void init() {
            passwordUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            passwordLoginUserService.registerPasswordLoginUser(
                    PasswordLoginUserSample.getNormalPasswordLoginUserRegisterRequest(), passwordUser);

            socialUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            socialLoginUserService.registerSocialLoginUser(socialUser, OAuth2Provider.kakao, "social_id");

            adminHostUser = userJoinService.joinAdminManageEventHostUser(
                    AdminManageEventHostUserSample.getNormalAdminManageEventHostUserRegisterRequest()
            ).getUser();

            socialAndPasswordUser = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
            socialLoginUserService.registerSocialLoginUser(socialAndPasswordUser, OAuth2Provider.kakao, "multi_type_id");
            passwordLoginUserService.registerPasswordLoginUser(
                    PasswordLoginUserSample.getNormal2PasswordLoginUserRegisterRequest(), socialAndPasswordUser
            );

            em.flush();
            em.clear();
        }

        @Test
        @DisplayName("유저 서브 타입으로 검색 - SOCIAL")
        void getUserIdsByCondition_bySubTypeSocial() {
            //given
            //when
            UserSearchRequest request = UserSearchRequest.builder()
                    .subTypes(Collections.singletonList(UserSubType.SOCIAL_LOGIN_USER))
                    .build();
            Pageable pageable = PageRequest.of(0, 10);
            UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

            //then
            assertThat(result.getTotalCount()).isEqualTo(2);
            assertThat(result.getUserIds()).hasSize(2);
            assertThat(result.getUserIds()).contains(socialUser.getId());
            assertThat(result.getUserIds()).contains(socialAndPasswordUser.getId());
        }

        @Test
        @DisplayName("유저 서브 타입으로 검색 - PASSWORD")
        void getUserIdsByCondition_bySubTypePassword() {
            //given
            //when
            UserSearchRequest request = UserSearchRequest.builder()
                    .subTypes(Collections.singletonList(UserSubType.PASSWORD_LOGIN_USER))
                    .build();
            Pageable pageable = PageRequest.of(0, 10);
            UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

            //then
            assertThat(result.getTotalCount()).isEqualTo(2);
            assertThat(result.getUserIds()).hasSize(2);
            assertThat(result.getUserIds()).contains(passwordUser.getId());
            assertThat(result.getUserIds()).contains(socialAndPasswordUser.getId());
        }

        @Test
        @DisplayName("유저 서브 타입으로 검색 - ADMIN_MANAGE_EVENT_HOST")
        void getUserIdsByCondition_bySubTypeAdminHost() {
            //given
            //when
            UserSearchRequest request = UserSearchRequest.builder()
                    .subTypes(Collections.singletonList(UserSubType.ADMIN_MANAGE_EVENT_HOST_USER))
                    .build();
            Pageable pageable = PageRequest.of(0, 10);
            UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

            //then
            assertThat(result.getTotalCount()).isEqualTo(1);
            assertThat(result.getUserIds()).hasSize(1);
            assertThat(result.getUserIds()).contains(adminHostUser.getId());
        }

        @Test
        @DisplayName("유저 서브 타입으로 검색 - 다중 선택(AND 조건)")
        void getUserIdsByCondition_bySubTypeMultiple_AND() {
            //given
            //when
            UserSearchRequest request = UserSearchRequest.builder()
                    .subTypes(Arrays.asList(UserSubType.SOCIAL_LOGIN_USER, UserSubType.PASSWORD_LOGIN_USER))
                    .build();
            Pageable pageable = PageRequest.of(0, 10);
            UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

            //then
            assertThat(result.getTotalCount()).isEqualTo(1);
            assertThat(result.getUserIds()).hasSize(1);
            assertThat(result.getUserIds()).containsExactly(socialAndPasswordUser.getId());
        }
    }
}
