package com.BaGulBaGul.BaGulBaGul.global.batch.ranking;

import java.util.Calendar;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchKeywordRankingUpdateJobScheduler {
    private final JobLauncher jobLauncher;
    private final Job searchKeywordRankingUpdateJob;

    @Value("${ranking.search.aggregate-period}")
    private int AGGREGATE_PERIOD;

    @Scheduled(cron = "${ranking.search.batch.scheduler.cron}")
    public void run() {
        //현재 시간에서 AGGREGATE_PERIOD일 전 날짜를 job parameter로 사용
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.add(Calendar.DAY_OF_MONTH, -AGGREGATE_PERIOD);
        nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
        nowCalendar.set(Calendar.MINUTE, 0);
        nowCalendar.set(Calendar.SECOND, 0);
        nowCalendar.set(Calendar.MILLISECOND, 0);
        Date startTime = nowCalendar.getTime();

        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("targetDate", startTime)
                .toJobParameters();
        log.info("작업");
        try {
            jobLauncher.run(searchKeywordRankingUpdateJob, jobParameters);
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

