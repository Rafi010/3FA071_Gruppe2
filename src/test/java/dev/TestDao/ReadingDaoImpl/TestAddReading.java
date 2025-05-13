package dev.TestDao.ReadingDaoImpl;

import dev.BaseTest;
import dev.hv.model.ICustomer;
import dev.hv.model.IReading;
import dev.hv.model.IReading.KindOfMeter;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
/*
class TestAddReading extends BaseTest {

    private ReadingDaoImpl readingDao;

    @BeforeEach
    public void initiate() {
        connection.createAllTables();
        readingDao = new ReadingDaoImpl(connection.getConnection());
    }

    @Test
    void testAddReadingWithValidParameters() {
        Connection conn = connection.getConnection();
        String customerId = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> {
            // Add a valid reading
            IReading reading = createTestReading(KindOfMeter.STROM, customerId, "1234", 500.0, "Valid STROM reading");
            readingDao.addReading(reading);

            // Check that the entry exists in the database
            String query = "SELECT * FROM strom WHERE zaehlernummer = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "1234");
            assertTrue(stmt.executeQuery().next(), "Reading should be present in the database");
        });
    }

    @Test
    void testAddReadingWithWaterMeter() {
        Connection conn = connection.getConnection();
        String customerId = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> {
            // Add a valid water reading
            IReading reading = createTestReading(KindOfMeter.WASSER, customerId, "5678", 100.0, "Water reading test");
            readingDao.addReading(reading);

            // Check that the entry exists in the database
            String query = "SELECT * FROM wasser WHERE zaehlernummer = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "5678");
            assertTrue(stmt.executeQuery().next(), "Water reading should be present in the database");
        });
    }

    @Test
    void testAddReadingWithHeatMeter() {
        Connection conn = connection.getConnection();
        String customerId = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> {
            // Add a valid heat meter reading
            IReading reading = createTestReading(KindOfMeter.HEIZUNG, customerId, "9101", 150.0, "Heat meter test");
            readingDao.addReading(reading);

            // Check that the entry exists in the database
            String query = "SELECT * FROM heizung WHERE zaehlernummer = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "9101");
            assertTrue(stmt.executeQuery().next(), "Heat reading should be present in the database");
        });
    }

    @Test
    void testAddReadingWithUnknownMeter() {
        Connection conn = connection.getConnection();
        String customerId = UUID.randomUUID().toString();
        assertDoesNotThrow(() -> {
            // Attempt to add an unknown type of reading
            IReading reading = createTestReading(KindOfMeter.UNBEKANNT, customerId, "1111", 200.0, "Unknown meter test");

            // Expect the DAO to handle it gracefully and not insert anything
            readingDao.addReading(reading);

            String query = "SELECT * FROM strom WHERE zaehlernummer = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "1111");
            assertFalse(stmt.executeQuery().next(), "Unknown reading should not be added to the database");
        });
    }

    @Test
    void testAddReadingWithInvalidParameters() {
        Connection conn = connection.getConnection();
        assertThrows(IllegalArgumentException.class, () -> {
            // Add a reading with invalid data (e.g., null customer ID)
            IReading reading = createTestReading(KindOfMeter.STROM, null, "2222", 100.0, "Invalid reading test");
            readingDao.addReading(reading);
        });
    }

    private IReading createTestReading(KindOfMeter kind, String customerId, String meterId, double count, String comment) {
        // Simple utility method for creating test readings
        return new IReading() {
            @Override
            public UUID getId() {
                return null;
            }

            @Override
            public void setId(UUID id) {

            }

            private KindOfMeter kindOfMeter = kind;
            private String customer = customerId;
            private String meterIdValue = meterId;
            private double countValue = count;
            private String commentValue = comment;
            private LocalDate date = LocalDate.now();

            @Override
            public KindOfMeter getKindOfMeter() {
                return kindOfMeter;
            }

            @Override
            public String getCustomerId() {
                return customer;
            }

            @Override
            public String getMeterId() {
                return meterIdValue;
            }

            @Override
            public Boolean getSubstitute() {
                return null;
            }

            @Override
            public String printDateOfReading() {
                return "";
            }

            @Override
            public void setComment(String comment) {

            }

            @Override
            public void setCustomer(ICustomer customer) {

            }

            @Override
            public void setDateOfReading(LocalDate dateOfReading) {

            }

            @Override
            public void setKindOfMeter(KindOfMeter kindOfMeter) {

            }

            @Override
            public void setMeterCount(Double meterCount) {

            }

            @Override
            public void setMeterId(String meterId) {

            }

            @Override
            public void setSubstitute(Boolean substitute) {

            }

            @Override
            public double getMeterCount() {
                return countValue;
            }

            @Override
            public String getComment() {
                return commentValue;
            }

            @Override
            public ICustomer getCustomer() {
                return null;
            }

            @Override
            public LocalDate getDateOfReading() {
                return date;
            }
        };
    }
}

 */