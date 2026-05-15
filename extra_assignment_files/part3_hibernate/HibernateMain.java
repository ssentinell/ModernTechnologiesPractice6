package com.movies;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HibernateMain {

    private static SessionFactory buildSessionFactory() {
        return new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();
    }

    static void saveMovies(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();

            session.persist(new Movie("Матрица", "Фантастика", 1999));
            session.persist(new Movie("Начало", "Фантастика", 2010));
            session.persist(new Movie("Тёмный рыцарь", "Боевик", 2008));
            session.persist(new Movie("Паразиты", "Триллер", 2019));
            session.persist(new Movie("Интерстеллар", "Фантастика", 2014));

            tx.commit();
            System.out.println("Фильмы сохранены");
        }
    }

    static void updateMovieByHQL(SessionFactory sf, String title, int newYear) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();

            int updated = session.createMutationQuery(
                    "UPDATE Movie SET year = :year WHERE title = :title")
                .setParameter("year", newYear)
                .setParameter("title", title)
                .executeUpdate();

            tx.commit();
            System.out.println("Обновлено записей: " + updated);
        }
    }

    static void deleteById(SessionFactory sf, int id) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();

            Movie movie = session.get(Movie.class, id);
            if (movie != null) {
                session.remove(movie);
                System.out.println("Удалён: " + movie.getTitle());
            }

            tx.commit();
        }
    }

    static List<Movie> findByGenreHQL(SessionFactory sf, String genre) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Movie WHERE genre = :genre ORDER BY year", Movie.class)
                .setParameter("genre", genre)
                .list();
        }
    }

    static List<Movie> findByYearRange(SessionFactory sf, int fromYear, int toYear) {
        try (Session session = sf.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
            Root<Movie> root = cq.from(Movie.class);

            cq.select(root)
                .where(cb.between(root.get("year"), fromYear, toYear))
                .orderBy(cb.asc(root.get("year")));

            return session.createQuery(cq).list();
        }
    }

    static List<Movie> findByTitleLike(SessionFactory sf, String titlePart) {
        try (Session session = sf.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
            Root<Movie> root = cq.from(Movie.class);

            cq.select(root)
                .where(cb.like(cb.lower(root.get("title")), "%" + titlePart.toLowerCase() + "%"));

            return session.createQuery(cq).list();
        }
    }

    static List<Movie> findPage(SessionFactory sf, int pageNumber, int pageSize) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Movie ORDER BY id", Movie.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
        }
    }

    static void runAggregationQueries(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            System.out.println("=== Количество фильмов по жанрам ===");
            List<Object[]> byGenre = session.createQuery(
                "SELECT genre, COUNT(*) FROM Movie GROUP BY genre", Object[].class).list();
            byGenre.forEach(row -> System.out.println(row[0] + ": " + row[1]));

            System.out.println("\n=== Средний год выхода ===");
            Double avgYear = session.createQuery(
                "SELECT AVG(year) FROM Movie", Double.class).uniqueResult();
            System.out.printf("Средний год: %.1f%n", avgYear);

            System.out.println("\n=== Новейший фильм каждого жанра ===");
            List<Object[]> newestByGenre = session.createQuery(
                "SELECT genre, MAX(year) FROM Movie GROUP BY genre", Object[].class).list();
            newestByGenre.forEach(row -> System.out.println(row[0] + ": " + row[1]));
        }
    }

    public static void main(String[] args) {
        try (SessionFactory sf = buildSessionFactory()) {
            saveMovies(sf);

            System.out.println("\n=== Обновление через HQL ===");
            updateMovieByHQL(sf, "Матрица", 1998);

            System.out.println("\n=== Удаление по id ===");
            deleteById(sf, 3);

            System.out.println("\n=== Поиск фантастики (HQL) ===");
            findByGenreHQL(sf, "Фантастика").forEach(System.out::println);

            System.out.println("\n=== Фильмы 2000-2015 (Criteria API) ===");
            findByYearRange(sf, 2000, 2015).forEach(System.out::println);

            System.out.println("\n=== Поиск 'тёмн' (Criteria API) ===");
            findByTitleLike(sf, "тёмн").forEach(System.out::println);

            System.out.println("\n=== Пагинация: страница 1 ===");
            findPage(sf, 1, 3).forEach(System.out::println);

            System.out.println("\n=== Пагинация: страница 2 ===");
            findPage(sf, 2, 3).forEach(System.out::println);

            System.out.println();
            runAggregationQueries(sf);
        }
    }
}
