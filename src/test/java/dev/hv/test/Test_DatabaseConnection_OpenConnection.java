package dev.hv.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class Test_DatabaseConnection_OpenConnection {

    @TempDir
    Path tempDir;

    @Test
    public void testOpenConnectionSuccess() throws Exception {
        // Setup temporary properties file
        Path propertiesFile = tempDir.resolve("hv.properties");
        try (FileWriter writer = new FileWriter(propertiesFile.toFile())) {
            writer.write(System.getProperty("user.name") + ".db.url=jdbc:h2:mem:test\n");
            writer.write(System.getProperty("user.name") + ".db.user=sa\n");
            writer.write(System.getProperty("user.name") + ".db.pw=\n");
        }

        // Load properties
        Properties properties = new Properties();
        properties.load(new FileReader(propertiesFile.toString()));

        // Create test instance and invoke the method
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.openConnection(properties);

        // Verify connection is not null and valid
        assertNotNull(connection);
        assertTrue(connection.isValid(2), "Connection should be valid");

        // Clean up
        connection.close();
    }

    @Test
    public void testPropertiesFileMissing() {
        Properties properties = new Properties();
        DatabaseConnection dbConnection = new DatabaseConnection();

        SQLException exception = assertThrows(SQLException.class,
                () -> dbConnection.openConnection(properties),
                "Expected openConnection to throw, but it didn't");

        assertTrue(exception.getMessage().contains("hv.properties"));
    }

    @Test
    public void testInvalidDbUrl() throws Exception {
        // Setup temporary properties file
        Path propertiesFile = tempDir.resolve("hv.properties");
        try (FileWriter writer = new FileWriter(propertiesFile.toFile())) {
            writer.write(System.getProperty("user.name") + ".db.url=invalid-url\n");
            writer.write(System.getProperty("user.name") + ".db.user=sa\n");
            writer.write(System.getProperty("user.name") + ".db.pw=\n");
        }

        // Load properties
        Properties properties = new Properties();
        properties.load(new FileReader(propertiesFile.toString()));

        // Create test instance and invoke the method
        DatabaseConnection dbConnection = new DatabaseConnection();

        SQLException exception = assertThrows(SQLException.class,
                () -> dbConnection.openConnection(properties),
                "Expected openConnection to throw, but it didn't");

        assertTrue(exception.getMessage().contains("No suitable driver found"));
    }

    @Test
    public void testMissingDbUserOrPassword() throws Exception {
        // Setup temporary properties file
        Path propertiesFile = tempDir.resolve("hv.properties");
        try (FileWriter writer = new FileWriter(propertiesFile.toFile())) {
            writer.write(System.getProperty("user.name") + ".db.url=jdbc:h2:mem:test\n");
            writer.write(System.getProperty("user.name") + ".db.user=\n");
            writer.write(System.getProperty("user.name") + ".db.pw=\n");
        }

        // Load properties
        Properties properties = new Properties();
        properties.load(new FileReader(propertiesFile.toString()));

        // Create test instance and invoke the method
        DatabaseConnection dbConnection = new DatabaseConnection();

        SQLException exception = assertThrows(SQLException.class,
                () -> dbConnection.openConnection(properties),
                "Expected openConnection to throw, but it didn't");

        assertTrue(exception.getMessage().contains("Access denied for user"));
    }

    // Example implementation of the DatabaseConnection class
    class DatabaseConnection {
        private Connection connection;

        public Connection openConnection(Properties properties) {
            String userName = System.getProperty("user.name");
            try {
                final String home = System.getProperty("user.home");
                // Assuming Util.backOrForward() returns the correct separator
                properties.load(new FileReader(home + java.io.File.separator + "hv.properties"));
                String dburl = properties.getProperty(userName + ".db.url");
                String dbuser = properties.getProperty(userName + ".db.user");
                String dbpw = properties.getProperty(userName + ".db.pw");
                this.connection = DriverManager.getConnection(dburl, dbuser, dbpw);
                System.out.println("Connected to MySql");
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
            return this.connection;
        }
    }
}