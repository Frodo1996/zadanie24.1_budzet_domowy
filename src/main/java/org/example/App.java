package org.example;

import java.util.Scanner;

public class App {
    private final static int EXIT = 0;

    public static void main(String[] args) {
        App.mainLoop();
    }

    private static void mainLoop() {
        TransactionLogic transactionLogic = new TransactionLogic();
        HomeBudgetDao budgetDao = new HomeBudgetDao();
        System.out.println("~~~Witamy w aplikacji do zarządzania budżetem domowym!~~~");
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            displayOptions();
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1 -> transactionLogic.add();
                case 2 -> transactionLogic.update();
                case 3 -> transactionLogic.delete();
                case 4 -> budgetDao.displayTransactionByType(TransactionType.INCOME);
                case 5 -> budgetDao.displayTransactionByType(TransactionType.EXPENSE);
                case EXIT -> budgetDao.exit();
                default -> System.out.println("Nie ma takiej opcji, spróbuj ponownie.");
            }
        } while (option != EXIT);
    }

    private static void displayOptions() {
        System.out.println("""
                Wybierz co chciałbyś zrobić:
                > 1. Dodaj nową transakcję
                > 2. Zmodyfikuj istniejące już transakcję
                > 3. Usuń transakcję
                > 4. Wyświetl wszystkie transakcje z przychodami
                > 5. Wyświetl wszystkie transakcje z wydatkami
                > 0. Wyjście z programu""");
    }
}