package br.com.mylocal.literalura.dto;

import java.util.List;

public record AuthorExportDto(
        String name,
        int birthYear,
        int deathYear,
        List<String> bookTitles
) {}
