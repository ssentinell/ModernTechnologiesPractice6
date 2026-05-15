package com.movies.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAOTest {
    private static final String URL = "jdbc:h2:mem:movietest;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void main(String[] args) throws Exception {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            MovieDAO dao = new MovieDAOImpl(conn);

            dao.dropTable();
            dao.createTable();

            dao.insert(new Movie("Матрица", "Фантастика", 1999));
            dao.insert(new Movie("Начало", "Фантастика", 2010));
            dao.insert(new Movie("Лев", "Драма", 2016));
            dao.insert(new Movie("Джокер", "Триллер", 2019));

            dao.updateTitle(1, "Матрица: Перезагрузка");
            dao.delete(2);

            System.out.println("=== Все фильмы ===");
            dao.findAll().forEach(System.out::println);

            System.out.println("\n=== Поиск по id=1 ===");
            System.out.println(dao.findById(1));

            System.out.println("\n=== Поиск по жанру 'Фантастика' ===");
            dao.findByGenre("Фантастика").forEach(System.out::println);

            System.out.println("\n=== Поиск по году 2019 ===");
            dao.findByYear(2019).forEach(System.out::println);

            System.out.println("\n=== Поиск по части названия 'матр' ===");
            dao.findByTitle("матр").forEach(System.out::println);
        }
    }
}
