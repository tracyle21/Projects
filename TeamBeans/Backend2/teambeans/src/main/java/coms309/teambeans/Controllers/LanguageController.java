package coms309.teambeans.Controllers;


import coms309.teambeans.Models.Language;
import coms309.teambeans.Services.LanguageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "LanguageController", tags = {"Language API"})
@SwaggerDefinition(tags = {
        @Tag(name = "Language API", description = "Admin interface to manage languages")
})
@RestController
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @ApiOperation(value = "Get List of All Languages")
    @GetMapping("/languages")
    public List<Language> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @ApiOperation(value = "Get a specific language based on name")
    @GetMapping("/languages/{name}")
    public Language getLanguage(@PathVariable String name) {
        return languageService.getLanguage(name);
    }

    @ApiOperation(value = "Add a new language")
    @PostMapping("/languages")
    public void addLanguage(@RequestBody Language language) {
        languageService.addLanguage(language);
    }
}
