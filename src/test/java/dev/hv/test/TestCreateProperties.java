package dev.hv.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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