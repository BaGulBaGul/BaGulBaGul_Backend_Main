package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;


import  com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "모집글")
@RequiredArgsConstructor
@RestController
@RequestMapping("/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping
    @Operation(summary = "특정 게시글에 관한 전체 모집글 페이징 조회",
            description = "게시글의 id와 offset을 받아 모집글을 조회함")
    public ApiResponse<List<RecruitmentResponseDto.RInfoWithPaging>> getRecruitments(@RequestParam Long id,
                                                                                     @RequestParam Integer offset){

        return recruitmentService.getRecruitments(id, offset);
    }

    @GetMapping("/detail")
    @Operation(summary = "특정 모집글 조회",
            description = "모집글의 id를 받아 해당 모집글 조회")
    public ApiResponse<RecruitmentResponseDto.RInfo> getRecruitment(@RequestParam Long id){
        return recruitmentService.getRecruitment(id);
    }

}
