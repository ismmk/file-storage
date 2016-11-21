package com.koval.storage.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by Volodymyr Kovalenko
 */
@Configuration
public class SchedulerTestConfig {
    public static volatile ScheduledExecutorService scheduledExecutorService = spy(new ScheduledThreadPoolExecutor(1));

    @Bean
    public ScheduledExecutorService taskExecutor() {
        return scheduledExecutorService;
    }
}
