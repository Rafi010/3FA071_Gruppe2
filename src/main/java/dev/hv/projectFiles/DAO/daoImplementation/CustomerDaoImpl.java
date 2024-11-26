package dev.hv.projectFiles.DAO.daoImplementation;

import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CustomerDaoImpl implements CustomerDao<User> {
private Connection connection;

    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * funktion um einen Nutzer in der Datenbank zu erstellen
     * @param user das nutzer Objekt welches in die DB eingefügt wird
     */
    @Override
    public void addUser(User user) {

        LocalDate birthDate = user.getBirthDate();
        Date sqlDate = Date.valueOf(birthDate);

        try {
            String query = "INSERT INTO kunde (UUID, Anrede, Vorname, Nachname, Geburtsdatum) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user.getId().toString());
            stmt.setString(2, user.getGender().toString());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setDate(5, sqlDate);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funktion um einen Benutzer aus der Datenbank anhand der ID zu bekommen
     * @param id id anhand welcher der nutzer/user gesucht wird
     * @return gibt den anhand der db daten erstellten nutzer zurück
     */
    @Override
    public ICustomer getUserById(String id) {
        try {
            //query vorlage
            String query = "SELECT * FROM users WHERE uuid = ?";
            //query in ein preparedStatement speichern
            PreparedStatement stmt = connection.prepareStatement(query);
            //fragezeichen in der query anhand vom index mit daten ersetzen
            stmt.setString(1, id);
            //ergebnisse der query in ein ResultSet speichern
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("uuid")));
                user.setGender(ICustomer.Gender.valueOf(rs.getString("anrede")));
                user.setFirstName(rs.getString("vorname"));
                user.setLastName(rs.getString("nachname"));
                user.setBirthDate(rs.getDate("geburtsdatum").toLocalDate());
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Funktion um alle Nutzer aus der DB auszugeben in einer liste
     * @return Liste mit den nutzer/user Objekten welche zuvor für jeden Eintrag erstellt wurden
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM kunde";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("uuid")));
                user.setGender(ICustomer.Gender.valueOf(rs.getString("anrede")));
                user.setFirstName(rs.getString("vorname"));
                user.setLastName(rs.getString("nachname"));
                user.setBirthDate(rs.getDate("geburtsdatum").toLocalDate());
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Funktion um einen nutzer aus der DB zu entfernen
     * @param id die ID des nutzers welcher aus der DB entfernt wird
     */
    @Override
    public void deleteUser(String id) {
        try {
            String query = "DELETE FROM kunde WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funktion um einen nutzer innerhalb der DB zu updaten
     * @param user das nutzer/user Objekt welches updated werden soll1
     */
    @Override
    public void updateUser(User user) {

        LocalDate birthDate = user.getBirthDate();
        Date sqlDate = Date.valueOf(birthDate);

        try {
            String query = "UPDATE users SET name = ?, email = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user.getId().toString());
            stmt.setString(2, user.getGender().toString());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setDate(5, sqlDate);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
