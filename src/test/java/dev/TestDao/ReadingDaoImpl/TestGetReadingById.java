package dev.TestDao.ReadingDaoImpl;

import dev.BaseTest;
import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TestDeleteReading extends BaseTest {

    private ReadingDaoImpl readingDao;

    @BeforeEach
    public void initiate() throws SQLException {
        connection.createAllTables();
        Connection conn = connection.getConnection();
        readingDao = new ReadingDaoImpl(conn);
    }

    @Test
    void testDeleteReadingWithValidParameters() {
        Connection conn = connection.getConnection();
        String id = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> {
            String insertQuery = "INSERT INTO strom (uuid, kundenid, zaehlernummer, datum, zaehlerstand_in_kwh, kommentar) VALUES (?, 'kunde123', '456', CURRENT_DATE, 100.0, 'Test Comment')";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
            }

            // Act
            readingDao.deleteReading(IReading.KindOfMeter.STROM, id);

            // Assert
            // Hier sollten Sie überprüfen, ob der Eintrag tatsächlich gelöscht wurde
        });
    }

    @Test
    void testDeleteReadingWithWaterMeter() {
        // Implementieren Sie den Test für das Löschen eines Wassereintrags
    }

    @Test
    void testDeleteReadingWithHeatMeter() {
        // Implementieren Sie den Test für das Löschen eines Heizungseintrags
    }

    @Test
    void testDeleteReadingWithUnknownMeter() {
        // Implementieren Sie den Test für das Löschen eines Eintrags mit unbekanntem Zählertyp
    }

    @Test
    void testDeleteReadingWithInvalidParameters() {
        Connection conn = connection.getConnection();
        try {
            ReadingDaoImpl readingDao = new ReadingDaoImpl(conn);
            assertThrows(RuntimeException.class, () -> {
                readingDao.deleteReading(IReading.KindOfMeter.STROM, "non-existing-id");
            });
        } catch (SQLException e) {
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }
    }
}
