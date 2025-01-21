package dev;

import dev.hv.projectFiles.DatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;
import java.util.Properties;

public class BaseTest {

    protected static DatabaseConnection connection;

    @BeforeAll
    public static void setUp() throws SQLException {
        Properties properties = new Properties();
        connection = DatabaseConnection.getInstance();
        connection.openConnection(properties);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        // Close the connection after all tests have completed
        if (connection != null && !connection.getConnection().isClosed()) {
            connection.closeConnection();
        }
    }

    @BeforeEach
    public void prepare() {
        connection.removeAllTables();
    }

}