package dev.hv.dbComm;

import dev.hv.model.IDatabaseConnection;
import dev.hv.test.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;



public class DatabaseConnection implements IDatabaseConnection {

    private Connection connection;
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);

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
            System.out.println("Connected");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public void createAllTables() {
        if (connection == null) {
            throw new IllegalStateException("No open database connection");
        }
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SHOW TABLES");
            while(rs.next()){
                String tabellenname = rs.getString(1);
                System.out.println(tabellenname);
            }

        } catch (SQLException e) {
            log.error("error: ", e);  // Prints detailed information about the exception
        }
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

    public Connection getConnection() {
        return connection;
    }
}
