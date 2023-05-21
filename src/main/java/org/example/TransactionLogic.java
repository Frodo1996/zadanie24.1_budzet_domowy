package org.example;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class TransactionLogic {

    private final HomeBudgetDao budgetDao = new HomeBudgetDao();

    public void add() {
        Scanner scanner = new Scanner(System.in);
        TransactionType type;
        do {
            System.out.println("Podaj typ transakcji (Expense - wydatek, Income - przychód):");
            type = TransactionType.valueOf(scanner.nextLine().toUpperCase());
        } while (!(type.name().equalsIgnoreCase("EXPENSE") ||
                type.name().equalsIgnoreCase("INCOME")));
        System.out.println("Podaj opis:");
        String description = scanner.nextLine();
        System.out.println("Podaj wartość:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        Transaction transaction = new Transaction(type, description, amount, LocalDate.now());
        HomeBudgetDao budgetDao = new HomeBudgetDao();
        budgetDao.insert(transaction);
    }

    public void delete() {
        try {
            Scanner scanner = new Scanner(System.in);
            budgetDao.findAllTransactions();
            System.out.println("Wprowadcenź ID transakcji, którą chciałbyś usunąć:");
            int userInput = scanner.nextInt();
            scanner.nextLine();
            budgetDao.delete(userInput);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        try {
            Scanner scanner = new Scanner(System.in);
            budgetDao.findAllTransactions();
            System.out.println("> Wpisz ID transakcji, którą chciałbyś zmodyfikować:");
            int userInput = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Wprowadź nowy opis:");
            String description = scanner.nextLine();
            System.out.println("Podaj kwotę:");
            double amount = scanner.nextDouble();
            budgetDao.update(description, amount, userInput);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}