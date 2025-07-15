package br.com.mylocal.literalura.service;

import br.com.mylocal.literalura.model.Author;
import br.com.mylocal.literalura.model.Book;
import br.com.mylocal.literalura.model.Language;
import br.com.mylocal.literalura.repository.AuthorRepository;
import br.com.mylocal.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstatisticaService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public EstatisticaService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void totalLivros() {
        long total = bookRepository.count();
        System.out.println("📚 Total de livros cadastrados: " + total);
    }

    public void totalAutores() {
        long total = authorRepository.count();
        System.out.println("👤 Total de autores cadastrados: " + total);
    }

    public void mediaDownloadsPorIdioma() {
        List<Book> livros = bookRepository.findAll();
        Map<Language, Double> mediaPorIdioma = Arrays.stream(Language.values())
                .collect(Collectors.toMap(
                        idioma -> idioma,
                        idioma -> livros.stream()
                                .filter(l -> l.getLanguages().contains(idioma))
                                .mapToInt(Book::getDownloadCount)
                                .average()
                                .orElse(0.0)
                ));

        System.out.println("\n📊 Média de downloads por idioma:");
        mediaPorIdioma.forEach((idioma, media) ->
                System.out.printf("🌐 %s: %.2f downloads em média%n", idioma.name(), media));
    }

    public void idiomaMaisFrequente() {
        List<Book> livros = bookRepository.findAll();
        Map<Language, Long> contagem = livros.stream()
                .flatMap(l -> l.getLanguages().stream())
                .collect(Collectors.groupingBy(lang -> lang, Collectors.counting()));

        contagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> System.out.println("🌍 Idioma com mais livros: " + entry.getKey() + " (" + entry.getValue() + " livros)"));
    }

    public void livroMaisBaixadoPorIdioma() {
        List<Book> livros = bookRepository.findAll();
        Map<Language, Optional<Book>> topPorIdioma = Arrays.stream(Language.values())
                .collect(Collectors.toMap(
                        lang -> lang,
                        lang -> livros.stream()
                                .filter(l -> l.getLanguages().contains(lang))
                                .max(Comparator.comparingInt(Book::getDownloadCount))
                ));

        System.out.println("\n📘 Livro mais baixado por idioma:");
        topPorIdioma.forEach((lang, livroOpt) -> livroOpt.ifPresent(livro ->
                System.out.printf("%s ➜ %s (%d downloads)%n", lang.name(), livro.getTitle(), livro.getDownloadCount())));
    }

    public void autorComMaisLivros() {
        List<Author> autores = authorRepository.findAll();
        autores.stream()
                .max(Comparator.comparingInt(a -> a.getBooks().size()))
                .ifPresent(autor -> System.out.println("👑 Autor com mais livros: " + autor.getName() + " (" + autor.getBooks().size() + " livros)"));
    }

    public void intervaloVidaMediaAutores() {
        List<Author> autores = authorRepository.findAll();
        Double media = autores.stream()
                .filter(a -> a.getBirthYear() > 0 && a.getDeathYear() > 0)
                .mapToInt(a -> a.getDeathYear() - a.getBirthYear())
                .average()
                .orElse(0.0);

        System.out.printf("📈 Expectativa média de vida dos autores (com datas válidas): %.2f anos%n", media);
    }
}
