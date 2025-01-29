package com.puppet.starter.enums.serializer;

import com.puppet.starter.image.ImageKafka;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Позволяет принимать и отправлять сообщения в формате json
 */
public class MyJsonSerializer extends JsonSerializer<ImageKafka> {
}
