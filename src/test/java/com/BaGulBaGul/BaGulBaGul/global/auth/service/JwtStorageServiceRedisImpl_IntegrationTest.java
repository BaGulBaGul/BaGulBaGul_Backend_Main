package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtStorageServiceRedisImpl_IntegrationTest {

    @Autowired
    JwtStorageServiceRedisImpl jwtStorageService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @AfterEach
    void tearDown() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

    @Test
    @DisplayName("저장 테스트")
    @Transactional
    void testSave() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        Long userId = user.getId();
        AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId);
        RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);

        //when
        jwtStorageService.save(atInfo, rtInfo);

        //then
        String result = redisTemplate.opsForValue().get(jwtStorageService.getKey(atInfo));
        assertThat(result).isEqualTo(jwtStorageService.getValue(rtInfo));
    }

    @Test
    @DisplayName("조회 후 삭제 테스트(값이 있을 때)")
    @Transactional
    void testGetAndDelete_whenKeyExists() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        Long userId = user.getId();
        AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId);
        RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);
        jwtStorageService.save(atInfo, rtInfo);

        //when
        boolean isPresent = jwtStorageService.checkPresentAndDelete(atInfo, rtInfo);

        //then
        String result = redisTemplate.opsForValue().get(jwtStorageService.getKey(atInfo));
        assertThat(result).isNull();
        assertThat(isPresent).isTrue();
    }

    @Test
    @DisplayName("조회 후 삭제 테스트(값이 없을 때)")
    @Transactional
    void testGetAndDelete_whenKeyNotExists() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        Long userId = user.getId();
        AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId);
        RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);

        //when
        boolean isPresent = jwtStorageService.checkPresentAndDelete(atInfo, rtInfo);

        //then
        assertThat(isPresent).isFalse();
    }

    @Test
    @DisplayName("삭제 테스트")
    @Transactional
    void testDelete() {
        //given
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        Long userId = user.getId();
        AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId);
        RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);
        jwtStorageService.save(atInfo, rtInfo);

        //when
        jwtStorageService.delete(atInfo);

        //then
        String result = redisTemplate.opsForValue().get(jwtStorageService.getKey(atInfo));
        assertThat(result).isNull();
    }
}