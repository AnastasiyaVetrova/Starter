package com.puppet.frontendpracticeservice.service.impl;

import com.puppet.frontendpracticeservice.domain.kafka.NameKafka;
import com.puppet.frontendpracticeservice.domain.kafka.RequisitesKafka;
import com.puppet.frontendpracticeservice.domain.request.RequisitesDto;
import com.puppet.frontendpracticeservice.exception.UserNotFoundException;
import com.puppet.frontendpracticeservice.repository.RequisitesRepository;
import com.puppet.frontendpracticeservice.service.RequisitesService;
import com.puppet.frontendpracticeservice.service.kafka.EnumKafkaTopic;
import com.puppet.frontendpracticeservice.service.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequisitesServiceImpl implements RequisitesService {
    private final RequisitesRepository requisitesRepository;
    private final KafkaProducer kafkaProducer;

    @Override
    public RequisitesDto findRequisites(UUID user_id) {
        RequisitesDto dto = requisitesRepository.findRequisitesDtoById(user_id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        kafkaProducer.sendMessage(
                EnumKafkaTopic.TOPIC_NAME_ONE, new NameKafka(dto.name(), dto.surname()));
        kafkaProducer.sendMessage(
                EnumKafkaTopic.TOPIC_NAME_TWO, new RequisitesKafka(dto.name(), dto.currentAccount(), dto.kbk()));

        System.out.println(Thread.currentThread().getName());
        return dto;
    }
}