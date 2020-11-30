package coms309.teambeans.Services;

import coms309.teambeans.Models.Language;
import coms309.teambeans.Repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<Language> getAllLanguages() {
        List<Language> languages = new ArrayList<>();
        languageRepository.findAll()
                .forEach(languages::add);
        return languages;
    }

    public Language getLanguage(String name) {
        return languageRepository.findByName(name);
    }

    public void addLanguage(Language language) {
        languageRepository.save(language);
    }
}
