package com.puppet.starter;

import com.puppet.starter.annotation.EnableKafkaProducer;
import com.puppet.starter.enums.KeyValueSerializer;
import com.puppet.starter.image.ImageKafka;
import com.puppet.starter.properties.KafkaCustomerConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

/**
 * Конфигурация позволяет создавать слушателей Kafka. Создает 2 containerFactory с десериализацией
 * значений StringDeserializer и JsonDeserializer соответственно.
 * ContainerFactory<String, ImageKafka> позволяет обрабатывать ошибки в слушателях, перенаправляя сообщения
 * в dlt-topic. DTO должны быть наследниками ImageKafka.class
 * Для слушателей в аннотации @KafkaListener требуется указать название бина в настройке
 * containerFactory = "defaultListener" либо "dynamicListener".
 * Настройки по умолчанию:
 * my-customer:
 * default-prop:
 * bootstrap: ${KAFKA_BOOTSTRAP_SERVERS}
 * concurrency: 1
 * dynamic-prop:
 * bootstrap: ${KAFKA_BOOTSTRAP_SERVERS}
 * concurrency: 1
 * dlt-topic: default-puppet
 * interval: 1000
 * max-attempts: 1
 */
@Component
@EnableConfigurationProperties(KafkaCustomerConfigurationProperties.class)
@RequiredArgsConstructor
@Slf4j
public class KafkaCustomerConfiguration {

    private final KafkaCustomerConfigurationProperties properties;

    @EnableKafkaProducer(valueSerializer = KeyValueSerializer.JSON)
    private final KafkaTemplate<String, ImageKafka> kafkaTemplate;

    /**
     * Создание ContainerFactory с десериализацией StringDeserializer
     *
     * @return ConcurrentKafkaListenerContainerFactory<String, String>
     */
    @Bean("defaultListener")
    @ConditionalOnMissingBean(name = "defaultListener")
    public ConcurrentKafkaListenerContainerFactory<String, String> consumerDefaultFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getDefaultProp().getBootstrap(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)));
        factory.setConcurrency(properties.getDynamicProp().getConcurrency());

        return factory;
    }

    /**
     * Создание ContainerFactory с десериализацией JsonDeserializer
     *
     * @return ConcurrentKafkaListenerContainerFactory<String, ImageKafka>
     */
    @Bean("dynamicListener")
    @ConditionalOnMissingBean(name = "dynamicListener")
    public ConcurrentKafkaListenerContainerFactory<String, ImageKafka> consumerDynamicFactory() {

        ConcurrentKafkaListenerContainerFactory<String, ImageKafka> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerDynamic());
        factory.setCommonErrorHandler(getDefaultErrorHandler());
        factory.setConcurrency(properties.getDynamicProp().getConcurrency());
        return factory;
    }

    /**
     * Создание ConsumerFactory для бина "dynamicListener"
     *
     * @return ConsumerFactory<String, ImageKafka>
     */
    public ConsumerFactory<String, ImageKafka> consumerDynamic() {

        return new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getDynamicProp().getBootstrap(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class,
                JsonDeserializer.TRUSTED_PACKAGES, "*"));
    }

    /**
     * Создание обработчика ошибок бина "dynamicListener" и настройка условий срабатывания
     *
     * @return DefaultErrorHandler
     */
    public DefaultErrorHandler getDefaultErrorHandler() {
        return new DefaultErrorHandler(
                getDeadLetter(), new FixedBackOff(
                properties.getDynamicProp().getInterval(),
                properties.getDynamicProp().getMaxAttempts()));
    }

    /**
     * Перенаправление запросов в DLT, перенаправляет запросы и смещает офсет
     *
     * @return DeadLetterPublishingRecoverer
     */
    public DeadLetterPublishingRecoverer getDeadLetter() {
        return new DeadLetterPublishingRecoverer(kafkaTemplate,
                (ConsumerRecord<?, ?> record, Exception ex) ->
                        new TopicPartition(properties.getDynamicProp().getDltTopic(), 0));
    }
}