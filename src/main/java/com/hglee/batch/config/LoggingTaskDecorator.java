package com.hglee.batch.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class LoggingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable task) {

        Map<String, String> callerThreadContext = MDC.getCopyOfContextMap();
        return () -> {

            if (callerThreadContext == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(callerThreadContext);
            }
            task.run();
        };
    }
}
