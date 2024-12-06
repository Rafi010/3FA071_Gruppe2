package dev.TestDao.CustomerDaoImpl;

import dev.TestUtils;
import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class TestCustomerDeleteUser {

    private static DatabaseConnection databaseConnection;


    @BeforeAll
    public static void setUp() throws SQLException {
        // Initialisiert die Datenquelle und DatabaseConnection vor jedem Test
        Connection connection = TestUtils.getTestDbConnection();
        databaseConnection = new DatabaseConnection();
        databaseConnection.setConnection(connection);
        databaseConnection.createAllTables();

    }


    private void createUser(UUID id) throws SQLException {
        String query = "INSERT INTO kunde (UUID, Anrede, Vorname, Nachname, Geburtsdatum) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = databaseConnection.getConnection().prepareStatement(query);
         // UUID erstellen
        statement.setString(1, id.toString()); // UUID
        statement.setString(2, ICustomer.Gender.M.toString()); // Anrede
        statement.setString(3, "Max"); // Vorname
        statement.setString(4, "Mustermann"); // Nachname
        statement.setDate(5, java.sql.Date.valueOf(LocalDate.now())); // Geburtsdatum
        statement.executeUpdate();

    }

    @Test
    public void testDeleteUser() throws SQLException {
        UUID uuid = UUID.randomUUID();
        createUser(uuid);
        CustomerDao customerDao = new CustomerDaoImpl(databaseConnection.getConnection());
        customerDao.deleteUser(uuid.toString());

        String query = "SELECT * FROM kunde WHERE uuid = ?";
        PreparedStatement statement = databaseConnection.getConnection().prepareStatement(query);
        statement.setString(1, uuid.toString());
        assertThrows(SQLException.class, () -> statement.executeUpdate(), "Diese UUID ist in der Tabelle nicht vorhanden");
    }



}
