package com.puppet.frontendpracticeservice.service.kafka;

import com.puppet.frontendpracticeservice.domain.kafka.NameKafka;
import com.puppet.frontendpracticeservice.domain.kafka.RequisitesKafka;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class KafkaCustomer {
    
    @Value("${spring.kafka.template.default-topic}")
    private String defaultTopic;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumers.consumer-one.group-id}",
            containerFactory = "defaultListener")
    public void listenStringZero(@Payload String message,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                 @Header(KafkaHeaders.OFFSET) Integer offset) {
        System.out.printf("№ %s, par = %s, o = %s\n", message, partition, offset);
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.topic-one-json}",
            groupId = "${spring.kafka.consumers.consumer-two.group-id}",
            containerFactory = "dynamicListener")
    public void listenJsonOne(@Payload NameKafka message) {
        System.out.println(message);
    }

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.topic-two-json}",
            groupId = "${spring.kafka.consumers.consumer-two.group-id}",
            containerFactory = "dynamicListener")
    public void listenJsonTwo(@Payload RequisitesKafka message) {
        System.out.println("Ошибка в listenJsonTwo");
        throw new RuntimeException();
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumers.consumer-one.group-id}",
            containerFactory = "defaultListener")
    public void listenStringOne(@Payload String message,
                                @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                                @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                @Header(KafkaHeaders.OFFSET) Integer offset) throws InterruptedException {
        System.out.printf("№ %s, par = %s, o = %s\n", message, partition, offset);
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumers.consumer-one.group-id}",
            containerFactory = "defaultListener")
    public void listenStringTwo(@Payload String message,
                                @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                                @Header(KafkaHeaders.OFFSET) Integer offset) {
        System.out.printf("№ %s, par = %s, o = %s\n", message, partition, offset);
    }
}