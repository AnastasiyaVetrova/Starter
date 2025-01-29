package com.puppet.frontendpracticeservice.service;

import com.puppet.frontendpracticeservice.domain.request.RequisitesDto;
import com.puppet.frontendpracticeservice.exception.UserNotFoundException;
import com.puppet.frontendpracticeservice.repository.RequisitesRepository;
import com.puppet.frontendpracticeservice.service.impl.RequisitesServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RequisitesServiceImplTest {
    private static final UUID testId = UUID.fromString("7220e843-6558-4750-9c3e-b4444324ca3e");
    private static final UUID invalidTestId = UUID.fromString("0000e000-0000-4750-9c3e-b4444324ca3e");

    @Mock
    RequisitesRepository requisitesRepository;

    @InjectMocks
    private RequisitesServiceImpl requisitesService;

    @BeforeAll
    static void setUp() {

    }

    @Test
    void findRequisites_andReturnValidData() {
        RequisitesDto expectedRequisites = new RequisitesDto(
                "Марфа", "Марфович", "77777777777777777777", "77777777777777777777");
        when(requisitesRepository.findRequisitesDtoById(testId)).thenReturn(Optional.of(expectedRequisites));

        RequisitesDto actualRequisites = requisitesService.findRequisites(testId);

        assertEquals(expectedRequisites, actualRequisites);
    }

    @Test
    void findRequisites_andReturnInvalidData() {
        when(requisitesRepository.findRequisitesDtoById(invalidTestId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> requisitesService.findRequisites(invalidTestId));
    }
}