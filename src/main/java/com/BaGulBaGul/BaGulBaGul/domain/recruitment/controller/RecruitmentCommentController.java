package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentCommentService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "모집글의 댓글")
@RequiredArgsConstructor
@RestController
@RequestMapping("/recruitment_comment")
public class RecruitmentCommentController {

    private final RecruitmentCommentService recruitmentCommentService;

    @GetMapping("")
    @Operation(summary = "특정 모집글의 댓글 페이징 조회",
            description = "모집글 id와 offset을 받아 댓글 조회함")
    public ApiResponse<List<RecruitmentResponseDto.RCommentWithPaging>> gerComments(@RequestParam Long id,
                                                                                    @RequestParam Integer offset){
        return recruitmentCommentService.getRecruitmentComment(id, offset);
    }


}
