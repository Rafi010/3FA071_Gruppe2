package dev.TestDao.ReadingDaoImpl;

import dev.BaseTest;
import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
/*
class TestDeleteReading extends BaseTest {


    @BeforeEach
    public void initiate() {
        connection.createAllTables();
    }


    @Test
    void testDeleteReadingWithValidParameters() {
        Connection conn = connection.getConnection();
        String id = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> {
            String insertQuery = "INSERT INTO strom (kundenid, zaehlernummer, datum, zaehlerstand_in_kwh, kommentar) VALUES (?, '456', CURRENT_DATE, 100.0, 'Test Comment')";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, id);
            stmt.executeUpdate();
        });


    }


    @Test
    void testDeleteReadingWithWaterMeter() {
    }

    void testDeleteReadingWithHeatMeter() {
    }

    void testDeleteReadingWithUnknownMeter() {
    }

    //@Test
    void testDeleteReadingWithInvalidParameters() {
        Connection conn = connection.getConnection();
        try {
            ReadingDaoImpl readingDao = new ReadingDaoImpl(conn);
            readingDao.deleteReading(IReading.KindOfMeter.STROM, "non-existing-id");
        } catch (RuntimeException e) {
            fail("SQLException occurred while checking if tables exist: " + e.getMessage());
        }
    }
}
/**
 * Test für Wasserzähler
 * Test für Stromzähler
 * Test für Heizung
 * Test für Unbekannt (z.B. Null kindofMeter)
 */
