package TestDao.CustomerDaoImpl;

import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Test_ReadingDaoImpl {

    private static final String JDBC_URL = "jdbc:h2:mem:testdb";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    private Connection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE strom (id VARCHAR(255) PRIMARY KEY, kundenid VARCHAR(255), zaehlernummer VARCHAR(255), datum DATE, zaehlerstand_in_kwh DOUBLE, kommentar VARCHAR(255))");
        }
        return connection;
    }

    @Test
    void testDeleteReadingWithValidParameters() {
        assertDoesNotThrow(() -> {
            Connection connection = createConnection();
            try {
                String id = UUID.randomUUID().toString();
                String insertQuery = "INSERT INTO strom (id, kundenid, zaehlernummer, datum, zaehlerstand_in_kwh, kommentar) VALUES (?, '123', '456', CURRENT_DATE, 100.0, 'Test Comment')";

                try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                    stmt.setString(1, id);
                    stmt.executeUpdate();
                }

                ReadingDaoImpl readingDao = new ReadingDaoImpl(connection);
                readingDao.deleteReading(IReading.KindOfMeter.STROM, id);

            } finally {
                connection.close();
            }
        });
    }

    @Test
    void testDeleteReadingWithWaterMeter() {}
    void testDeleteReadingWithHeatMeter() {}
    void testDeleteReadingWithUnknownMeter() {}

    @Test
    void testDeleteReadingWithInvalidParameters() {
        assertThrows(RuntimeException.class, () -> {
            try (Connection connection = createConnection()) {
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
