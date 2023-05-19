package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class HomeBudgetDao {
    private final int EXIT = 0;
    private final String ALL_TRANSACTIONS_SQL = "SELECT * FROM transactions";
    private Connection connection;
    private final Scanner scanner = new Scanner(System.in);

    public HomeBudgetDao() {
        try {
            String url = "jdbc:mysql://localhost:3306/home_budget?serverTimezone=UTC";
            this.connection = DriverManager.getConnection(url, "root", "admin");
        } catch (SQLException e) {
            System.out.println("Nie udało się połączyć z serwerem " + e.getMessage());
        }
    }

    public void mainLoop() {
        System.out.println("~~~Witamy w aplikacji do zarządzania budżetem domowym!~~~");
        int option;
        do {
            displayOptions();
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1 -> addTransaction();
                case 2 -> modifyTransaction();
                case 3 -> deleteTransaction();
                case 4 -> displayTransactionByType(TransactionType.P);
                case 5 -> displayTransactionByType(TransactionType.W);
                case EXIT -> System.out.println("Dziękujemy za skorzystanie z naszej aplikacji, do zobaczenia!");
            }
        } while (option != EXIT);
    }

    private void addTransaction() {
        TransactionType type;
        do {
            System.out.println("Podaj typ transakcji (W - wydatek, P - przychód):");
            type = TransactionType.valueOf(scanner.nextLine().toUpperCase());
        } while (!(type.name().equalsIgnoreCase("W") || type.name().equalsIgnoreCase("P")));
        System.out.println("Podaj opis:");
        String description = scanner.nextLine();
        System.out.println("Podaj wartość:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        LocalDate date = LocalDate.now();
        String sql = "INSERT INTO transactions (type, description, amount, date) VALUES (?, ?, ?, ?)";
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
        try {
            printTransactions(ALL_TRANSACTIONS_SQL);
            updateTransaction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteTransaction() {
        try {
            printTransactions(ALL_TRANSACTIONS_SQL);
            System.out.println("Wprowadcenź ID transakcji, którą chciałbyś usunąć:");
            int userInput = scanner.nextInt();
            scanner.nextLine();
            String sql = "DELETE FROM transactions WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userInput);
            boolean execute = preparedStatement.execute();
            if (!execute) {
                System.out.println("Transakcja usunięta! Poniżej zaktualizowana lista dostępnych transakcji:\n");
                printTransactions(ALL_TRANSACTIONS_SQL);
            } else {
                System.out.println("Nie udało się usunąć transakcji, poniżej lista dostępnych transakcji:\n");
                printTransactions(ALL_TRANSACTIONS_SQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayTransactionByType(TransactionType type) {
        try {
            String sql = "SELECT * FROM transactions WHERE type = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type.getDescription());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("description");
                double amount = resultSet.getDouble("amount");
                Date date = resultSet.getDate("date");
                System.out.println("Opis: " + description + ", cena: " + amount + ", data utworzenia transakcji: " + date);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private void printTransactions(String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String description = resultSet.getString("description");
            System.out.println("ID " + id + " -> " + description);
        }
    }

    private void updateTransaction() throws SQLException {
        System.out.println("> Wpisz ID transakcji, którą chciałbyś zmodyfikować:");
        int userInput = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Wprowadź nowy opis:");
        String description = scanner.nextLine();
        System.out.println("Podaj kwotę:");
        double amount = scanner.nextDouble();
        String updateTransaction = "UPDATE transactions SET description = ?, amount = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateTransaction);
        preparedStatement.setString(1, description);
        preparedStatement.setDouble(2, amount);
        preparedStatement.setInt(3, userInput);
        int update = preparedStatement.executeUpdate();
        if (update == 0) {
            System.out.println("Nie udało się zmodyfikować transkacji, spróbuj ponownie.");
        } else {
            System.out.println("Transakcja została zmodyfikowana!");
        }
    }
}
