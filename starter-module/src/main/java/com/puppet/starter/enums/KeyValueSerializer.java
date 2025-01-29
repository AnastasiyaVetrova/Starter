package com.puppet.starter.enums;

import com.puppet.starter.enums.serializer.MyJsonSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Форматы передачи сообщений в Kafka
 */
@RequiredArgsConstructor
@Getter
public enum KeyValueSerializer {
    STRING(StringSerializer.class),
    LONG(LongSerializer.class),
    JSON(MyJsonSerializer.class);

    private final Class<? extends Serializer<?>> serializerClass;
}
