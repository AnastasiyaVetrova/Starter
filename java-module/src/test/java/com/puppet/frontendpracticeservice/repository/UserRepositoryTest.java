package com.puppet.frontendpracticeservice.repository;

import com.puppet.frontendpracticeservice.domain.entity.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

class UserRepositoryTest extends TestContainerDataBase {
    private static final String testLogin = "Марфа3";
    private static final UUID testId = UUID.fromString("7220e843-6558-4750-9c3e-b4444324ca3e");

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByLogin() {
        Optional<User> userOptional = userRepository.findByLogin(testLogin);

        assertTrue(userOptional.isPresent());
        assertEquals(testId, userOptional.get().getId());
    }

    @Test
    void testExistsByLogin() {
        boolean userExists = userRepository.existsByLogin(testLogin);

        assertTrue(userExists);
    }
}