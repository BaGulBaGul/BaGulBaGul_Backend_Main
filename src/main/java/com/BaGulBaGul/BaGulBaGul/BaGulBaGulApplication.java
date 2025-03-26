package com.BaGulBaGul.BaGulBaGul;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@EnableBatchProcessing
@EnableAsync
@SpringBootApplication
public class BaGulBaGulApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaGulBaGulApplication.class, args);
	}
}
