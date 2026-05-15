package com.movies.tx;

import java.math.BigDecimal;
import java.sql.*;

public class TransactionJDBC {

    private static final String URL = "jdbc:h2:mem:bankdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void main(String[] args) throws Exception {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            createTable(conn);
            seed(conn);

            System.out.println("=== До перевода ===");
            printAccounts(conn);

            System.out.println("\n=== Корректный перевод 150.00 ===");
            transfer(conn, 1, 2, 150.00);
            printAccounts(conn);

            System.out.println("\n=== Неуспешный перевод 5000.00 ===");
            transfer(conn, 1, 2, 5000.00);
            printAccounts(conn);
        }
    }

    static void createTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS accounts");
            stmt.execute("""
                CREATE TABLE accounts (
                    id INT PRIMARY KEY,
                    owner VARCHAR(100),
                    balance DECIMAL(10,2)
                )
                """);
        }
    }

    static void seed(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO accounts (id, owner, balance) VALUES (?, ?, ?)")) {
            Object[][] data = {
                {1, "Alice", new BigDecimal("1000.00")},
                {2, "Bob", new BigDecimal("500.00")},
                {3, "Charlie", new BigDecimal("700.00")}
            };
            for (Object[] row : data) {
                ps.setInt(1, (Integer) row[0]);
                ps.setString(2, (String) row[1]);
                ps.setBigDecimal(3, (BigDecimal) row[2]);
                ps.executeUpdate();
            }
        }
    }

    static void transfer(Connection conn, int fromId, int toId, double amount) throws SQLException {
        boolean oldAutoCommit = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);

            BigDecimal money = BigDecimal.valueOf(amount);
            BigDecimal fromBalance = getBalance(conn, fromId);

            if (fromBalance == null) {
                throw new SQLException("Счет отправителя не найден");
            }
            if (getBalance(conn, toId) == null) {
                throw new SQLException("Счет получателя не найден");
            }
            if (fromBalance.compareTo(money) < 0) {
                throw new SQLException("Недостаточно средств");
            }

            try (PreparedStatement withdraw = conn.prepareStatement(
                     "UPDATE accounts SET balance = balance - ? WHERE id = ?");
                 PreparedStatement deposit = conn.prepareStatement(
                     "UPDATE accounts SET balance = balance + ? WHERE id = ?")) {

                withdraw.setBigDecimal(1, money);
                withdraw.setInt(2, fromId);
                withdraw.executeUpdate();

                deposit.setBigDecimal(1, money);
                deposit.setInt(2, toId);
                deposit.executeUpdate();
            }

            conn.commit();
            System.out.println("Перевод выполнен успешно");
        } catch (Exception e) {
            conn.rollback();
            System.out.println("Перевод отменён: " + e.getMessage());
        } finally {
            conn.setAutoCommit(oldAutoCommit);
        }
    }

    static BigDecimal getBalance(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
            "SELECT balance FROM accounts WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("balance");
                }
                return null;
            }
        }
    }

    static void printAccounts(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM accounts ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf("id=%d | %s | %s%n",
                    rs.getInt("id"),
                    rs.getString("owner"),
                    rs.getBigDecimal("balance"));
            }
        }
    }
}
