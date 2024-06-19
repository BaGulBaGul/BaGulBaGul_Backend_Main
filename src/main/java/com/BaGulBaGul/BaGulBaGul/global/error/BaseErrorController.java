package com.BaGulBaGul.BaGulBaGul.global.error;

import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode.CodeType;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class BaseErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<ApiResponse> error(HttpServletResponse response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        CodeType codeType = CodeType.valueOf(httpStatus);

        return ResponseEntity
                .status(httpStatus)
                .body(ApiResponse.of(null, codeType));
    }
}