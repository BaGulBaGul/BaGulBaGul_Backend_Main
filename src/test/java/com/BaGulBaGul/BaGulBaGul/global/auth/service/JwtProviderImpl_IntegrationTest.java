package com.BaGulBaGul.BaGulBaGul.global.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtProviderImpl_IntegrationTest {

    @Autowired
    JwtProviderImpl jwtProvider;

    @Autowired
    UserJoinService userJoinService;

    @Test
    @Transactional
    void createAndParseTest() {
        //given
        UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
        User user = userJoinService.registerUser(userRegisterRequest);
        Long userId = user.getId();
        List<String> roles = userRegisterRequest.getRoles();

        //when
        AccessTokenInfo accessTokenInfo = jwtProvider.createAccessToken(userId);
        AccessTokenInfo parsedAccessTokenInfo = jwtProvider.parseAccessToken(accessTokenInfo.getJwt());

        //then
        assertThat(accessTokenInfo.getJti()).isEqualTo(parsedAccessTokenInfo.getJti());
        assertThat(accessTokenInfo.getJwt()).isEqualTo(parsedAccessTokenInfo.getJwt());
        assertThat(accessTokenInfo.getSubject()).isEqualTo(parsedAccessTokenInfo.getSubject());
        assertThat(accessTokenInfo.getUserId()).isEqualTo(parsedAccessTokenInfo.getUserId());
        assertThat(accessTokenInfo.getRoles()).containsExactlyInAnyOrderElementsOf(
                parsedAccessTokenInfo.getRoles());
        assertThat(accessTokenInfo.getRoles()).containsExactlyInAnyOrderElementsOf(
                roles);
    }
}