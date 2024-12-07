package com.BaGulBaGul.BaGulBaGul.domain.post.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import java.util.Collections;
import java.util.List;

public class PostSample {
    public static String NORMAL_TITLE = "테스트제목1";
    public static String NORMAL_CONTENT = "테스트 내용입니다.";
    public static List<String> NORMAL_TAGS = List.of("피아노", "춤", "와인", "클래식");
    public static String NORMAL_TAGS_STRING = String.join(" ", NORMAL_TAGS.toArray(new String[0]));

    public static final PostRegisterRequest NORMAL_REGISTER_REQUEST = PostRegisterRequest.builder()
            .title(PostSample.NORMAL_TITLE)
            .content(PostSample.NORMAL_CONTENT)
            .tags(PostSample.NORMAL_TAGS)
            .imageIds(Collections.emptyList())
            .build();

    public static String NORMAL2_TITLE = "테스트제목2";
    public static String NORMAL2_CONTENT = "테스트 내용입니다.222";
    public static List<String> NORMAL2_TAGS = List.of("피아노", "와인", "바이올린");
    public static String NORMAL2_TAGS_STRING = String.join(" ", NORMAL2_TAGS.toArray(new String[0]));
    public static final PostRegisterRequest NORMAL2_REGISTER_REQUEST = PostRegisterRequest.builder()
            .title(PostSample.NORMAL2_TITLE)
            .content(PostSample.NORMAL2_CONTENT)
            .tags(PostSample.NORMAL2_TAGS)
            .imageIds(Collections.emptyList())
            .build();
}
