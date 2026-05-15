package part3.p3_1;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * CRUD операции с фильмами через Hibernate ORM.
 * Учебное задание: Hibernate - работа с сущностями
 */

// ==================== Сущность Movie (уже реализована) ====================
@Entity
@Table(name = "movies")
class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "genre")
    private String genre;
    
    @Column(name = "year")
    private Integer year;
    
    // Конструкторы
    public Movie() {}
    
    public Movie(String title, String genre, Integer year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
    }
    
    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    @Override
    public String toString() {
        return String.format("Movie{id=%d, title='%s', genre='%s', year=%d}",
            id, title, genre, year);
    }
}

// ==================== Основной класс ====================
public class MovieHibernate {
    
    private SessionFactory sessionFactory;
    
    public MovieHibernate() {
        // Конфигурация Hibernate (programmatic, без XML)
        this.sessionFactory = new Configuration()
            .addAnnotatedClass(Movie.class)
            .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
            .setProperty("hibernate.connection.url", "jdbc:h2:mem:moviedb_hibernate")
            .setProperty("hibernate.connection.username", "sa")
            .setProperty("hibernate.connection.password", "")
            .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
            .setProperty("hibernate.hbm2ddl.auto", "create-drop")
            .setProperty("hibernate.show_sql", "true")
            .setProperty("hibernate.format_sql", "true")
            .buildSessionFactory();
    }
    
    public static void main(String[] args) {
        MovieHibernate app = new MovieHibernate();
        
        try {
            // Создаем сессию
            Session session = app.sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            
            System.out.println("=== Сохранение фильмов ===");
            
            // TODO: Сохраните 4 фильма используя app.saveMovie()
            // Фильмы:
            // 1. "Матрица", "sci-fi", 1999
            // 2. "Начало", "sci-fi", 2010
            // 3. "Крестный отец", "crime", 1972
            // 4. "Темный рыцарь", "action", 2008
            
            // ▼ ВАШ КОД ЗДЕСЬ ▼
            
            // ▲ КОНЕЦ ВАШЕГО КОДА ▲
            
            tx.commit();
            System.out.println(" Фильмы сохранены\n");
            
            // TODO: Найдите все sci-fi фильмы используя app.findMoviesByGenre()
            System.out.println("=== Поиск по жанру 'sci-fi' ===");
            // ▼ ВАШ КОД ЗДЕСЬ ▼
            
            // ▲ КОНЕЦ ВАШЕГО КОДА ▲
            
            // Демонстрация других операций
            System.out.println("\n=== Все фильмы ===");
            List<Movie> allMovies = session.createQuery("FROM Movie", Movie.class).list();
            allMovies.forEach(System.out::println);
            
            // Обновление
            System.out.println("\n=== Обновление фильма ===");
            tx = session.beginTransaction();
            if (!allMovies.isEmpty()) {
                Movie first = allMovies.get(0);
                app.updateMovie(session, first.getId(), "Матрица (Обновлено)", "sci-fi", 1999);
            }
            tx.commit();
            
            // Удаление
            System.out.println("\n=== Удаление фильма ===");
            tx = session.beginTransaction();
            if (allMovies.size() > 1) {
                app.deleteMovie(session, allMovies.get(1).getId());
            }
            tx.commit();
            
            // Итоговый список
            System.out.println("\n=== Итоговый список фильмов ===");
            List<Movie> finalMovies = session.createQuery("FROM Movie", Movie.class).list();
            finalMovies.forEach(System.out::println);
            
            session.close();
            
        } finally {
            app.sessionFactory.close();
        }
    }
    
    /**
     * TODO: Реализуйте сохранение фильма в базу данных
     * Используйте session.persist(movie) для сохранения
     * Не забудьте, что транзакция уже начата в main()
     */
    public void saveMovie(Session session, Movie movie) {
        // ▼ ВАШ КОД ЗДЕСЬ ▼
        
        // ▲ КОНЕЦ ВАШЕГО КОДА ▲
    }
    
    /**
     * TODO: Реализуйте поиск фильмов по жанру с использованием HQL
     * HQL запрос: "FROM Movie WHERE genre = :genre"
     * Используйте session.createQuery() и setParameter()
     * Верните List<Movie>
     */
    public List<Movie> findMoviesByGenre(Session session, String genre) {
        // ▼ ВАШ КОД ЗДЕСЬ ▼
        return null; // замените эту строку
        // ▲ КОНЕЦ ВАШЕГО КОДА ▲
    }
    
    // ==================== Методы уже реализованы ниже ====================
    
    public void updateMovie(Session session, Long id, String title, String genre, Integer year) {
        Movie movie = session.get(Movie.class, id);
        if (movie != null) {
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setYear(year);
            session.merge(movie);
            System.out.println("Обновлен фильм: " + movie.getTitle());
        }
    }
    
    public void deleteMovie(Session session, Long id) {
        Movie movie = session.get(Movie.class, id);
        if (movie != null) {
            session.remove(movie);
            System.out.println("Удален фильм: " + movie.getTitle());
        }
    }
}
