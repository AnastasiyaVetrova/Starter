package com.puppet.frontendpracticeservice.repository;

import com.puppet.frontendpracticeservice.domain.request.RequisitesDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class RequisitesRepositoryTest extends TestContainerDataBase {
    private static final UUID testId = UUID.fromString("7220e843-6558-4750-9c3e-b4444324ca3e");

    @Autowired
    private RequisitesRepository requisitesRepository;

    @Test
    public void testFindRequisitesDtoById() {
        RequisitesDto expectedRequisites = new RequisitesDto(
                "Марфа", "Марфович", "77777777777777777777", "77777777777777777777");

        RequisitesDto actualRequisites = requisitesRepository.findRequisitesDtoById(testId).orElse(null);

        assertEquals(expectedRequisites, actualRequisites);
    }
}