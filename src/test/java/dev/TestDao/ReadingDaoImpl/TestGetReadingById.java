package dev.TestDao.ReadingDaoImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.entities.Reading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestGetReadingById {

    private TestGetReadingById readingService;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Verbindung zur H2-Datenbank herstellen
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");

        // Testdaten in die Datenbank einfügen
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE strom (uuid VARCHAR PRIMARY KEY, kommentar VARCHAR, kundenid VARCHAR, datum DATE, zaehlerstand_in_kwh DOUBLE, zaehlernummer VARCHAR)");
            stmt.execute("INSERT INTO strom (uuid, kommentar, kundenid, datum, zaehlerstand_in_kwh, zaehlernummer) VALUES ('123e4567-e89b-12d3-a456-426614174000', 'Testkommentar', 'kunde123', '2023-10-01', 100.0, 'Z12345')");
        }

        // ReadingService mit der Verbindung initialisieren
        readingService = new TestGetReadingById();
        readingService.setConnection(connection);
    }

    @Test
    public void testGetReadingById() {
        // Act
        Reading reading = readingService.getReadingById(IReading.KindOfMeter.STROM, "123e4567-e89b-12d3-a456-426614174000");

        // Assert
        assertNotNull(reading);
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), reading.getId());
        assertEquals("Testkommentar", reading.getComment());
        assertEquals("kunde123", reading.getCustomer().getId()); // Angenommen, dass getCustomerById ein Customer-Objekt mit ID zurückgibt
        assertEquals("2023-10-01", reading.getDateOfReading().toString());
        assertEquals(IReading.KindOfMeter.STROM, reading.getKindOfMeter());
        assertEquals(100.0, reading.getMeterCount());
        assertEquals("Z12345", reading.getMeterId());
    }

    @Test
    public void testGetReadingByIdUnknownMeter() {
        // Act
        Reading reading = readingService.getReadingById(IReading.KindOfMeter.UNBEKANNT, "any-id");

        // Assert
        assertNull(reading);
    }
}

