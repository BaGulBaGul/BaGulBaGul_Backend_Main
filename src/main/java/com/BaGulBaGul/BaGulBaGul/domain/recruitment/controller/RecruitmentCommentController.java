package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentCommentService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
//import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Api(tags = "모집글의 댓글")
@RequiredArgsConstructor
@RestController
@RequestMapping("/recruitment_comment")
public class RecruitmentCommentController {

    private final RecruitmentCommentService recruitmentCommentService;

    @GetMapping("")
    public ApiResponse<List<RecruitmentResponseDto.RCommentWithPaging>> gerComments(@RequestParam Long id,
                                                                                    @RequestParam Integer offset){
        return recruitmentCommentService.getRecruitmentComment(id, offset);
    }


}
