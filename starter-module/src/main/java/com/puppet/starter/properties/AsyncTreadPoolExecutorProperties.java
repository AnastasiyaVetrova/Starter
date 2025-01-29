package com.puppet.starter.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Конфигурация для настройки пула потоков для @Async
 * Данные загружаются из application.yaml
 */
@Component
@ConfigurationProperties(prefix = "my-setting-kafka.my-async.thread-pool")
@Setter
@Getter
public class AsyncTreadPoolExecutorProperties {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private String threadNamePrefix;
}
