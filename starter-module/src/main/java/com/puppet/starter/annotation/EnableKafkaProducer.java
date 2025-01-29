package com.puppet.starter.annotation;

import com.puppet.starter.enums.KeyValueSerializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотацию @EnableKafkaProducer необходимо ставить над соответствующим полем
 * для внедрения KafkaTemplate с предустановленной настройкой
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface EnableKafkaProducer {
    String bootstrapServers() default "localhost:29092;localhost:29093;localhost:29094";

    KeyValueSerializer keySerializer() default KeyValueSerializer.STRING;

    KeyValueSerializer valueSerializer() default KeyValueSerializer.STRING;

    String batchSize() default "50";

    Class<? extends Payload>[] payload() default {};
}
