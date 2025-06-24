package br.com.mylocal.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDto(int id, String title,
                      List<AuthorDto> authors,
                      List<String> languages,
                      @JsonAlias("download_count") int downloadCount) {
}
