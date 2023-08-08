package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;


import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
//import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Api(tags = "모집글")
@RequiredArgsConstructor
@RestController
@RequestMapping("/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping
    public ApiResponse<List<RecruitmentResponseDto.RInfoWithPaging>> getRecruitments(@RequestParam Long id,
                                                                                     @RequestParam Integer offset){
        return recruitmentService.getRecruitments(id, offset);
    }

    @GetMapping("/detail")
    public ApiResponse<RecruitmentResponseDto.RInfo> getRecruitment(@RequestParam Long id){
        return recruitmentService.getRecruitment(id);
    }

}
