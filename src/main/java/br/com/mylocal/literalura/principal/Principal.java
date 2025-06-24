package br.com.mylocal.literalura.principal;

import br.com.mylocal.literalura.dto.BookDto;
import br.com.mylocal.literalura.dto.BookResultDto;
import br.com.mylocal.literalura.service.ApiService;
import br.com.mylocal.literalura.util.Constants;
import br.com.mylocal.literalura.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Principal {

    private final Scanner scanner = new Scanner(System.in);
    private final ObjectMapper mapper = new ObjectMapper();

    public void startSystem() {
        int opcao;

        do {
            menu();
            opcao = readInt();

            switch (opcao) {
                case 1 -> buscarLivroPorTitulo();
                case 2 -> System.out.println("📚 Em breve: Listar livros registrados.");
                case 3 -> System.out.println("👤 Em breve: Listar autores registrados.");
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
        return scanner.nextInt();
    }

    private void buscarLivroPorTitulo() {
        scanner.nextLine(); // limpar buffer
        System.out.print("🔍 Digite o título do livro: ");
        String titulo = scanner.nextLine().trim();

        try {
            String endpoint = Constants.buildSearch(titulo);
            String json = ApiService.fetchApiResponse(endpoint);
            BookResultDto resultado = mapper.readValue(json, BookResultDto.class);

            List<BookDto> resultados = resultado.results();

            // 🔍 Se nenhum resultado exato, tente uma busca ampliada
            if (resultados.isEmpty()) {
                System.out.println("\n⚠️  Nenhum livro encontrado.");
                // 👉 Nova tentativa com palavra-chave (primeira palavra do título)
                String palavraChave = titulo.split(" ")[0];
                System.out.println("\n🔁 Tentando com a palavra-chave: " + palavraChave);

                endpoint = Constants.buildSearch(palavraChave);
                json = ApiService.fetchApiResponse(endpoint);
                resultado = mapper.readValue(json, BookResultDto.class);
                resultados = resultado.results();

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
                    System.out.println("=".repeat(65));
                    System.out.printf("------------------------ LIVRO #%d -------------------------------%n", count.getAndIncrement());
                    System.out.println("📖 Título: " + livro.title());
                    System.out.println("✍️  Autor(es):");
                    livro.authors().forEach(autor -> {
                        String nome = autor.name();
                        String nascimento = autor.birthYear() > 0 ? String.valueOf(autor.birthYear()) : "?";
                        String falecimento = autor.deathYear() > 0 ? String.valueOf(autor.deathYear()) : "?";
                        System.out.println("   - " + nome + " (" + nascimento + " - " + falecimento + ")");
                    });
                    System.out.println("🌐 Idiomas: " + String.join(", ", livro.languages()));
                    System.out.println("⬇️  Downloads: " + livro.downloadCount());
                    System.out.println("-".repeat(65));
                    System.out.println("=".repeat(65));
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