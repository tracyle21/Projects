package coms309.teambeans;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import coms309.teambeans.DTOs.UserLoginDTO;
import coms309.teambeans.DTOs.UserRegistrationDTO;
import coms309.teambeans.Services.UserService;
import coms309.teambeans.Controllers.UserController;
import coms309.teambeans.Controllers.LanguageController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeambeansApplicationTests {

    @Test
    void contextLoads() {
    }

    // for debugging
    @BeforeAll
    private static void before() {
        System.out.println("Before");
    }

    // for debugging
    @AfterAll
    private static void after() {
        System.out.println("After");
    }
}
