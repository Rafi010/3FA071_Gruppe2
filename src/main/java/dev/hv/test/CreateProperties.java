package dev.hv.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Class responsible for creating a properties file for database configuration.
 * This properties file specifies the URL, user, and password for the database.
 */
public class CreateProperties {

    private static final Logger log = LoggerFactory.getLogger(CreateProperties.class);
    private static final String DB_URL_PROPERTY_SUFFIX = ".db.url";
    private static final String DB_USER_PROPERTY_SUFFIX = ".db.user";
    private static final String DB_PASSWORD_PROPERTY_SUFFIX = ".db.pw";
    private static final String DB_JDBC_URL = "jdbc:mariadb://localhost:3306/hv?allowMultiQueries=true";
    private static final String DB_DEFAULT_USER = "root";
    private static final String DB_DEFAULT_PASSWORD = "";

    public void Start() {
        // Extracted method for getting user.home property
        Path configFilePath = getConfigFilePath("hv.properties");

        String userName = System.getProperty("user.name");
        String urlKey = userName + DB_URL_PROPERTY_SUFFIX;
        String userKey = userName + DB_USER_PROPERTY_SUFFIX;
        String passwordKey = userName + DB_PASSWORD_PROPERTY_SUFFIX;
        String databaseComment = getDatabaseComment(userName);

        if (Files.exists(configFilePath)) {
            log.info("Config file already exists at {}", configFilePath);
        } else {
            createConfigFile(configFilePath, urlKey, userKey, passwordKey, databaseComment);
        }
    }

    private Path getConfigFilePath(String fileName) {
        String homeDir = System.getProperty("user.home");
        return Paths.get(homeDir, fileName);
    }

    private String getDatabaseComment(String userName) {
        return "Database Configuration for " + userName;
    }

    private void createConfigFile(Path configFilePath, String urlKey, String userKey, String passwordKey, String comment) {
        Properties config = new Properties();
        config.setProperty(urlKey, DB_JDBC_URL);
        config.setProperty(userKey, DB_DEFAULT_USER);
        config.setProperty(passwordKey, DB_DEFAULT_PASSWORD);

        try (FileOutputStream output = new FileOutputStream(configFilePath.toFile())) {
            config.store(output, comment);
            log.info("Config file created at {}", configFilePath);
        } catch (IOException e) {
            log.error("Error creating config file: ", e);
        }
    }
}