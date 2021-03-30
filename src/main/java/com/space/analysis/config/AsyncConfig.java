package com.space.analysis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * 配置线程池
 */

@Configuration
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor getAsyncThreadPoolExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        threadPoolExecutor.setCorePoolSize(5);
        //配置最大线程数
        threadPoolExecutor.setMaxPoolSize(10);
        //配置队列大小
        threadPoolExecutor.setQueueCapacity(400);
        //配置允许的空闲时间,当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        threadPoolExecutor.setKeepAliveSeconds(200);

        //配置线程池中的线程的名称前缀
        threadPoolExecutor.setThreadNamePrefix("tony-");

        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }
}
