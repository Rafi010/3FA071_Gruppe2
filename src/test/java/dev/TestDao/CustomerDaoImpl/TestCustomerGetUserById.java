package dev.TestDao.CustomerDaoImpl;

import dev.BaseTest;
import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoImplementation.CustomerDaoImpl;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestCustomerGetUserById extends BaseTest {

    @BeforeEach
    public void initiate(){
        connection.createAllTables();
    }

    @Test
    void testGetUserByID() {
        UUID randomUuid = UUID.randomUUID();
        Date date = Date.valueOf(LocalDate.now());
        try {
            saveUserInDb(connection.getConnection(), randomUuid, date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        CustomerDao<User> customerDao = new CustomerDaoImpl(connection.getConnection());
        ICustomer user = customerDao.getUserById(randomUuid.toString());

        assertEquals(ICustomer.Gender.M, user.getGender());
        assertEquals("Max", user.getFirstName());
        assertEquals("Mustermann", user.getLastName());
        assertEquals(date, Date.valueOf(user.getBirthDate()));

    }

    private void saveUserInDb(Connection connection, UUID uuid, Date date) throws SQLException{
        String query = "INSERT INTO kunde (UUID, Anrede, Vorname, Nachname, Geburtsdatum) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, uuid.toString()); // UUID
        stmt.setString(2, ICustomer.Gender.M.toString()); // Anrede
        stmt.setString(3, "Max"); // Vorname
        stmt.setString(4, "Mustermann"); // Nachname
        stmt.setDate(5, date); // Geburtsdatum
        stmt.executeUpdate();
    }
}
