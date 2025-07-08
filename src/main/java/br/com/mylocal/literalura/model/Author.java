package br.com.mylocal.literalura.model;

import br.com.mylocal.literalura.dto.AuthorDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@AllArgsConstructor
@NoArgsConstructor @Getter @Setter
@EqualsAndHashCode(of = "id")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String name;

    @Column(name = "birth_year", nullable = false)
    private int birthYear;

    @Column(name = "death_year", nullable = false)
    private int deathYear;

    @ManyToMany(mappedBy = "authors")
    private List<Book> book = new ArrayList<>();
    @Override
    public String toString() {
        return String.format("Author{name='%s', birth=%d, death=%d}", name, birthYear, deathYear);
    }
    public Author(AuthorDto authorDto) {
        this.name = authorDto.name();
        this.birthYear = authorDto.birthYear();
        this.deathYear = authorDto.deathYear();
    }
}
