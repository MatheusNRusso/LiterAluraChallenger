package br.com.mylocal.literalura.dto;

import br.com.mylocal.literalura.model.Author;

import java.util.List;

public record BookExportDto(
        String title,
        int downloadCount,
        List<String> authors,
        List<String> languages
) {}
