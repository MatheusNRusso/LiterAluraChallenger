package br.com.mylocal.literalura.model;

import br.com.mylocal.literalura.dto.AuthorDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Author")
@Table(name = "authors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "birth_year", nullable = false)
    private int birthYear;

    @Column(name = "death_year", nullable = false)
    private int deathYear;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public Author(AuthorDto authorDto) {
        this.name = authorDto.name();
        this.birthYear = authorDto.birthYear();
        this.deathYear = authorDto.deathYear();
    }

    public Author(String name, int birthYear, int deathYear) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }


    @Override
    public String toString() {
        return String.format("""
                 Author{
                        name=%s,
                        birth=%d, 
                        death=%d
                }""",name, birthYear, deathYear);

    }
}
