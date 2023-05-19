package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class HomeBudgetDao {
    private final int EXIT = 0;
    private Connection connection;
    private Scanner scanner = new Scanner(System.in);

    public HomeBudgetDao() {
        try {
            String url = "jdbc:mysql://localhost:3306/home_budget?serverTimezone=UTC";
            this.connection = DriverManager.getConnection(url, "root", "admin");
        } catch (SQLException e) {
            System.out.println("Nie udało się połączyć z serwerem " + e.getMessage());
        }
    }

    public void mainLoop() {
        System.out.println("~Witamy w aplikacji do zarządzania budżetem domowym!~");
        int option;
        do {
            displayOptions();
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1 -> addTransaction();
                case 2 -> modifyTransaction();
                case 3 -> deleteTransaction();
                case 4 -> displayTransactionsWithRevenue();
                case 5 -> displayExpenseTransactions();
                case EXIT -> System.out.println("Dziękujemy za skorzystanie z naszej aplikacji, do zobaczenia!");
            }
        } while (option != EXIT);
    }

    private void addTransaction() {
        TransactionType type;
        do {
            System.out.println("Podaj typ transakcji (W - wydatek, P - przychód):");
            type = TransactionType.valueOf(scanner.nextLine().toUpperCase());
        } while (!type.name().equalsIgnoreCase("W") || type.name().equalsIgnoreCase("P"));
        System.out.println("Podaj opis:");
        String description = scanner.nextLine();
        System.out.println("Podaj wartość:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        LocalDate date = LocalDate.now();
        String sql = "INSERT INTO transaction (type, description, amount, date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type.getDescription());
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, date.toString());
            boolean execute = preparedStatement.execute();
            if (!execute) {
                System.out.println("Udało się dodać nową transakcję!");
            } else {
                System.out.println("Coś poszło nie tak :( Spróbuj ponownie!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifyTransaction() {

    }

    private void deleteTransaction() {
    }

    private void displayTransactionsWithRevenue() {

    }

    private void displayExpenseTransactions() {

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
