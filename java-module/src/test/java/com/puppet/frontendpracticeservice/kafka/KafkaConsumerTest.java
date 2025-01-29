package com.puppet.frontendpracticeservice.kafka;

import lombok.Getter;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@TestComponent
@Getter
public class KafkaConsumerTest {
    private CountDownLatch latch = new CountDownLatch(1);
    private String result = "";

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void listen(String message) {
        result = message;
        latch.countDown();
    }
}
