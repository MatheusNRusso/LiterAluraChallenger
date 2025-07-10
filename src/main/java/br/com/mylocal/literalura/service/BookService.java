package br.com.mylocal.literalura.service;

import br.com.mylocal.literalura.dto.AuthorDto;
import br.com.mylocal.literalura.dto.BookDto;
import br.com.mylocal.literalura.model.Author;
import br.com.mylocal.literalura.model.Book;
import br.com.mylocal.literalura.model.Language;
import br.com.mylocal.literalura.repository.AuthorRepository;
import br.com.mylocal.literalura.repository.BookRepository;
import br.com.mylocal.literalura.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public Book saveIfNotExists(BookDto bookDto) {
        Optional<Book> existingBook = bookRepository.findByTitle(bookDto.title());
        if (existingBook.isPresent()) {
            return existingBook.get();
        }

        // 1. Converter e persistir autores, associando ao livro mais tarde
        List<Author> authors = bookDto.authors()
                .stream()
                .map(this::findCreateAuthor)
                .collect(Collectors.toList());

        // 2. Mapear idiomas
        List<Language> languages = bookDto.languages()
                .stream()
                .map(code -> Language.fromCode(code)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Language code: " + code)))
                .collect(Collectors.toList());

        // 3. Criar o livro, mas ainda sem autores vinculados
        Book book = new Book(bookDto.title(),
                bookDto.downloadCount(),
                bookDto.authors(),
                bookDto.languages());
        bookRepository.save(book);

        for (AuthorDto dto : bookDto.authors()) {
            Author author = findCreateAuthor(dto);
            if (author.getBook() == null) {
                author.setBook(book);
                authorRepository.save(author);
            }
        }
        return book;
    }

    private Author findCreateAuthor(AuthorDto authorDto) {
        return authorRepository.findByNameAndBirthYearAndDeathYear(
                authorDto.name(), authorDto.birthYear(), authorDto.deathYear())
                .orElse(new Author(authorDto));
    }

    public void printDetails(BookDto bookDto, int index) {
        System.out.println("=".repeat(65));
        System.out.printf("------------------------ LIVRO #%d -------------------------------%n", index);
        System.out.println("Title: " + bookDto.title());
        System.out.println("Authors: ");
        bookDto.authors().forEach(autor -> {
            String name = autor.name();
            String by = autor.birthYear() > 0 ? String.valueOf(autor.birthYear()) : "?";
            String dy = autor.deathYear() > 0 ? String.valueOf(autor.deathYear()) : "?";
            System.out.println("  -" + name + " (" + by + " - " + dy + ")");
        });
        System.out.println("Languages: " + bookDto.languages());
        System.out.println("Download count: " + bookDto.downloadCount());
        System.out.println("-".repeat(65));
        System.out.println("=".repeat(65));
    }

    public void printDetails(Book book, int index) {
        System.out.println("=".repeat(65));
        System.out.printf("------------------------ LIVRO #%d -------------------------------%n", index);
        System.out.println("📖 Título: " + book.getTitle());

        System.out.println("✍️  Autor(es):");
        Author author = book.getAuthors().stream().findFirst().orElse(null);
        if (author != null) {
            String name = author.getName();
            String birth = author.getBirthYear() > 0 ? String.valueOf(author.getBirthYear()) : "?";
            String death = author.getDeathYear() > 0 ? String.valueOf(author.getDeathYear()) : "?";
            System.out.println("   - " + name + " (" + birth + " - " + death + ")");
        }
        System.out.println("🌐 Idiomas: " + book.getLanguages());
        System.out.println("⬇️  Downloads: " + book.getDownloadCount());
        System.out.println("-".repeat(65));
        System.out.println("=".repeat(65));
    }

    public void findBookByTitle(String titulo, List<BookDto> booksBuscados, List<AuthorDto> authorsBuscados) {
        try {
            List<BookDto> resultados = CatalogoService.findBook(titulo);

            if (resultados.isEmpty()) {
                System.out.println("\n⚠️  Nenhum livro encontrado.");
                String palavraChave = titulo.split(" ")[0];
                System.out.println("\n🔁 Tentando com a palavra-chave: " + palavraChave);
                resultados = CatalogoService.findBook(palavraChave);

                if (resultados.isEmpty()) {
                    System.out.println("⚠️ Ainda assim, nenhum livro foi encontrado.");
                    return;
                }
            }

            List<BookDto> livrosExatos = resultados.stream()
                    .filter(l -> l.title().equalsIgnoreCase(titulo))
                    .toList();

            if (!livrosExatos.isEmpty()) {
                System.out.println("\n📚 Resultados encontrados:\n");
                AtomicInteger count = new AtomicInteger(1);

                livrosExatos.forEach(livro -> {
                    booksBuscados.add(livro);
                    saveIfNotExists(livro);
                    authorsBuscados.addAll(livro.authors());
                    printDetails(livro, count.getAndIncrement());
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

    public void showTitleLanguage(Language idioma) {
        List<Book> livros = bookRepository.findAll();

        List<Book> filtrados = livros.stream()
                .filter(b -> b.getLanguages().contains(idioma))
                .toList();

        if (filtrados.isEmpty()) {
            System.out.println("⚠️ Nenhum livro encontrado no idioma: " + idioma);
        } else {
            System.out.println("\n🌐 Livros no idioma " + idioma + ":\n");
            AtomicInteger index = new AtomicInteger(1);
            filtrados.forEach(book -> printDetails(book, index.getAndIncrement()));
        }
    }
}
