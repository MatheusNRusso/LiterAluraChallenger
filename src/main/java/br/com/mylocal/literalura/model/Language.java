package br.com.mylocal.literalura.model;

import java.util.Arrays;
import java.util.Optional;

public enum Language {
    EN("en"),
    PT("pt"),
    FR("fr"),
    ES("es");

    private final String language;


    Language(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public static Optional<Language> fromCode(String code) {
        return Arrays.stream(values())
                .filter(lang -> lang.language.equalsIgnoreCase(code))
                .findFirst();
    }
}
