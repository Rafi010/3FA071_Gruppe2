package dev.hv.test;

import dev.hv.dbComm.CreateProperties;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreateProperties {
    private Path configFilePath;

    @BeforeEach
    public void setUp() {
        String resourcesDir = "src/test/resources";
        configFilePath = Paths.get(resourcesDir, "test_hv.properties");
    }

    @Test
    public void testStartCreatesConfigFile() throws IOException {
        String userName = System.getProperty("user.name");

        // Set the properties file path in the CreateProperties class if needed
        // This line depends on your CreateProperties class implementation
        // new CreateProperties().setConfigPath(configFilePath.toString());

        // Call the method
        new CreateProperties().Start();

        // Verify the config file was created (or already exists)
        assertTrue(Files.exists(configFilePath), "Config file should be created or already exist");

        // Load the properties from the file
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(configFilePath.toFile())) {
            properties.load(input);
        }

        // Verify the properties
        assertEquals("jdbc:mariadb://localhost:3306/hv?allowMultiQueries=true", properties.getProperty(userName + ".db.url"));
        assertEquals("root", properties.getProperty(userName + ".db.user"));
        assertEquals("", properties.getProperty(userName + ".db.pw"));
    }
}