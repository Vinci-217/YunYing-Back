package com.yunying.gh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        return new ThreadPoolTaskExecutor() {{
            setCorePoolSize(5);  // 设置核心线程数
            setMaxPoolSize(10);  // 设置最大线程数
            setQueueCapacity(500);  // 设置队列容量
            setThreadNamePrefix("async-thread-");
            initialize();
        }};
    }
}
