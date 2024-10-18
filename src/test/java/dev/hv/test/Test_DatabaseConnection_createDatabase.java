package dev.hv.test;

import dev.hv.dbComm.DatabaseConnection;
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
    private DatabaseConnection dbConnection;

    @Before
    public void setUp() throws Exception {
        setConnection(dbConnection, connection);
    }

    @Test
    public void testCreateDatabase() {
        dbConnection.createDatabase();
        // Überprüfungen und Assertions hier
    }

    private void setConnection(DatabaseConnection dbConnection, Connection connection) throws Exception {
        Field connectionField = DatabaseConnection.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(dbConnection, connection);
    }

    // Weitere Tests
}