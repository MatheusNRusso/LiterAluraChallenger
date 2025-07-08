package br.com.mylocal.literalura.model;

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


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private List<Language> languages;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "author_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();

    public Book(BookDto livro) {
        this.title = livro.title();

        this.languages = livro.languages().stream()
                .map(code -> Language.fromCode(code)
                        .orElseThrow(() -> new IllegalArgumentException("Idioma desconhecido: " + code)))
                .collect(Collectors.toList());

        this.downloadCount = livro.downloadCount();
        this.authors = livro.authors().stream()
                .map(Author::new)
                .collect(Collectors.toList());
    }
    @Override
    public String toString() {
        return String.format(
                "Livro: %s, Downloads: %d, Autores: %s, Idiomas: %s",
                title,
                downloadCount,
                authors.stream().map(Author::getName).collect(Collectors.joining(", ")),
                (languages != null ? languages.stream().map(Enum::name).collect(Collectors.joining(", ")) : "nenhum")
        );
    }
}
