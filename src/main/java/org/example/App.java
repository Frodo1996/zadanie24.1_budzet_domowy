package org.example;

public class App {
    private final static HomeBudgetDao DAO = new HomeBudgetDao();
    public static void main(String[] args) {
        DAO.mainLoop();
    }
}