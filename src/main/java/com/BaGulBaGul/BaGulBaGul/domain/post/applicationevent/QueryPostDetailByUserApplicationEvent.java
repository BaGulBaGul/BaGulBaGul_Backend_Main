package com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.global.applicationevent.BasicTimeApplicationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
 * 유저가 특정 게시글을 상세조회 했을 때 발생하는 어플리케이션 이벤트
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class QueryPostDetailByUserApplicationEvent extends BasicTimeApplicationEvent {
    private PostDetailInfo postDetailInfo;
}
