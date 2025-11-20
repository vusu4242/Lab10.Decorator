package ua.ucu.apps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import lombok.SneakyThrows;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;
    
    @SneakyThrows
    private DBConnection() {
        this.connection = DriverManager.getConnection(
            "jdbc:sqlite:/Users/sasha/sources_ucu/cache.db");
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
    
    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }
}