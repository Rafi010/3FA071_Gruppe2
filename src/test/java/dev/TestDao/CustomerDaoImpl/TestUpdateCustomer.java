package dev.TestDao.CustomerDaoImpl;

import dev.BaseTest;
import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUpdateCustomer extends BaseTest {

    @BeforeEach
    public void initiate() {
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
    public void testUpdateDeletedUser() throws SQLException {
        UUID uuid = UUID.randomUUID();
        createUser(uuid); // Erstelle einen Benutzer mit der UUID
        CustomerDao<Customer> customerDao = new CustomerDaoImpl(connection.getConnection());
        customerDao.deleteCustomer(uuid.toString()); // Lösche den Benutzer, um sicherzustellen, dass die UUID nicht existiert

        String query = "UPDATE kunde SET Vorname = ? WHERE uuid = ?";
        PreparedStatement statement = connection.getConnection().prepareStatement(query);
        statement.setString(1, "Thomas");
        statement.setString(2, uuid.toString());

        int rowsAffected = statement.executeUpdate(); // Führt das UPDATE aus
        assertEquals(0, rowsAffected, "Das UPDATE sollte keine Zeilen betreffen");

    }


}


