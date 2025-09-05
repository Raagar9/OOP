package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost/OOPCP";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "aneeshd16";
    private Connection connection;

    public DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}