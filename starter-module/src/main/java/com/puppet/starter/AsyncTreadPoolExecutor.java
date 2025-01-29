package com.puppet.starter;

import com.puppet.starter.properties.AsyncTreadPoolExecutorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Пул потоков для @Async по умолчанию включается при подключении зависимости
 * Для выключения используйте свойство my-async.enabled=false
 * по умолчанию свойства:
 * core-pool-size: 3
 * max-pool-size: 10
 * queue-capacity: 50
 * thread-name-prefix: "CustomAsync-"
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties(AsyncTreadPoolExecutorProperties.class)
@ConditionalOnProperty(prefix = "my-setting-kafka.my-async", value = "enabled", havingValue = "true", matchIfMissing = true)
public class AsyncTreadPoolExecutor {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTreadPoolExecutor.class);

    private final AsyncTreadPoolExecutorProperties properties;

    public AsyncTreadPoolExecutor(AsyncTreadPoolExecutorProperties properties) {
        this.properties = properties;
        logger.info("Core Pool Size: {}", properties.getCorePoolSize());
        logger.info("Max Pool Size: {}", properties.getMaxPoolSize());
        logger.info("Queue Capacity: {}", properties.getQueueCapacity());
        logger.info("Thread Name Prefix: {}", properties.getThreadNamePrefix());
    }

    /**
     * Устанавливает настройки из AsyncTreadPoolExecutorProperties
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(properties.getCorePoolSize());
        threadPool.setMaxPoolSize(properties.getMaxPoolSize());
        threadPool.setQueueCapacity(properties.getQueueCapacity());
        threadPool.setThreadNamePrefix(properties.getThreadNamePrefix());
        threadPool.initialize();
        return threadPool;
    }
}
