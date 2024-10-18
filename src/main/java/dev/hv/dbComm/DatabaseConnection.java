package dev.hv.dbComm;

import dev.hv.model.IDatebaseConnection;
import dev.hv.test.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;



public class DatabaseConnection implements IDatebaseConnection {
    //connection is saved locally so all db interaction can happen only within this class
    private Connection connection;

    //opens a connection to mySql (uses the url in the properties file wich does not connect to the hv database)
    @Override
    public IDatebaseConnection openConnection(Properties properties){
        final String userName = System.getProperty("user.name");
        final String home = System.getProperty("user.home");
        try {
            //loads the key-value pairs into the properties object
            properties.load(new FileReader(home + Util.backOrForward() + "hv.properties"));
            //gets the needed values out of the properties file
            final String dburl = properties.getProperty(userName + ".db.url");
            final String dbuser = properties.getProperty(userName + ".db.user");
            final String dbpw = properties.getProperty(userName + ".db.pw");
            //uses the values to create the connection and save it
            this.connection = DriverManager.getConnection(dburl, dbuser, dbpw);
            System.out.println("Connected to MySql");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    //opens a connection to mySql (uses the url in the properties file wich does connect to the hv database)
    public void openHvConnection (Properties properties){
        String userName = System.getProperty("user.name");
        final String home = System.getProperty("user.home");
        try {
            //loads the key-value pairs into the properties object
            properties.load(new FileReader(home + Util.backOrForward() + "hv.properties"));
            //gets the needed values out of the properties file
            String dburl = properties.getProperty(userName + ".db.url_db");
            String dbuser = properties.getProperty(userName + ".db.user");
            String dbpw = properties.getProperty(userName + ".db.pw");
            //uses the values to create the connection and save it
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
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }
        Util.executeSQL(connection, "dateien/sql/truncate_table.sql");
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
