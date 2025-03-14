package dev.Test_DatabaseConnection;

import dev.BaseTest;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestOpenConnection extends BaseTest {

    @Test
    public void isOpen(){
        // Retrieve the connection
        Connection conn = connection.getConnection();

        // Check that the connection is not null (i.e., it was opened)
        assertNotNull(conn, "Connection should not be null");

        // Check that the connection is open
        try {
            assertFalse(conn.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }
    }
}
