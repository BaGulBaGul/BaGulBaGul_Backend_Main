package com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl.FindUserByCondition.UserIdsWithTotalCount;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        assertThat(result.getEventIds()).hasSize(3);
        assertThat(result.getEventIds()).contains(user1.getId(), user2.getId(), user3.getId());
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
        assertThat(result.getEventIds()).hasSize(1);
        assertThat(result.getEventIds()).contains(user2.getId());
    }

    @Test
    @DisplayName("가입 시작일로 검색")
    void getUserIdsByCondition_byJoinDateStart() {
        //given
        User user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        User user3 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
        LocalDateTime searchStartDate = LocalDateTime.now().minusHours(1);
        em.createNativeQuery("UPDATE user SET created_at = :createdAt WHERE user_id = :userId")
                .setParameter("createdAt", searchStartDate.minusDays(1))
                .setParameter("userId", user2.getId())
                .executeUpdate();

        UserSearchRequest request = UserSearchRequest.builder().joinDateSearchStart(searchStartDate).build();
        Pageable pageable = PageRequest.of(0, 10);


        //when
        UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

        //then
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getEventIds()).hasSize(2);
        assertThat(result.getEventIds()).contains(user1.getId(), user3.getId());
    }

    @Test
    @DisplayName("가입 종료일로 검색")
    void getUserIdsByCondition_byJoinDateEnd() {
        //given
        User user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        User user3 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
        LocalDateTime searchEndDate = LocalDateTime.now().plusHours(1);
        em.createNativeQuery("UPDATE user SET created_at = :createdAt WHERE user_id = :userId")
                .setParameter("createdAt", searchEndDate.plusDays(1))
                .setParameter("userId", user2.getId())
                .executeUpdate();

        UserSearchRequest request = UserSearchRequest.builder().joinDateSearchEnd(searchEndDate).build();
        Pageable pageable = PageRequest.of(0, 10);


        //when
        UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

        //then
        assertThat(result.getTotalCount()).isEqualTo(2);
        assertThat(result.getEventIds()).hasSize(2);
        assertThat(result.getEventIds()).contains(user1.getId(), user3.getId());
    }

    @Test
    @DisplayName("가입 시작일, 종료일로 검색")
    void getUserIdsByCondition_byJoinDateStartEnd() {
        //given
        User user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        User user3 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
        LocalDateTime searchStartDate = LocalDateTime.now().minusHours(3);
        LocalDateTime searchEndDate = searchStartDate.plusHours(6);
        em.createNativeQuery("UPDATE user SET created_at = :createdAt WHERE user_id = :userId")
                .setParameter("createdAt", searchStartDate.minusDays(1))
                .setParameter("userId", user1.getId())
                .executeUpdate();
        em.createNativeQuery("UPDATE user SET created_at = :createdAt WHERE user_id = :userId")
                .setParameter("createdAt", searchEndDate.plusDays(1))
                .setParameter("userId", user3.getId())
                .executeUpdate();

        UserSearchRequest request = UserSearchRequest.builder()
                .joinDateSearchStart(searchStartDate)
                .joinDateSearchEnd(searchEndDate)
                .build();
        Pageable pageable = PageRequest.of(0, 10);


        //when
        UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

        //then
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getEventIds()).hasSize(1);
        assertThat(result.getEventIds()).contains(user2.getId());
    }

    @Test
    @DisplayName("페이지네이션 테스트")
    void getUserIdsByCondition_pagination() {
        //given
        User user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        User user3 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
        UserSearchRequest request = UserSearchRequest.builder().build();
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Order.asc("id")));
        Pageable pageable2 = PageRequest.of(1, 2, Sort.by(Sort.Order.asc("id")));
        //when
        UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);
        UserIdsWithTotalCount result2 = findUserByCondition.getUserIdsByCondition(request, pageable2);

        //then
        assertThat(result.getTotalCount()).isEqualTo(3);
        assertThat(result.getEventIds()).hasSize(2);
        assertThat(result.getEventIds().get(0)).isEqualTo(user1.getId());
        assertThat(result.getEventIds().get(1)).isEqualTo(user2.getId());
        assertThat(result2.getTotalCount()).isEqualTo(3);
        assertThat(result2.getEventIds()).hasSize(1);
        assertThat(result2.getEventIds().get(0)).isEqualTo(user3.getId());
    }

    @Test
    @DisplayName("ID 내림차순 정렬 테스트")
    void getUserIdsByCondition_sortByIdDesc() {
        //given
        User user1 = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        User user2 = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
        User user3 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
        UserSearchRequest request = UserSearchRequest.builder().build();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("id")));

        //when
        UserIdsWithTotalCount result = findUserByCondition.getUserIdsByCondition(request, pageable);

        //then
        assertThat(result.getEventIds()).isEqualTo(Arrays.asList(user3.getId(), user2.getId(), user1.getId()));
    }
}
