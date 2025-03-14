package dev.Test_DatabaseConnection;

import dev.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Klasse ist eine JUnit-Testklasse, die verschiedene Tests für die Methode `createAllTables`
 * der Klasse `DatabaseConnection` durchführt.
 */
class TestCreateAllTables extends BaseTest {

    @BeforeEach
    public void initiate(){
        connection.removeAllTables();
    }

    @Test
    void testCreateAllTables() {
        Connection conn = connection.getConnection();

        try {
            assertFalse(doesTableExist(conn, "heizung"));
            assertFalse(doesTableExist(conn, "wasser"));
            assertFalse(doesTableExist(conn, "strom"));
            assertFalse(doesTableExist(conn, "kunde"));
        } catch (SQLException e) {
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }

        connection.createAllTables();

        try {
            assertTrue(doesTableExist(conn, "heizung"));
            assertTrue(doesTableExist(conn, "wasser"));
            assertTrue(doesTableExist(conn, "strom"));
            assertTrue(doesTableExist(conn, "kunde"));
        } catch (SQLException e) {
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }


    }

    /**
     * Checks if a table with the given name exists in the database.
     *
     * @param conn      the database conn to use for the query
     * @param tableName the name of the table to check for
     * @return true if the table exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    private boolean doesTableExist(Connection conn, String tableName) throws SQLException {
        // Use DatabaseMetaData to fetch table metadata
        try (var tables = conn.getMetaData().getTables(
                null, // Catalog (null means any)
                null, // Schema pattern (null means any)
                tableName.toUpperCase(), // Table name (case-insensitive)
                new String[]{"TABLE"} // Types to include (e.g., only "TABLE")
        )) {
            // Return true if the result set contains at least one table
            return tables.next();
        }
    }

}