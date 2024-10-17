package dev.hv.dbComm;

import dev.hv.model.IDatabaseConnection;
import dev.hv.test.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;



public class DatabaseConnection implements IDatabaseConnection {

    private Connection connection;

    @Override
    public IDatabaseConnection openConnection(Properties properties){
        String userName = System.getProperty("user.name");
        try {
            final String home = System.getProperty("user.home");
            properties.load(new FileReader(home + Util.backOrForward() + "hv.properties"));
            String dburl = properties.getProperty(userName + ".db.url");
            String dbuser = properties.getProperty(userName + ".db.user");
            String dbpw = properties.getProperty(userName + ".db.pw");
            this.connection = DriverManager.getConnection(dburl, dbuser, dbpw);
            System.out.println("Connected to MySql");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public void openHvConnection (Properties properties){
        String userName = System.getProperty("user.name");
        try {
            final String home = System.getProperty("user.home");
            properties.load(new FileReader(home + Util.backOrForward() + "hv.properties"));
            String dburl = properties.getProperty(userName + ".db.url_db");
            String dbuser = properties.getProperty(userName + ".db.user");
            String dbpw = properties.getProperty(userName + ".db.pw");
            this.connection = DriverManager.getConnection(dburl, dbuser, dbpw);
            System.out.println("Connected to: hv");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDatabase(){
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }
        Util.executeSQL(connection, "dateien/sql/create_db_hv.sql");
    }

    @Override
    public void createAllTables() {
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }
        Util.executeSQL(connection, "dateien/sql/create_table.sql");
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
        try {
            connection.close();
            System.out.println("Connection closed");
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fillDatabase(){
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }
        Util.executeSQL(connection, "dateien/sql/load_csv_file_in_table.sql");
    }

    public Connection getConnection() {
        return connection;
    }
}
