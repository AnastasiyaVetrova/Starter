package com.puppet.frontendpracticeservice.kafka;

import com.puppet.frontendpracticeservice.config.kafka.KafkaProducerConfig;
import com.puppet.frontendpracticeservice.service.kafka.KafkaProducer;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

@EmbeddedKafka(topics = "default-puppet", partitions = 1)
@SpringBootTest(classes = {KafkaProducer.class, KafkaProducerConfig.class, KafkaConsumerTest.class})
@ActiveProfiles("local")
@DirtiesContext
public class KafkaProducerTest {

    @Autowired
    private KafkaProducer producer;

    @Autowired
    KafkaConsumerTest consumer;

    @Test
    void kafkaProducerTest() throws InterruptedException {
        String message = "Hello";
        producer.sendMessage(message);

        boolean messageReceived = consumer.getLatch().await(10, TimeUnit.SECONDS);

        assertAll(() -> {
            assertTrue(messageReceived, "Сообщение не было получено в течение времени ожидания");
            assertEquals(message, consumer.getResult());
        });
    }
}