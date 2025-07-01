package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.AdminManageEventHostUserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.AdminManageEventHostUserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AdminManageEventHostUserModifyRequest_UnitTest {
    @Nested
    @DisplayName("검증 테스트")
    class Validation {
        @Test
        @DisplayName("정상 동작")
        void shouldOk() {
            AdminManageEventHostUserModifyRequest eventHostUserModifyRequest = AdminManageEventHostUserSample
                    .getNormalAdminManageEventHostUserModifyRequest();
            assertDoesNotThrow(() -> ValidationUtil.validate(eventHostUserModifyRequest));
        }

        @Test
        @DisplayName("내부 dto를 검증하는지 테스트")
        void shouldThrowConstraintViolationException_whenInnerDtoIsInvalid() {
            AdminManageEventHostUserModifyRequest eventHostUserModifyRequest = AdminManageEventHostUserSample
                    .getNormalAdminManageEventHostUserModifyRequest();
            eventHostUserModifyRequest.setUserModifyRequest(UserSample.getUnNormalUserModifyRequest());
            assertThatThrownBy(() -> ValidationUtil.validate(eventHostUserModifyRequest))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }
}
