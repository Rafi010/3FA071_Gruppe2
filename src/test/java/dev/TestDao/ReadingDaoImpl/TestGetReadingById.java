package dev.TestDao.ReadingDaoImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import dev.BaseTest;
import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoImplementation.ReadingDaoImpl;
import dev.hv.projectFiles.DAO.entities.Reading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/*
public class TestGetReadingById extends BaseTest {

    private ReadingDaoImpl readingService;

    @BeforeEach
    public void initiate() {
        connection.createAllTables();
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

 */
