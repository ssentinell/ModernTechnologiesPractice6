package com.movies.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAOImpl implements MovieDAO {

    private final Connection conn;

    public MovieDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createTable() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE movies (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(200) NOT NULL,
                    genre VARCHAR(100),
                    year INT
                )
                """);
        }
    }

    @Override
    public void dropTable() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS movies");
        }
    }

    @Override
    public Movie insert(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, genre, year) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getGenre());
            pstmt.setInt(3, movie.getYear());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    movie.setId(keys.getInt(1));
                }
            }
            return movie;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateTitle(int id, String newTitle) throws SQLException {
        String sql = "UPDATE movies SET title = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newTitle);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Movie> findById(int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Movie> findAll() throws SQLException {
        String sql = "SELECT * FROM movies ORDER BY id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            return mapRows(rs);
        }
    }

    @Override
    public List<Movie> findByTitle(String part) throws SQLException {
        String sql = "SELECT * FROM movies WHERE LOWER(title) LIKE LOWER(?) ORDER BY id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + part + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                return mapRows(rs);
            }
        }
    }

    @Override
    public List<Movie> findByGenre(String genre) throws SQLException {
        String sql = "SELECT * FROM movies WHERE LOWER(genre) = LOWER(?) ORDER BY year";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genre);
            try (ResultSet rs = pstmt.executeQuery()) {
                return mapRows(rs);
            }
        }
    }

    @Override
    public List<Movie> findByYear(int year) throws SQLException {
        String sql = "SELECT * FROM movies WHERE year = ? ORDER BY id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            try (ResultSet rs = pstmt.executeQuery()) {
                return mapRows(rs);
            }
        }
    }

    private Movie mapRow(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getInt("id"));
        movie.setTitle(rs.getString("title"));
        movie.setGenre(rs.getString("genre"));
        movie.setYear(rs.getInt("year"));
        return movie;
    }

    private List<Movie> mapRows(ResultSet rs) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        while (rs.next()) {
            movies.add(mapRow(rs));
        }
        return movies;
    }
}
