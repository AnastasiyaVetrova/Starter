package com.puppet.frontendpracticeservice.domain.kafka;

import com.puppet.starter.image.ImageKafka;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность для топика topic-two")
public record RequisitesKafka(
        String name,
        String currentAccount,
        String kbk) implements ImageKafka {

    @Override
    public String toString() {
        return String.format("RequisitesKafka %s %s %s", name, currentAccount, kbk);
    }
}
