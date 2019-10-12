package com.hglee.batch.service;

import com.hglee.batch.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

//    private final RestTemplateBuilder restTemplate;


    @Async
    public CompletableFuture findUser(String user,Long runTime) throws InterruptedException {
//        String url = String.format("https://api.github.com/users/%s", user);
        User results = new User();
        results.setName(user);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(20000);
        logger.info("Looking up user {} runTime {}",user,runTime);
        return CompletableFuture.completedFuture(results);
    }
}
