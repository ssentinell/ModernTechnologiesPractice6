package com.movies.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MovieDAO {
    void createTable() throws SQLException;
    void dropTable() throws SQLException;
    Movie insert(Movie movie) throws SQLException;
    boolean delete(int id) throws SQLException;
    boolean updateTitle(int id, String newTitle) throws SQLException;
    Optional<Movie> findById(int id) throws SQLException;
    List<Movie> findAll() throws SQLException;
    List<Movie> findByTitle(String part) throws SQLException;
    List<Movie> findByGenre(String genre) throws SQLException;
    List<Movie> findByYear(int year) throws SQLException;
}
