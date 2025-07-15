package br.com.mylocal.literalura.service;

import br.com.mylocal.literalura.dto.BookDto;
import br.com.mylocal.literalura.dto.BookResultDto;
import br.com.mylocal.literalura.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CatalogoService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static BookResultDto getBookResultDto(String title) throws IOException, InterruptedException {
        String endpoint = Constants.buildSearch(title);
        String json = ApiService.fetchApiResponse(endpoint);

        BookResultDto resultado = objectMapper.readValue(json, BookResultDto.class);

        if (resultado.results() == null || resultado.results().isEmpty()) {
            System.out.println("📚 Nenhum livro encontrado com o título: " + title);
            return null;
        }

        return resultado;
    }

    public static List<BookDto> findBook(String title) throws IOException, InterruptedException {
        BookResultDto books = getBookResultDto(title);

        if (books == null) {
            return Collections.emptyList();
        }

        return books.results();
    }
}
