package org.example;

public enum TransactionType {
    W("Wydatek"),
    P("Przychód");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
