package com.puppet.frontendpracticeservice.service.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumKafkaTopic {
    TOPIC_NAME_ONE("topic-one"),
    TOPIC_NAME_TWO("topic-two");

    private final String topic;
}
