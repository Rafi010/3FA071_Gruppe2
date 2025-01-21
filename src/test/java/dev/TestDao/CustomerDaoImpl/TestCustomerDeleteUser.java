package dev.TestDao.CustomerDaoImpl;

import dev.BaseTest;
import dev.TestUtils;
import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class TestCustomerDeleteUser extends BaseTest {

@BeforeEach
public void initiate(){
    connection.createAllTables();
}

    private void createUser(UUID id) throws SQLException {
        String query = "INSERT INTO kunde (UUID, Anrede, Vorname, Nachname, Geburtsdatum) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.getConnection().prepareStatement(query);
         // UUID erstellen
        statement.setString(1, id.toString()); // UUID
        statement.setString(2, ICustomer.Gender.M.toString()); // Anrede
        statement.setString(3, "Max"); // Vorname
        statement.setString(4, "Mustermann"); // Nachname
        statement.setDate(5, java.sql.Date.valueOf(LocalDate.now())); // Geburtsdatum
        statement.executeUpdate();

    }

    @Test
    public void testDeleteUser(){
        UUID uuid = UUID.randomUUID();
        try {
            createUser(uuid);
        } catch (SQLException e) {
            fail("SQLException occurred while creating User in DB: " + e.getMessage());
        }
        CustomerDao customerDao = new CustomerDaoImpl(connection.getConnection());
        customerDao.deleteUser(uuid.toString());

        try {
            String query = "SELECT * FROM kunde WHERE uuid = ?";
            PreparedStatement statement = connection.getConnection().prepareStatement(query);
            statement.setString(1, uuid.toString());
            assertThrows(SQLException.class, () -> statement.executeUpdate(), "Diese UUID ist in der Tabelle nicht vorhanden");
        } catch (SQLException e) {
            fail("SQLException occurred while checking if UUID exist: " + e.getMessage());
        }
    }



}
