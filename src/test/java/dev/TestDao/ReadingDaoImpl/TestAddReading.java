package dev.TestDao.ReadingDaoImpl;

import dev.hv.model.IReading;
import dev.hv.model.IReading.KindOfMeter;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.entities.Customer;
import dev.hv.projectFiles.DAO.entities.Reading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestAddReading {

    private Connection connection; // Database connection for testing
    private ReadingDaoImpl readingDao; // DAO under test

    @BeforeEach
    void setUp() throws SQLException {
        // Create an in-memory H2 database
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        resetDatabase(); // Reset and initialize the database schema
        readingDao = new ReadingDaoImpl(connection);
    }

    @Test
    void testAddReadingWithStromMeter() {
        // Arrange
        IReading reading = createTestReading(KindOfMeter.STROM, "123", "1234", 500.0, "Adding STROM reading");

        // Act & Assert
        assertDoesNotThrow(() -> readingDao.addReading(reading));
        assertTrue(isReadingInDatabase("strom", "1234")); // Verify that the entry was added
    }

    @Test
    void testAddReadingWithUnknownMeter() {
        // Arrange
        IReading reading = createTestReading(KindOfMeter.UNBEKANNT, "123", "1234", 500.0, "Adding unknown reading");

        // Act
        assertDoesNotThrow(() -> readingDao.addReading(reading));

        // Assert
        assertFalse(isReadingInDatabase("unknown", "1234")); // Entry should not be added
    }

    @Test
    void testAddReadingWithInvalidData() {
        // Arrange: Create a reading with invalid data (e.g., null customer ID)
        IReading reading = createTestReading(KindOfMeter.STROM, null, "1234", 500.0, "Invalid data");

        // Act & Assert: Expect validation to throw an exception
        assertThrows(IllegalArgumentException.class, () -> readingDao.addReading(reading));
    }

    @Test
    void testAddReadingWithSqlException() throws SQLException {
        // Arrange: Delete the schema to force an SQLException
        connection.createStatement().execute("DROP TABLE strom");
        IReading reading = createTestReading(KindOfMeter.STROM, "123", "1234", 500.0, "SQL Exception test");

        // Act & Assert: Method should handle the exception gracefully
        assertDoesNotThrow(() -> readingDao.addReading(reading));
    }

    private IReading createTestReading(KindOfMeter kind, String customerId, String meterId, double count, String comment) {
        // Creating a test implementation of IReading
        return new Reading() {{
            setKindOfMeter(kind);
            setCustomer(new Customer() {{
                setId(customerId != null ? java.util.UUID.randomUUID() : null);
            }});
            setMeterId(meterId);
            setMeterCount(count);
            setComment(comment);
            setDateOfReading(LocalDate.now());
        }};
    }

    private void resetDatabase() throws SQLException {
        // Drop tables if they exist and recreate them
        connection.createStatement().execute("DROP TABLE IF EXISTS strom;");
        connection.createStatement().execute("DROP TABLE IF EXISTS wasser;");
        connection.createStatement().execute("DROP TABLE IF EXISTS heizung;");
        createTestDatabaseSchema();
    }

    private void createTestDatabaseSchema() throws SQLException {
        // Creating schema for the tests with H2 database
        connection.createStatement().execute("""
            CREATE TABLE strom (
                kundenid VARCHAR(255) NOT NULL,
                zaehlernummer VARCHAR(255) NOT NULL,
                datum DATE NOT NULL,
                zaehlerstand_in_kwh DOUBLE NOT NULL,
                kommentar VARCHAR(255));
        """);

        connection.createStatement().execute("""
            CREATE TABLE wasser (
                kundenid VARCHAR(255) NOT NULL,
                zaehlernummer VARCHAR(255) NOT NULL,
                datum DATE NOT NULL,
                zaehlerstand_in_m3 DOUBLE NOT NULL,
                kommentar VARCHAR(255));
        """);

        connection.createStatement().execute("""
            CREATE TABLE heizung (
                kundenid VARCHAR(255) NOT NULL,
                zaehlernummer VARCHAR(255) NOT NULL,
                datum DATE NOT NULL,
                zaehlerstand_in_mwh DOUBLE NOT NULL,
                kommentar VARCHAR(255));
        """);
    }

    private boolean isReadingInDatabase(String tableName, String meterId) {
        try (var stmt = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE zaehlernummer = ?")) {
            stmt.setString(1, meterId);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
