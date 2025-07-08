package br.com.mylocal.literalura.principal;

import br.com.mylocal.literalura.dto.AuthorDto;
import br.com.mylocal.literalura.dto.BookDto;
import br.com.mylocal.literalura.model.Author;
import br.com.mylocal.literalura.model.Book;
import br.com.mylocal.literalura.repository.AuthorRepository;
import br.com.mylocal.literalura.repository.BookRepository;
import br.com.mylocal.literalura.service.CatalogoService;
import br.com.mylocal.literalura.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Principal implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<BookDto> booksBuscados = new ArrayList<>();
    private final List<AuthorDto> authorsBuscados = new ArrayList<>();

    private BookRepository bookRepository;

    private AuthorRepository authorRepository;

    public Principal(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public void run(String... args) {
        startSystem();
    }

    public void startSystem() {
        int opcao;

        do {
            menu();
            opcao = readInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> buscarLivroPorTitulo();
                case 2 -> showBook();
                case 3 -> showAuthors();
                case 4 -> System.out.println("📅 Em breve: Autores por ano.");
                case 5 -> System.out.println("🌍 Em breve: Livros por idioma.");
                case 6 -> System.out.println("⚙️ Em breve: Opções extras.");
                case 0 -> System.out.println("Saindo do sistema...");
                default -> System.out.println("❌ Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }

    public void menu() {
        System.out.println("\n=================== BEM VINDO AO LITERALURA =====================");
        System.out.printf("%-5s | %-50s%n", "Opção", "Descrição");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-5s | %-50s%n", "1", "Buscar livro por título");
        System.out.printf("%-5s | %-50s%n", "2", "Listar livros registrados");
        System.out.printf("%-5s | %-50s%n", "3", "Listar autores registrados");
        System.out.printf("%-5s | %-50s%n", "4", "Listar autores por ano");
        System.out.printf("%-5s | %-50s%n", "5", "Listar livros por idioma");
        System.out.printf("%-5s | %-50s%n", "6", "Opções extras (opcional)");
        System.out.printf("%-5s | %-50s%n", "0", "Sair");
        System.out.println("==================================================================");
        System.out.print("# Escolha uma opção: ");
    }

    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        return valor;
    }

    private String readLine(String message) {
        System.out.print(message);
        String input = scanner.nextLine();
        while (input.isEmpty())
        {
            System.out.println("Digite um nome válido!");
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private String readYesOrNo(String message) {
        String input;
        do {
            System.out.print(message);
            input = scanner.nextLine().trim().toLowerCase();
            if (!input.equals("s") && !input.equals("n")) {
                System.out.println("Digite apenas 's' para sim ou 'n' para não.");
            }
        } while (!input.equals("s") && !input.equals("n"));
        return input;
    }
    public void saveBook(Book newBook) {
        Optional<Book> existingBook = bookRepository.findByTitle(newBook.getTitle());

        if (existingBook.isPresent()) {
            System.out.println("Livro já salvo: " + newBook.getTitle());
            return;
        }

        // Evita duplicar autores
        List<Author> processedAuthors = new ArrayList<>();
        for (Author author : newBook.getAuthors()) {
            Optional<Author> existingAuthor = authorRepository.findByName(author.getName());
            if (existingAuthor.isPresent()) {
                processedAuthors.add(existingAuthor.get());
            } else {
                processedAuthors.add(authorRepository.save(author));
            }
        }

        newBook.setAuthors(processedAuthors);
        bookRepository.save(newBook);
        System.out.println("Livro salvo com sucesso: " + newBook.getTitle());
    }


    @Transactional
    public void showBook()
    {
        bookRepository.findAll()
                .forEach(l -> System.out.println(l));
    }
    public void saveAuthor(Author author)
    {
        authorRepository.save(author);
    }
    public void showAuthors()
    {
        authorRepository.findAll()
                .forEach(l -> System.out.println(l));
    }

    private void buscarLivroPorTitulo() {

        String titulo = readLine("Digite o título do livro: ");

        try {
            List<BookDto> resultados = CatalogoService.findBook(titulo);

            // 🔍 Se nenhum resultado exato, tente uma busca ampliada
            if (resultados.isEmpty()) {
                System.out.println("\n⚠️  Nenhum livro encontrado.");
                // 👉 Nova tentativa com palavra-chave (primeira palavra do título)
                String palavraChave = titulo.split(" ")[0];
                System.out.println("\n🔁 Tentando com a palavra-chave: " + palavraChave);
                resultados = CatalogoService.findBook(palavraChave);

                if (resultados.isEmpty()) {
                    System.out.println("⚠️ Ainda assim, nenhum livro foi encontrado.");
                    return;
                }
            }

            // ✅ Agora temos resultados: buscar o título exato
            List<BookDto> livrosExatos = resultados.stream()
                    .filter(l -> l.title().equalsIgnoreCase(titulo))
                    .toList();

            if (!livrosExatos.isEmpty()) {

                System.out.println("\n📚 Resultados encontrados:\n");
                AtomicInteger count = new AtomicInteger(1);

                livrosExatos.forEach(livro -> {
                    booksBuscados.add(livro);
                    Book book = new Book(livro);
                    saveBook(book);
                    System.out.println(booksBuscados);
                    System.out.println("=".repeat(65));
                    System.out.printf("------------------------ LIVRO #%d -------------------------------%n", count.getAndIncrement());
                    System.out.println("📖 Título: " + livro.title());
                    System.out.println("✍️  Autor(es):");
                    livro.authors().forEach(autor -> {
                        String nome = autor.name();
                        String nascimento = autor.birthYear() > 0 ? String.valueOf(autor.birthYear()) : "?";
                        String falecimento = autor.deathYear() > 0 ? String.valueOf(autor.deathYear()) : "?";
                        System.out.println("   - " + nome + " (" + nascimento + " - " + falecimento + ")");
                        authorsBuscados.add(autor);
                        Author author = new Author(autor);
                        saveAuthor(author);
                    });
                    System.out.println("🌐 Idiomas: "  + livro.languages());
                    System.out.println("⬇️  Downloads: " + livro.downloadCount());
                    System.out.println("-".repeat(65));
                    System.out.println("=".repeat(65));
//                    booksBuscados.add(livro);
//                    Book book = new Book(livro);
//                    saveBook(book);
//                    System.out.println(booksBuscados);
                });
            } else {
                System.out.println("\n⚠️  Nenhum livro com esse título exato.");
                System.out.println("\n🔍 Sugestões semelhantes:");

                resultados.stream()
                        .sorted(Comparator.comparingInt(livro ->
                                StringUtils.distanciaLevenshtein(livro.title().toLowerCase(), titulo.toLowerCase())))
                        .map(BookDto::title)
                        .distinct()
                        .limit(5)
                        .forEach(sugestao -> System.out.println("   ➤ " + sugestao));
            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar o livro: " + e.getMessage());
            e.printStackTrace();
        }
    }

}