package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.GetLikeRecruitmentResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentService {

    RecruitmentSimpleInfo getRecruitmentSimpleInfoById(Long recruitmentId);
    RecruitmentDetailResponse getRecruitmentDetailById(Long recruitmentId);
    Page<RecruitmentSimpleResponse> getRecruitmentPageByCondition(RecruitmentConditionalRequest recruitmentConditionalRequest, Pageable pageable);
    Page<GetLikeRecruitmentResponse> getMyLikeRecruitment(Long userId, Pageable pageable);
    Long registerRecruitment(Long eventId, Long userId, RecruitmentRegisterRequest recruitmentRegisterRequest);
    void modifyRecruitment(Long recruitmentId, Long userId, RecruitmentModifyRequest recruitmentModifyRequest);
    void deleteRecruitment(Long recruitmentId, Long userId);

    int getLikeCount(Long recruitmentId);
    void addLike(Long recruitmentId, Long userId) throws DuplicateLikeException;
    void deleteLike(Long recruitmentId, Long userId) throws LikeNotExistException;
    boolean isMyLike(Long recruitmentId, Long userId);
}
