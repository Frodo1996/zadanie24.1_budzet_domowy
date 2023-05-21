package org.example;

public enum TransactionType {
    EXPENSE("Wydatek"),
    INCOME("Przychód");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}