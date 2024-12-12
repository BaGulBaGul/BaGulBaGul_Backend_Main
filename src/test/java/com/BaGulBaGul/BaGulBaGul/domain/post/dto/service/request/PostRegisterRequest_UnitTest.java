package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.BaGulBaGul.BaGulBaGul.domain.post.sampledata.PostSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostRegisterRequest_UnitTest {

    @Test
    @DisplayName("정상 통과")
    void shouldOK() {
        assertDoesNotThrow(() -> ValidationUtil.validate(PostSample.getNormalRegisterRequest()));
    }

    @Test
    @DisplayName("제목 길이 초과")
    void shouldThrowConstraintViolationException_WhenTitleLengthOver() {
        //given
        String title = "a" .repeat(PostSample.TITLE_MAX_LENGTH + 1);
        PostRegisterRequest postRegisterRequest = PostSample.getNormalRegisterRequest();
        postRegisterRequest.setTitle(title);
        //when //then
        assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(postRegisterRequest));
    }

    @Test
    @DisplayName("내용 길이 초과")
    void shouldThrowConstraintViolationException_WhenContentLengthOver() {
        //given
        String content = "a" .repeat(PostSample.CONTENT_MAX_LENGTH + 1);
        PostRegisterRequest postRegisterRequest = PostSample.getNormalRegisterRequest();
        postRegisterRequest.setContent(content);
        //when //then
        assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(postRegisterRequest));
    }

    @Test
    @DisplayName("태그 최대 개수 초과")
    void shouldThrowConstraintViolationException_WhenTagCountOver() {
        //given
        PostRegisterRequest postRegisterRequest = PostSample.getNormalRegisterRequest();
        List<String> tags = new ArrayList<>(Collections.nCopies(PostSample.TAG_MAX_LENGTH + 1, "a"));
        postRegisterRequest.setTags(tags);
        //when //then
        assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(postRegisterRequest));
    }

    @Test
    @DisplayName("이미지 최대 개수 초과")
    void shouldThrowConstraintViolationException_WhenImageCountOver() {
        //given
        PostRegisterRequest postRegisterRequest = PostSample.getNormalRegisterRequest();
        List<Long> imageIds = new ArrayList<>(Collections.nCopies(PostSample.IMAGE_MAX_LENGTH + 1, 1L));
        postRegisterRequest.setImageIds(imageIds);
        //when //then
        assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(postRegisterRequest));
    }
}