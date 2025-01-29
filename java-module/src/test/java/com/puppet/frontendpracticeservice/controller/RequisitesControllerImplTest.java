package com.puppet.frontendpracticeservice.controller;

import com.puppet.frontendpracticeservice.controller.impl.RequisitesControllerImpl;
import com.puppet.frontendpracticeservice.handler.ControllerExceptionHandler;
import com.puppet.frontendpracticeservice.repository.TestContainerDataBase;
import com.puppet.frontendpracticeservice.service.impl.RequisitesServiceImpl;
import com.puppet.frontendpracticeservice.service.kafka.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@Import({RequisitesControllerImpl.class, RequisitesServiceImpl.class})
class RequisitesControllerImplTest extends TestContainerDataBase {
    private static final UUID testId = UUID.fromString("7220e843-6558-4750-9c3e-b4444324ca3e");
    private static final UUID invalidTestId = UUID.fromString("0000e000-0000-4750-9c3e-b4444324ca3e");
    private static final String jsonResponse = """
                {
                "name": "Марфа",
                "surname": "Марфович",
                "currentAccount": "77777777777777777777",
                "kbk": "77777777777777777777"
                }
            """;

    private MockMvc mockMvc;

    @Autowired
    private RequisitesServiceImpl requisitesService;
    @Autowired
    private RequisitesControllerImpl requisitesController;
    @MockBean
    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(requisitesController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void givenValidId_whenGetRequisites_thenRequisitesDto200Status() throws Exception {
        mockMvc.perform(get("/frontend-practice/requisites/{id}", testId))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(jsonResponse))
                .andDo(print());
    }

    @Test
    void givenInvalidId_whenGetRequisites_thenRequisitesDto200Status() throws Exception {
        mockMvc.perform(get("/frontend-practice/requisites/{id}", invalidTestId))
                .andExpect(
                        status().isNotFound())
                .andDo(print());
    }
}