package br.com.mylocal.literalura.model;

import br.com.mylocal.literalura.dto.AuthorDto;
import br.com.mylocal.literalura.dto.BookDto;
import br.com.mylocal.literalura.model.Author;
import br.com.mylocal.literalura.model.Language;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private List<Language> languages = new ArrayList<>();

    public Book(BookDto dto) {
        this.title = dto.title();
        this.downloadCount = dto.downloadCount();
        this.languages = dto.languages().stream()
                .map(code -> Language.fromCode(code)
                        .orElseThrow(() -> new IllegalArgumentException("Idioma desconhecido: " + code)))
                .collect(Collectors.toList());
        // NÃO atribui authors aqui — deixe para o serviço
    }

    public Book(String title, int i, List<Author> authors, List<Language> languages) {
        this.title = title;
        this.downloadCount = i;
        this.authors = authors;
        this.languages = languages;


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
