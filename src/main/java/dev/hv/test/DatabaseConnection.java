package dev.hv.test;

import dev.hv.model.IDatebaseConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseConnection implements IDatebaseConnection {
    private Properties properties;

    public DatabaseConnection() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties(){
        String userHome = System.getProperty("user.home");
        System.getProperty("user.name");

        String filePath = Paths.get(userHome, "hv.properties").toString();

        try(InputStream input = new FileInputStream(filePath)){
            properties.load(input);
        } catch (IOException e) {
            try(InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
                if (input != null){
                    properties.load(input);
                } else {
                    throw new RuntimeException("Properties-Datei nicht gefunden!");
                }
            } catch (IOException ex) {
                throw new RuntimeException("Fehler beim Laden der Properties-Datei", ex);
            }
        }
    }

    private String getDatabaseProperty(String key){
        String userName = System.getProperty("user.name");
        return properties.getProperty(userName + ".db." + key);
    }

    @Override
    public IDatebaseConnection openConnection(Properties properties){
        String url = getDatabaseProperty("url");
        String user = getDatabaseProperty("user");
        String password = getDatabaseProperty("pw");

        //TODO DATENBANKÖFFNUNG IMPLEMENTIERUNG

        System.out.println("Verbindung geöffnet mit URL:" + url);
        return this;
    }

    @Override
    public void createAllTables(){
        //TODO
    }

    @Override
    public void truncateAllTables(){
        //TODO
    }

    @Override
    public void removeAllTables(){
        //TODO
    }

    @Override
    public void closeConnection(){
        //TODO
    }

    public static void main(String[] args){
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.openConnection(null);
    }
}
