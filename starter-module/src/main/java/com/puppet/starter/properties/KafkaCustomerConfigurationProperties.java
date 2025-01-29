package com.puppet.starter.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Настройки, полученные из application.yaml ConcurrentKafkaListenerContainerFactory
 */
@Component
@ConfigurationProperties(prefix = "my-setting-kafka.my-customer")
@Getter
@Setter
public class KafkaCustomerConfigurationProperties {
    private DefaultProperties defaultProp;
    private DynamicProperties dynamicProp;

    /**
     * Настройки ConcurrentKafkaListenerContainerFactory<String, String>
     */
    @Getter
    @Setter
    public static class DefaultProperties {
        private String bootstrap;
        private Integer concurrency;
    }

    /**
     * Настройки ConcurrentKafkaListenerContainerFactory<String, ImageKafka>
     */
    @Getter
    @Setter
    public static class DynamicProperties {
        private String bootstrap;
        private Integer concurrency;
        private String dltTopic;
        private Long interval;
        private Long maxAttempts;
    }
}
