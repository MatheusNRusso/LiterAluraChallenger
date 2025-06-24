package br.com.mylocal.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AuthorDto(String name,
                        @JsonAlias("birth_year") int birthYear,
                        @JsonAlias("death_year") int deathYear) {
}
