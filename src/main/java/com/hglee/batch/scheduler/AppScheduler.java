package com.hglee.batch.scheduler;

import com.hglee.batch.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AppScheduler {


    private static final Logger log = LoggerFactory.getLogger(AppScheduler.class);

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private AsyncService asyncService;

    @Scheduled(cron = "0/10 * * * * *")
    public void work() throws Exception {


        Long runTime = System.currentTimeMillis();

        log.info("Job Started at : {}", runTime);

        JobParameters param =
                new JobParametersBuilder()
                        .addLong("runTime", runTime).toJobParameters();

        JobExecution execution = null;

        try {

            execution = jobLauncher.run(job, param);

        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }

        log.info("Job finished with status : {}", execution.getStatus());
        asyncService.findUser("coderhglee",runTime);
    }
}