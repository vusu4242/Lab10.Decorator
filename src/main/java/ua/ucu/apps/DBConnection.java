package ua.ucu.apps;

import lombok.SneakyThrows;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;
    
    @SneakyThrows
    private DBConnection() {
        // Use in-memory database for testing
        this.connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        createTableIfNotExists();
    }
    
    @SneakyThrows
    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS documents (path TEXT PRIMARY KEY, parsed_content TEXT NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }
    
    @SneakyThrows
    public String getParsedByPath(String path) {
        String query = "SELECT parsed_content FROM documents WHERE path = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, path);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("parsed_content");
            }
        }
        return null;
    }
    
    @SneakyThrows
    public void createDocument(String path, String parsed) {
        String insertSQL = "INSERT OR REPLACE INTO documents (path, parsed_content) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, path);
            preparedStatement.setString(2, parsed);
            preparedStatement.executeUpdate();
        }
    }
    
    @SneakyThrows
    public void clearCache() {
        String deleteSQL = "DELETE FROM documents";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteSQL);
        }
    }
    
    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }
    
    public static void resetInstance() {
        dbConnection = null;
    }
}
