package org.example;

import java.sql.*;
import java.util.Objects;

public class HomeBudgetDao {

    private final Connection connection = connect();

    public void insert(Transaction transaction) {

        String sql = "INSERT INTO transactions (type, description, amount, date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(sql);
            preparedStatement.setString(1, transaction.getType().getDescription());
            preparedStatement.setString(2, transaction.getDescription());
            preparedStatement.setDouble(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getDate().toString());
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

    public void update(String description, double amount, int userInput) throws SQLException {
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

    public void delete(int userInput) {
        try {
            String sql = "DELETE FROM transactions WHERE id = ?";
            PreparedStatement preparedStatement = Objects.requireNonNull(connect()).prepareStatement(sql);
            preparedStatement.setInt(1, userInput);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayTransactionByType(TransactionType type) {
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

    public void exit() {
        System.out.println("Dziękujemy za skorzystanie z naszego programu! Do zobaczenia :)");
        closeConnection(Objects.requireNonNull(connection));
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:mysql://localhost:3306/home_budget?serverTimezone=UTC&characterEncoding=utf8";
        try {
            return DriverManager.getConnection(url, "root", "admin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void findAllTransactions() throws SQLException {
        PreparedStatement preparedStatement = Objects.requireNonNull(connect()).prepareStatement("SELECT * FROM transactions");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String description = resultSet.getString("description");
            System.out.println("ID " + id + " -> " + description);
        }
    }
}
