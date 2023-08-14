package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentCommentChildService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "모집글의 대댓글")
@RequiredArgsConstructor
@RestController
@RequestMapping("/recruitment_comment_child")
public class RecruitmentCommentChildController {

    private final RecruitmentCommentChildService recruitmentCommentChildService;

    @GetMapping("")
    @Operation(summary = "특정 뎃글에 관한 대댓글 페이징 조회",
            description = "댓글의 id와 offset을 받아 대댓글을 조회함")
    public ApiResponse<List<RecruitmentResponseDto.RCommentChildWithPaging>> getChildComments(@RequestParam Long id,
                                                                                             @RequestParam Integer offset){
        return recruitmentCommentChildService.getRecruitmentCommentChild(id, offset);
    }
}
