package br.com.mylocal.literalura.repository;

import br.com.mylocal.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);

    List<Author> findByNameAndBirthYearAndDeathYear(String name, int  birthYear, int deathYear);

    List<Author> findByNameContainingIgnoreCase(String name);
}
