package br.com.mylocal.literalura.service;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MenuService {

    private final Scanner scanner;
    private final ExportService exportService;
    private final EstatisticaService estatisticaService;

    public MenuService(ExportService exportService, EstatisticaService estatisticaService) {
        this.exportService = exportService;
        this.scanner = new Scanner(System.in);
        this.estatisticaService = estatisticaService;
    }

    public void menu() {
        System.out.println("\n=================== BEM VINDO AO LITERALURA =====================");
        System.out.printf("%-5s | %-50s%n", "Opção", "Descrição");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-5s | %-50s%n", "1", "Buscar livro por título");
        System.out.printf("%-5s | %-50s%n", "2", "Listar livros registrados");
        System.out.printf("%-5s | %-50s%n", "3", "Listar autores registrados");
        System.out.printf("%-5s | %-50s%n", "4", "Listar autores em determinado intervalo");
        System.out.printf("%-5s | %-50s%n", "5", "Listar livros por idioma");
        System.out.printf("%-5s | %-50s%n", "6", "Listar Top 5 livros");
        System.out.printf("%-5s | %-50s%n", "7", "Listar autores por nome");
        System.out.printf("%-5s | %-50s%n", "8", "Exportar dados (livros/autores em JSON ou CSV)");
        System.out.printf("%-5s | %-50s%n", "9", "Exibir as estatisticas disponíveis");
        System.out.printf("%-5s | %-50s%n", "0", "Sair");
        System.out.println("==================================================================");
        System.out.print("# Escolha uma opção: ");
    }

    public int readInt() {
        int valor;
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                valor = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido: ");
            }
        }
        return valor;
    }


    public String readLine(String message) {
        String input;
        do {
            System.out.print(message);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Digite um nome válido!");
            }
        } while (input.isEmpty());
        return input;
    }

    public String readYesOrNo(String message) {
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


    public void exportarDados() {
        System.out.println("Você deseja exportar:");
        System.out.println("1 - Livros");
        System.out.println("2 - Autores");
        System.out.print("Escolha uma opção: ");
        int tipo = readInt();

        System.out.println("Escolha o formato de exportação:");
        System.out.println("1 - JSON");
        System.out.println("2 - CSV");
        System.out.print("Formato: ");
        int formato = readInt();

        if ((tipo != 1 && tipo != 2) || (formato != 1 && formato != 2)) {
            System.out.println("❌ Opção inválida. Exportação cancelada.");
            return;
        }

        String tipoTexto = (tipo == 1) ? "livros" : "autores";
        String formatoTexto = (formato == 1) ? "json" : "csv";

        System.out.printf("📤 Exportando %s em formato %s...\n", tipoTexto, formatoTexto);

        if (tipo == 1) {
            exportService.exportarLivros(formatoTexto);
        } else {
            exportService.exportarAutores(formatoTexto);
        }

        System.out.println("✅ Exportação concluída com sucesso!");
    }

    public void exibirEstatisticas() {
        while (true) {
            System.out.println("\n📊 Estatísticas disponíveis:");
            System.out.println("0 - Digite zero para sair");
            System.out.println("1 - Total de livros cadastrados");
            System.out.println("2 - Total de autores cadastrados");
            System.out.println("3 - Média de downloads por idioma");
            System.out.println("4 - Idioma com mais livros");
            System.out.println("5 - Livro mais baixado por idioma");
            System.out.println("6 - Autor com mais livros");
            System.out.println("7 - Intervalo de vida média dos autores");
            System.out.print("Escolha uma opção: ");

            int opcao = readInt();
            switch (opcao) {
                case 0 -> {
                    return;
                }
                case 1 -> estatisticaService.totalLivros();
                case 2 -> estatisticaService.totalAutores();
                case 3 -> estatisticaService.mediaDownloadsPorIdioma();
                case 4 -> estatisticaService.idiomaMaisFrequente();
                case 5 -> estatisticaService.livroMaisBaixadoPorIdioma();
                case 6 -> estatisticaService.autorComMaisLivros();
                case 7 -> estatisticaService.intervaloVidaMediaAutores();
                default -> System.out.println("❌ Opção inválida.");
            }
            String repete = readYesOrNo("Deseja visualizar outra estatística? (s/n) ");
            if (!repete.equals("s")) {
                break;
            }
        }
    }
}
