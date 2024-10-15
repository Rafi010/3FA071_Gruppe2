package dev.hv.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class CreateProperties {

    private static final Logger log = LoggerFactory.getLogger(CreateProperties.class);

    public void Start(){
        String homeDir = System.getProperty("user.home");
        Path configFilePath = Paths.get(homeDir, "hv.properties");
        String userName = System.getProperty("user.name");

        String URL = userName + ".db.url";
        String USER = userName + ".db.user";
        String PW = userName + ".db.pw";

        String DatabaseComment = "Database Configuration for " + userName;

        // Check if the config file already exists
        if (Files.exists(configFilePath)) {
            System.out.println("Config file already exists at " + configFilePath);
        } else {
            // Create the Properties object
            Properties config = new Properties();

            // Add the key-value pairs to the config
            config.setProperty(URL, "jdbc:mariadb://localhost:3306/hv?allowMultiQueries=true");
            config.setProperty(USER, "root");
            config.setProperty(PW, "");

            // Save the properties to the file
            try (FileOutputStream output = new FileOutputStream(configFilePath.toFile())) {
                config.store(output, DatabaseComment);
                System.out.println("Config file created at " + configFilePath);
            } catch (IOException e) {
                log.error("error: ", e);
            }
        }
    }
}
