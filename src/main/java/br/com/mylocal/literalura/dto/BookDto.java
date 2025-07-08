package br.com.mylocal.literalura.dto;

import br.com.mylocal.literalura.model.Author;
import br.com.mylocal.literalura.model.Language;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDto(int id,
                      String title,
                      List<AuthorDto> authors,
                      @JsonAlias("languages" ) List<String>  languages,
                      @JsonAlias("download_count") int downloadCount) {
}
