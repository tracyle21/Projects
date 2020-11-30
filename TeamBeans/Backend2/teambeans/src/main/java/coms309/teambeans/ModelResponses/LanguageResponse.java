package coms309.teambeans.ModelResponses;

import java.util.List;

public class LanguageResponse {
    List<String> languages;

    public LanguageResponse() {
    }

    public LanguageResponse(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
