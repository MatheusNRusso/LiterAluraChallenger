package br.com.mylocal.literalura.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Component
public class MenuService {

    private final Scanner scanner;

    public MenuService() {
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        System.out.println("\n=================== BEM VINDO AO LITERALURA =====================");
        System.out.printf("%-5s | %-50s%n", "Opção", "Descrição");
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("%-5s | %-50s%n", "1", "Buscar livro por título");
        System.out.printf("%-5s | %-50s%n", "2", "Listar livros registrados");
        System.out.printf("%-5s | %-50s%n", "3", "Listar autores registrados");
        System.out.printf("%-5s | %-50s%n", "4", "Autores vivos em determinado intervalo: ");
        System.out.printf("%-5s | %-50s%n", "5", "Listar livros por idioma");
        System.out.printf("%-5s | %-50s%n", "6", "Opções extras (opcional)");
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




}
