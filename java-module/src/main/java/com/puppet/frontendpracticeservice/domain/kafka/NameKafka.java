package com.puppet.frontendpracticeservice.domain.kafka;

import com.puppet.starter.image.ImageKafka;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность для топика topic-one")
public record NameKafka(
        String name,
        String surname) implements ImageKafka {

    @Override
    public String toString() {
        return String.format("NameKafka %s %s", name, surname);
    }
}
