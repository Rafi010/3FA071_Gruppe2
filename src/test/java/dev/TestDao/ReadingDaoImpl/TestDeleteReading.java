package dev.TestDao.ReadingDaoImpl;

import dev.TestUtils;
import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TestDeleteReading {

    private static DatabaseConnection databaseConnection;

    @BeforeAll
    static void setUp() throws SQLException {
        // Initialisiert die Datenquelle und DatabaseConnection vor jedem Test
        Connection connection = TestUtils.getTestDbConnection();
        databaseConnection = new DatabaseConnection();
        databaseConnection.setConnection(connection);
        databaseConnection.createAllTables();

    }


    @Test
    void testDeleteReadingWithValidParameters() {
        Connection connection = databaseConnection.getConnection();
        String id = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> {
            String insertQuery = "INSERT INTO strom (id, kundenid, zaehlernummer, datum, zaehlerstand_in_kwh, kommentar) VALUES (?, '123', '456', CURRENT_DATE, 100.0, 'Test Comment')";
            PreparedStatement stmt = connection.prepareStatement(insertQuery);
            stmt.setString(1, id);
            stmt.executeUpdate();
        });

            ReadingDaoImpl readingDao = new ReadingDaoImpl(connection);
            readingDao.deleteReading(IReading.KindOfMeter.STROM, id);
    }


    @Test
    void testDeleteReadingWithWaterMeter() {}
    void testDeleteReadingWithHeatMeter() {}
    void testDeleteReadingWithUnknownMeter() {}

    @Test
    void testDeleteReadingWithInvalidParameters() {
        assertThrows(RuntimeException.class, () -> {
            try (Connection connection = databaseConnection.getConnection()) {
                ReadingDaoImpl readingDao = new ReadingDaoImpl(connection);
                readingDao.deleteReading(IReading.KindOfMeter.STROM, "non-existing-id");
            }
        });
    }
}
/**
 * Test für Wasserzähler
 *Test für Stromzähler
 * Test für Heizung
 * Test für Unbekannt (z.B. Null kindofMeter)
 */
