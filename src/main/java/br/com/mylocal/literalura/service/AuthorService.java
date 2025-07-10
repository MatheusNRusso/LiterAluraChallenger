package br.com.mylocal.literalura.service;

import br.com.mylocal.literalura.model.Author;
import br.com.mylocal.literalura.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> listarTodos() {
        return authorRepository.findAll();
    }

    public List<Author> listarAutoresVivos() {
        return authorRepository.findAll()
                .stream()
                .filter(a -> a.getDeathYear() == 0)
                .toList();
    }

    public void printDetails(Author author, int index) {
        System.out.println("=".repeat(50));
        System.out.printf("👤 Autor #%d%n", index);
        System.out.println("Nome: " + author.getName());

        String nascimento = author.getBirthYear() > 0 && author.getBirthYear() > 0
                ? String.valueOf(author.getBirthYear()) : "?";
        String falecimento = author.getDeathYear() > 0 && author.getDeathYear() > 0
                ? String.valueOf(author.getDeathYear()) : "?";

        System.out.println("📅 Vida: " + nascimento + " - " + falecimento);

        if (author.getBook() != null) {
            System.out.println("📖 Livro: " + author.getBook().getTitle());
        } else {
            System.out.println("📖 Livro: (não vinculado)");
        }

        System.out.println("=".repeat(50));
    }

    public List<Author> listarAutoresVivosEntre(int anoInicial, int anoFinal) {
        return authorRepository.findAll()
                .stream()
                .filter(autor -> {
                    Integer nascimento = autor.getBirthYear();
                    Integer morte = autor.getDeathYear();

                    return nascimento != null && nascimento <= anoFinal
                            && (morte == null || morte >= anoInicial);
                })
                .toList();
    }

}
