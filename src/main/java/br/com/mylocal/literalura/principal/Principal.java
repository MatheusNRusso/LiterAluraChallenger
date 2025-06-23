package br.com.mylocal.literalura.principal;

public class Principal {

    /**
     * Displays the main menu.
     * Exibe o menu principal.
     */
    public void menu() {
        System.out.println("\n====================== BEM VINDO AO LITERALURA =======================");
        System.out.printf("%-5s | %-50s%n", "Opção", "Descrição");
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-5s | %-50s%n", "1", "Buscar livro por título: ");
        System.out.printf("%-5s | %-50s%n", "2", "Listar livros registrados: ");
        System.out.printf("%-5s | %-50s%n", "3", "Listar autores registrados: ");
        System.out.printf("%-5s | %-50s%n", "4", "Listar autores registrados nem determinado ano: ");
        System.out.printf("%-5s | %-50s%n", "5", "Listar livros em determinado idioma: ");
        System.out.printf("%-5s | %-50s%n", "6", "Opções extras(opcionais): ");
        System.out.printf("%-5s | %-50s%n", "0", "Sair");
        System.out.println("=======================================================================");
        System.out.print("# Escolha uma opção: ");
    }

}
