package br.com.mylocal.literalura.service;

import br.com.mylocal.literalura.dto.AuthorExportDto;
import br.com.mylocal.literalura.dto.BookExportDto;
import br.com.mylocal.literalura.model.Author;
import br.com.mylocal.literalura.model.Book;
import br.com.mylocal.literalura.repository.AuthorRepository;
import br.com.mylocal.literalura.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public ExportService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void exportarLivros(String formato) {
        List<Book> livros = bookRepository.findAll();
        List<BookExportDto> exportList = livros.stream()
                .map(book -> new BookExportDto(
                        book.getTitle(),
                        book.getDownloadCount(),
                        book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()),
                        book.getLanguages().stream().map(Enum::name).collect(Collectors.toList())
                )).toList();

        if (formato.equalsIgnoreCase("json")) {
            salvarJson(exportList, "livros.json");
        } else {
            salvarCsvLivros(exportList, "livros.csv");
        }
    }

    public void exportarAutores(String formato) {
        List<Author> autores = authorRepository.findAll();
        List<AuthorExportDto> exportList = autores.stream()
                .map(author -> new AuthorExportDto(
                        author.getName(),
                        author.getBirthYear(),
                        author.getDeathYear(),
                        author.getBooks().stream().map(Book::getTitle).toList()
                )).toList();

        if (formato.equalsIgnoreCase("json")) {
            salvarJson(exportList, "autores.json");
        } else {
            salvarCsvAutores(exportList, "autores.csv");
        }
    }

    private <T> void salvarJson(List<T> data, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Path path = Path.of("export", fileName);
            Files.createDirectories(path.getParent());
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), data);
        } catch (IOException e) {
            System.err.println("Erro ao exportar JSON: " + e.getMessage());
        }
    }

    private void salvarCsvLivros(List<BookExportDto> livros, String fileName) {
        try (CSVWriter writer = new CSVWriter(new FileWriter("export/" + fileName))) {
            writer.writeNext(new String[]{"Título", "Downloads", "Autores", "Idiomas"});
            for (BookExportDto b : livros) {
                writer.writeNext(new String[]{
                        b.title(),
                        String.valueOf(b.downloadCount()),
                        String.join(", ", b.authors()),
                        String.join(", ", b.languages())
                });
            }
        } catch (IOException e) {
            System.err.println("Erro ao exportar CSV de livros: " + e.getMessage());
        }
    }

    private void salvarCsvAutores(List<AuthorExportDto> autores, String fileName) {
        try (CSVWriter writer = new CSVWriter(new FileWriter("export/" + fileName))) {
            writer.writeNext(new String[]{"Nome", "Nascimento", "Falecimento", "Livros"});
            for (AuthorExportDto a : autores) {
                writer.writeNext(new String[]{
                        a.name(),
                        String.valueOf(a.birthYear()),
                        String.valueOf(a.deathYear()),
                        String.join(", ", a.bookTitles())
                });
            }
        } catch (IOException e) {
            System.err.println("Erro ao exportar CSV de autores: " + e.getMessage());
        }
    }
}
