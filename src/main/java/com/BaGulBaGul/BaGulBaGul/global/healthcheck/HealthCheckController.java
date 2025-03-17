package com.BaGulBaGul.BaGulBaGul.global.healthcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/")
    public void healthCheck() {
    }
}
