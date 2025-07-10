package br.com.mylocal.literalura.model;

import br.com.mylocal.literalura.dto.AuthorDto;
import br.com.mylocal.literalura.dto.BookDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity(name = "Book")
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int downloadCount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Author> authors = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private List<Language> languages = new ArrayList<>();


    public Book(BookDto livro) {
        this.title = livro.title();

        this.languages = livro.languages().stream()
                .map(code -> Language.fromCode(code)
                        .orElseThrow(() -> new IllegalArgumentException("Idioma desconhecido: " + code)))
                .collect(Collectors.toList());

        this.downloadCount = livro.downloadCount();

        this.authors = livro.authors().stream()
                .map(authorDto -> {
                    Author author = new Author(authorDto);
                    author.setBook(this); // <-- IMPORTANTE
                    return author;
                })
                .collect(Collectors.toList());
    }

    public Book(String title, int downloadCount, List<AuthorDto> authorDtos, List<String> languageCodes) {
        this.title = title;
        this.downloadCount = downloadCount;
        this.authors = authorDtos.stream()
                .map(Author::new)
                .collect(Collectors.toList());
        this.languages = languageCodes.stream()
                .map(code -> Language.fromCode(code)
                        .orElseThrow(() -> new IllegalArgumentException("Idioma desconhecido: " + code)))
                .collect(Collectors.toList());
    }


    @Override
    public String toString() {
        String autores = (authors != null && !authors.isEmpty())
                ? authors.stream().map(Author::getName).collect(Collectors.joining(", "))
                : "nenhum";

        String idiomas = (languages != null && !languages.isEmpty())
                ? languages.stream().map(Enum::name).collect(Collectors.joining(", "))
                : "nenhum";

        return String.format(
                "Livro: %s, Downloads: %d, Autores: %s, Idiomas: %s",
                title,
                downloadCount,
                autores,
                idiomas
        );
    }

}
