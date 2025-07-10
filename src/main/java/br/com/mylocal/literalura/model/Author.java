package br.com.mylocal.literalura.model;

import br.com.mylocal.literalura.dto.AuthorDto;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public Author(AuthorDto authorDto) {
        this.name = authorDto.name();
        this.birthYear = authorDto.birthYear();
        this.deathYear = authorDto.deathYear();
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
