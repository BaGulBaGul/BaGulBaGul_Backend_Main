package com.BaGulBaGul.BaGulBaGul.global.batch.resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClearTempResourceJobScheduler {
    private final JobLauncher jobLauncher;
    private final Job clearTempResourceJob;

    @Scheduled(cron = "${resource.temp.batch.scheduler.cron}")
    public void run() {
        Date startTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("startTime", startTime)
                .toJobParameters();
        log.info("작업");
        try {
            jobLauncher.run(clearTempResourceJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            log.info("이미 작업이 실행 중");
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.info("이미 완료된 작업");
        } catch (JobParametersInvalidException e) {
            log.info("유효하지 않은 job parameter");
            log.info(e.getMessage());
        }
    }
}
