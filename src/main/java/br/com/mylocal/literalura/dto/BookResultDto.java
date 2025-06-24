package br.com.mylocal.literalura.dto;

import java.util.List;

public record BookResultDto(Integer count,
                            String next, String previous,
                            List<BookDto> results) {
}
