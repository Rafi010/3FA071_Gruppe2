package dev.hv.test;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JUnitStartTest {
    private CreateProperties createProp;
    private DatabaseConnection connection;
    private StartTest startTest;

    @BeforeEach
    void setUp() {
        // Mocking the CreateProperties and DatabaseConnection classes
        createProp = Mockito.mock(CreateProperties.class);
        connection = Mockito.mock(DatabaseConnection.class);
        // Creating an instance of StartTest to call the main method
        startTest = new StartTest(createProp, connection);
    }

    @Test
    void testMain() {
        // Define behavior of mocks
        doNothing().when(createProp).Start();
        when(connection.openConnection(any())).thenReturn(connection);
        doNothing().when(connection).createAllTables();

        // Calling the main method
        startTest.main(new String[]{});

        // Verifying that the methods were called with correct arguments
        verify(createProp).Start();
        verify(connection).openConnection(any());
        verify(connection).createAllTables();
    }

    // Constructor for dependency injection in tests (optional, not needed anymore)
    public JUnitStartTest() {
    }

    // The main method
    public static class StartTest {
        private final CreateProperties createProp;
        private final DatabaseConnection connection;

        public StartTest(CreateProperties createProp, DatabaseConnection connection) {
            this.createProp = createProp;
            this.connection = connection;
        }

        public void main(String[] args) {
            createProp.Start();
            connection.openConnection(null);
            connection.createAllTables();
        }
    }
}