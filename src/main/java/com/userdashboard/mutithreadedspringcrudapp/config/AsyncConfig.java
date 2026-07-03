package com.userdashboard.mutithreadedspringcrudapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "dashboardExecutor")
    public Executor dashboardExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);       // Minimum number of threads to keep alive
        executor.setMaxPoolSize(10);      // Maximum number of threads allowed if queue fills up
        executor.setQueueCapacity(100);   // Number of tasks that can wait in line
        executor.setThreadNamePrefix("DashThread-"); // Makes debugging easy in logs
        executor.initialize();

        return executor;
    }
}
