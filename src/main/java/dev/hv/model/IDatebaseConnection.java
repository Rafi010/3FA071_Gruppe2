package dev.hv.model;

import java.sql.SQLException;
import java.util.Properties;

/**
 * This interface defines the contract for database connection operations.
 * It provides methods to open a connection, manage tables, and close the connection.
 */
public interface IDatebaseConnection {

    public IDatebaseConnection openConnection(Properties properties) throws SQLException;
    public void createAllTables();
    public void truncateAllTables();
    public void removeAllTables();
    public void closeConnection();


}
