package com.hglee.batch.job;


import com.hglee.batch.config.LoggingTaskDecorator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class SimpleJobConfiguration {
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    @Autowired
    private StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음A
    @Autowired
    private JobRepository jobRepository;

    public static final int CORE_TASK_POOL_SIZE = 3;
    public static final int MAX_TASK_POOL_SIZE = 30;
    public static final int QUEUE_CAPACITY_SIZE = 10;
    public static final int CHUNK_AND_PAGE_SIZE = 400;

    @Bean(name = "taskExecutor")
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_TASK_POOL_SIZE);
        executor.setMaxPoolSize(MAX_TASK_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY_SIZE);
        executor.setTaskDecorator(new LoggingTaskDecorator());
        executor.setThreadNamePrefix("task-pool-");
        executor.initialize();

        return executor;
    }

//    @Bean
//    public JobLauncher createJobLauncher() throws Exception {
//        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//        jobLauncher.setJobRepository(jobRepository);
//        jobLauncher.afterPropertiesSet();
//        jobLauncher.setTaskExecutor(executor());
//        return jobLauncher;
//    }

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .build();
    }

    @Bean
    public Step simpleStep1() {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    Thread.sleep(2000);
                    return RepeatStatus.FINISHED;
                })
//                .taskExecutor(executor())
                .build();
    }
}