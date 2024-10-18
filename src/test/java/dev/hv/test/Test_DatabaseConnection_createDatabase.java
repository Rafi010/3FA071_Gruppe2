package dev.hv.test;

import dev.hv.projectFiles.DatabaseConnection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.sql.Connection;

@RunWith(MockitoJUnitRunner.class)
public class Test_DatabaseConnection_createDatabase {

    @Mock
    private Connection connection;

    @InjectMocks
    private dev.hv.projectFiles.DatabaseConnection dbConnection;

    @Before
    public void setUp() throws Exception {
        setConnection(dbConnection, connection);  // Korrigierte Parameter und Methode
    }

    @Test
    public void testCreateDatabase() {
        dbConnection.createDatabase();  // Korrigierter Methodenaufruf
        // Überprüfungen und Assertions hier
    }

    private void setConnection(DatabaseConnection dbConnection, Connection connection) throws Exception {
        Field connectionField = dbConnection.getClass().getDeclaredField("connection");  // Dynamisches Feldabruf
        connectionField.setAccessible(true);
        connectionField.set(dbConnection, connection);
    }

    // Weitere Tests
}