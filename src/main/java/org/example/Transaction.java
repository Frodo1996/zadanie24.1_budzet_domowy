package org.example;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Transaction {
    private int id;
    private TransactionType type;
    private String description;
    private double amount;
    private LocalDate date;

    public Transaction(int id, TransactionType type, String description, double amount, LocalDate date) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public Transaction(TransactionType type, String description, double amount, LocalDate date) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}