package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request;

import static org.junit.jupiter.api.Assertions.*;

import com.BaGulBaGul.BaGulBaGul.domain.post.sampledata.PostSample;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostModifyRequest_UnitTest {
    @Test
    @DisplayName("정상 통과")
    void shouldOK() {
        assertDoesNotThrow(() -> ValidationUtil.validate(PostSample.getNormalModifyRequest()));
    }

    @Test
    @DisplayName("제목 길이 초과")
    void shouldThrowConstraintViolationException_WhenTitleLengthOver() {
        //given
        String title = "a" .repeat(PostSample.TITLE_MAX_LENGTH + 1);
        PostModifyRequest postModifyRequest = PostSample.getNormalModifyRequest();
        postModifyRequest.setTitle(title);
        //when //then
        assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(postModifyRequest));
    }

    @Test
    @DisplayName("내용 길이 초과")
    void shouldThrowConstraintViolationException_WhenContentLengthOver() {
        //given
        String content = "a" .repeat(PostSample.CONTENT_MAX_LENGTH + 1);
        PostModifyRequest postModifyRequest = PostSample.getNormalModifyRequest();
        postModifyRequest.setContent(content);
        //when //then
        assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(postModifyRequest));
    }

    @Test
    @DisplayName("태그 최대 개수 초과")
    void shouldThrowConstraintViolationException_WhenTagCountOver() {
        //given
        List<String> tags = new ArrayList<>();
        for(int i=0;i<PostSample.TAG_MAX_LENGTH + 1;i++) {
            tags.add("a");
        }
        PostModifyRequest postModifyRequest = PostSample.getNormalModifyRequest();
        postModifyRequest.setTags(tags);
        //when //then
        assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(postModifyRequest));
    }

    @Test
    @DisplayName("이미지 최대 개수 초과")
    void shouldThrowConstraintViolationException_WhenImageCountOver() {
        //given
        List<Long> imageIds = new ArrayList<>();
        for(int i=0;i<PostSample.IMAGE_MAX_LENGTH + 1;i++) {
            imageIds.add(1L);
        }
        PostModifyRequest postModifyRequest = PostSample.getNormalModifyRequest();
        postModifyRequest.setImageIds(imageIds);
        //when //then
        assertThrows(ConstraintViolationException.class, () -> ValidationUtil.validate(postModifyRequest));
    }
}