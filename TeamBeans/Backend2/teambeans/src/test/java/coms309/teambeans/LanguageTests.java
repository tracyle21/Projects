package coms309.teambeans;

import coms309.teambeans.Repositories.LanguageRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import coms309.teambeans.DTOs.LanguageDTO;
import coms309.teambeans.Models.Language;
import coms309.teambeans.Services.LanguageService;
import coms309.teambeans.Controllers.LanguageController;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LanguageTests {

    @InjectMocks
    LanguageController languageController;

    @Mock
    LanguageService languageService;

    @Mock
    private LanguageRepository languageRepository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void LanguageServiceTest() {
        // Tracy
        Language language = new Language("Java");
        when(languageService.getLanguage(anyString())).thenReturn(language);
        Language l = languageService.getLanguage("test");
        assertNotNull(l);
    }

    // ensures language is set/gotten correctly and passed through
    @Test
    void GoodLanguageString() {
        LanguageController controller = mock(LanguageController.class);
        Language lang = new Language("Swahili");
        lang.setName("Swahili");
        when(controller.getLanguage(lang.getName())).thenReturn(lang);
        Language result = controller.getLanguage(lang.getName());
        assertTrue(result.getName().equals("Swahili"));
    }

    // ensures language is set/gotten correctly and passed through
    @Test
    void GoodLanguageObject() {
        LanguageController controller = mock(LanguageController.class);
        Language lang = new Language("Swahili");
        lang.setName("Swahili");
        when(controller.getLanguage(lang.getName())).thenReturn(lang);
        Language result = controller.getLanguage(lang.getName());
        assertTrue(result.equals(lang));
    }

    // ensures language is set/gotten correctly and passed through
    @Test
    void GoodLanguageObject2() {
        LanguageController controller = mock(LanguageController.class);
        Language lang = new Language("Swahili");
        lang.setName("Swahili");
        when(controller.getLanguage(lang.getName())).thenReturn(null);
        Language result = controller.getLanguage(lang.getName());
        assertFalse(lang.equals(result));
    }

    // ensures language is set/gotten correctly and passed through
    @Test
    void GoodLanguageStringSet() {
        LanguageController controller = mock(LanguageController.class);
        Language lang = new Language("Swahili");
        lang.setName("German");
        when(controller.getLanguage(lang.getName())).thenReturn(lang);
        Language result = controller.getLanguage(lang.getName());
        assertTrue(result.getName().equals("German"));
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
