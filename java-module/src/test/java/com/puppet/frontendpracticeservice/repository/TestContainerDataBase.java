package com.puppet.frontendpracticeservice.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@DataJpaTest
@Transactional
@Sql("classpath:db/changelog/script-sql/users.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
public abstract class TestContainerDataBase {
    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("test_bd")
                    .withUsername("test_user")
                    .withPassword("test_password")
                    .withReuse(true);

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.liquibase.change-log", () -> "classpath:db/changelog/changelog-master.yaml");
    }

    @BeforeAll
    static void setUpBeforeAll() {
        container.start();
    }

    @AfterAll
    static void tearDownAfterAll() {
        container.stop();
    }
}