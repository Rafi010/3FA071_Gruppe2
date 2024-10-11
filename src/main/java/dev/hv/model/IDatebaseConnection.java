package dev.hv.model;

import java.sql.Connection;
import java.util.Properties;

public interface IDatebaseConnection {

    public IDatebaseConnection openConnection(Properties properties);
    public void createAllTables();
    public void truncateAllTables();
    public void removeAllTables();
    public void closeConnection();


}
