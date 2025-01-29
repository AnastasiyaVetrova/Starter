package com.puppet.starter.properties;

import com.puppet.starter.enums.KeyValueSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Характеристики, полученные из аннотации для настройки KafkaTemplate
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaProducerConfigurationProperties {
    private String bootstrapServers;
    private KeyValueSerializer keySerializer;
    private KeyValueSerializer valueSerializer;
    private String batchSize;
}
