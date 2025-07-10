package br.com.mylocal.literalura.principal;

import br.com.mylocal.literalura.dto.AuthorDto;
import br.com.mylocal.literalura.dto.BookDto;
import br.com.mylocal.literalura.model.Author;
import br.com.mylocal.literalura.model.Book;
import br.com.mylocal.literalura.model.Language;
import br.com.mylocal.literalura.repository.AuthorRepository;
import br.com.mylocal.literalura.repository.BookRepository;
import br.com.mylocal.literalura.service.AuthorService;
import br.com.mylocal.literalura.service.BookService;
import br.com.mylocal.literalura.service.CatalogoService;
import br.com.mylocal.literalura.service.MenuService;
import br.com.mylocal.literalura.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Principal implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<BookDto> booksBuscados = new ArrayList<>();
    private final List<AuthorDto> authorsBuscados = new ArrayList<>();

    private BookRepository bookRepository;

    private AuthorRepository authorRepository;

    private BookService bookService;

    private MenuService menuService;

    private AuthorService authorService;

    public Principal(BookRepository bookRepository,
                     AuthorRepository authorRepository,
                     BookService bookService,
                     MenuService menuService,
                     AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.menuService = menuService;
        this.authorService = authorService;
    }

    @Override
    public void run(String... args) {
        startSystem();
    }

    public void startSystem() {
        int opcao;

        do {
            menuService.menu();
            opcao = menuService.readInt();

            switch (opcao) {
                case 1 -> buscarLivroPorTitulo();
                case 2 -> showBook();
                case 3 -> showAuthors();
                case 4 -> buscarAutoresVivosEntre();
                case 5 -> buscarLivroIdioma();
                case 6 -> System.out.println("⚙️ Em breve: Opções extras.");
                case 0 -> System.out.println("Saindo do sistema...");
                default -> System.out.println("❌ Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    @Transactional
    public void showBook() {
        List<Book> livrosArmazenados = bookRepository.findAll();
        if (livrosArmazenados.isEmpty()) {
            System.out.println("\n⚠️ Nenhum livro armazenado.");
        } else {
            System.out.println("\n📚 Livros armazenados no banco:\n");
            AtomicInteger index = new AtomicInteger(1);
            livrosArmazenados.forEach(book -> bookService.printDetails(book, index.getAndIncrement()));
        }
    }

    @Transactional
    public void showAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("\n<UNK> Nenhum author encontrado.");
        } else {
            System.out.println("\n<UNK> Autores encontrados:\n");
            AtomicInteger index = new AtomicInteger(1);
            authors.forEach(author -> authorService.printDetails(author, index.getAndIncrement()));
        }

    }

    public void saveAuthor(Author author)
    {
        authorRepository.save(author);
    }
//    public void showAuthors()
//    {
//        authorRepository.findAll()
//                .forEach(l -> System.out.println(l));
//    }

    private void buscarLivroPorTitulo() {

        String titulo = menuService.readLine("Digite o nome do livro: ");

        bookService.findBookByTitle(titulo, booksBuscados, authorsBuscados);
    }

    private void buscarAutoresVivosEntre() {
        System.out.println("Digite o ano inicial: ");
        int anoInicio = menuService.readInt();
        System.out.println("Digite o ano final: ");
        int anoFim = menuService.readInt();

        List<Author> autores = authorService.listarAutoresVivosEntre(anoInicio, anoFim);

        if (autores.isEmpty()) {
            System.out.println("⚠️ Nenhum autor vivo nesse intervalo.");
        } else {
            AtomicInteger i = new AtomicInteger(1);
            autores.forEach(a -> authorService.printDetails(a, i.getAndIncrement()));
        }
    }
    private void buscarLivroIdioma() {
        String input = menuService.readLine("Informe o idioma (ex: EN, PT, ES, FR): ").toUpperCase();

        Language.fromCode(input).ifPresentOrElse(
                idioma -> bookService.showTitleLanguage(idioma),
                () -> System.out.println("❌ Idioma inválido.")
        );
    }



}