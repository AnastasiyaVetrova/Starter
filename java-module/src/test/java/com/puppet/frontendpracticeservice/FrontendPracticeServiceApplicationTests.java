package com.puppet.frontendpracticeservice;

import com.puppet.frontendpracticeservice.repository.TestContainerDataBase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

@Import(FrontendPracticeServiceApplication.class)
class FrontendPracticeServiceApplicationTests extends TestContainerDataBase {

    @Test
    void contextLoads() {
    }
}
