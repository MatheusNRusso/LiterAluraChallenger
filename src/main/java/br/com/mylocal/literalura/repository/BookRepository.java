package br.com.mylocal.literalura.repository;

import br.com.mylocal.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);

    List<Book> findTop5ByOrderByDownloadCountDesc();
}
