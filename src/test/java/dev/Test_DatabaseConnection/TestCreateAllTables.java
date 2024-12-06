package dev.Test_DatabaseConnection;

import dev.TestUtils;
import dev.hv.projectFiles.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Klasse ist eine JUnit-Testklasse, die verschiedene Tests für die Methode `createAllTables`
 * der Klasse `DatabaseConnection` durchführt.
 */
class TestCreateAllTables {

    private static DatabaseConnection databaseConnection;

    /**
     * Diese Methode wird vor jedem einzelnen Test ausgeführt und initialisiert das `databaseConnection` Objekt.
     * Sie stellt sicher, dass eine neue Datenbankverbindung und eine leere Testdatenbank vor jedem Test vorhanden sind.
     */
    @BeforeAll
    static void setUp() throws SQLException {
        // Initialisiert die Datenquelle und DatabaseConnection vor jedem Test
        Connection connection = TestUtils.getTestDbConnection();
        databaseConnection = new DatabaseConnection();
        databaseConnection.setConnection(connection);

    }

    @Test
    void testCreateAllTables() {
        Connection connection = databaseConnection.getConnection();

        try {
            assertFalse(doesTableExist(connection, "heizung"));
            assertFalse(doesTableExist(connection, "wasser"));
            assertFalse(doesTableExist(connection, "strom"));
            assertFalse(doesTableExist(connection, "kunde"));
        } catch (SQLException e) {
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }

        databaseConnection.createAllTables();

        try {
            assertTrue(doesTableExist(connection, "heizung"));
            assertTrue(doesTableExist(connection, "wasser"));
            assertTrue(doesTableExist(connection, "strom"));
            assertTrue(doesTableExist(connection, "kunde"));
        } catch (SQLException e) {
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }


    }

    /**
     * Checks if a table with the given name exists in the database.
     *
     * @param connection the database connection to use for the query
     * @param tableName the name of the table to check for
     * @return true if the table exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    private boolean doesTableExist(Connection connection, String tableName) throws SQLException {
        // Use DatabaseMetaData to fetch table metadata
        try (var tables = connection.getMetaData().getTables(
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