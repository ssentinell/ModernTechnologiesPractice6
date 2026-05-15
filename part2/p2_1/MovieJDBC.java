package part2.p2_1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD операции с фильмами через JDBC.
 * Учебное задание: JDBC - работа с базой данных
 */
public class MovieJDBC {
    
    // URL для встроенной базы H2
    private static final String DB_URL = "jdbc:h2:mem:moviedb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    public static void main(String[] args) {
        MovieJDBC movieDb = new MovieJDBC();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("✅ Подключение к базе данных установлено!\n");
            
            // Создаем таблицу
            movieDb.dropAndCreateTable(conn);
            
            // TODO: Вызовите insertMovies для добавления фильмов
            // ▼ ВАШ КОД ЗДЕСЬ ▼
            
            // ▲ КОНЕЦ ВАШЕГО КОДА ▲
            
            System.out.println("=== Все фильмы ===");
            movieDb.printAllMovies(conn);
            
            // TODO: Вызовите findByTitle для поиска фильма "Матрица"
            System.out.println("\n=== Поиск по названию 'Матрица' ===");
            // ▼ ВАШ КОД ЗДЕСЬ ▼
            
            // ▲ КОНЕЦ ВАШЕГО КОДА ▲
            
            // Демонстрация других операций
            System.out.println("\n=== Поиск по году (1999) ===");
            movieDb.findByYear(conn, 1999);
            
            System.out.println("\n=== Поиск по жанру (sci-fi) ===");
            movieDb.findByGenre(conn, "sci-fi");
            
            // Обновление фильма
            System.out.println("\n=== Обновление фильма ===");
            movieDb.updateMovie(conn, 1, "Матрица (Обновлено)", "sci-fi", 1999);
            movieDb.printAllMovies(conn);
            
            // Удаление фильма
            System.out.println("\n=== Удаление фильма ===");
            movieDb.deleteMovie(conn, 2);
            movieDb.printAllMovies(conn);
            
        } catch (SQLException e) {
            System.err.println("❌ Ошибка базы данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * TODO: Реализуйте вставку 4 фильмов в базу данных
     * Используйте PreparedStatement для безопасной вставки
     * Фильмы для вставки:
     * 1. "Матрица", "sci-fi", 1999
     * 2. "Начало", "sci-fi", 2010
     * 3. "Крестный отец", "crime", 1972
     * 4. "Темный рыцарь", "action", 2008
     */
    public void insertMovies(Connection conn) throws SQLException {
        // ▼ ВАШ КОД ЗДЕСЬ ▼
        // Подсказка: используйте conn.prepareStatement()
        // INSERT INTO movies (title, genre, year) VALUES (?, ?, ?)
        
        // ▲ КОНЕЦ ВАШЕГО КОДА ▲
    }
    
    /**
     * TODO: Реализуйте поиск фильмов по названию с использованием LIKE
     * Метод должен принимать подстроку для поиска и выводить найденные фильмы
     * Используйте PreparedStatement с LIKE ? (например: "%" + title + "%")
     */
    public void findByTitle(Connection conn, String title) throws SQLException {
        // ▼ ВАШ КОД ЗДЕСЬ ▼
        // Подсказка: SELECT * FROM movies WHERE title LIKE ?
        // Параметр: "%" + title + "%"
        
        // ▲ КОНЕЦ ВАШЕГО КОДА ▲
    }
    
    // ==================== Методы уже реализованы ниже ====================
    
    public void dropAndCreateTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Удаляем таблицу если существует
            stmt.execute("DROP TABLE IF EXISTS movies");
            
            // Создаем таблицу
            stmt.execute("""
                CREATE TABLE movies (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    genre VARCHAR(100),
                    year INT
                )
                """);
            System.out.println("✅ Таблица movies создана\n");
        }
    }
    
    public void printAllMovies(Connection conn) throws SQLException {
        String sql = "SELECT * FROM movies";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.printf("ID: %d, Название: %s, Жанр: %s, Год: %d%n",
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getInt("year"));
            }
        }
    }
    
    public void updateMovie(Connection conn, int id, String title, String genre, int year) throws SQLException {
        String sql = "UPDATE movies SET title = ?, genre = ?, year = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setInt(3, year);
            pstmt.setInt(4, id);
            
            int rows = pstmt.executeUpdate();
            System.out.println("Обновлено записей: " + rows);
        }
    }
    
    public void deleteMovie(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int rows = pstmt.executeUpdate();
            System.out.println("Удалено записей: " + rows);
        }
    }
    
    public void findByYear(Connection conn, int year) throws SQLException {
        String sql = "SELECT * FROM movies WHERE year = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("ID: %d, Название: %s, Жанр: %s, Год: %d%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("year"));
                }
            }
        }
    }
    
    public void findByGenre(Connection conn, String genre) throws SQLException {
        String sql = "SELECT * FROM movies WHERE genre = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genre);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("ID: %d, Название: %s, Жанр: %s, Год: %d%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("year"));
                }
            }
        }
    }
}
