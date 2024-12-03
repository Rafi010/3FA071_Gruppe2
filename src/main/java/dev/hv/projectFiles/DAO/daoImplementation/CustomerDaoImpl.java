package dev.hv.projectFiles.DAO.daoImplementation;

import dev.hv.model.ICustomer;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.entities.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementierung des CustomerDao-Interfaces für die Verwaltung von Kunden in der Datenbank.
 * Diese Klasse bietet Methoden zum Hinzufügen, Abrufen, Aktualisieren und Löschen von Benutzerdaten.
 */
public class CustomerDaoImpl implements CustomerDao<User> {

    private final Connection connection; // Datenbankverbindung

    /**
     * Konstruktor für CustomerDaoImpl.
     * @param connection die Datenbankverbindung, die von dieser DAO genutzt wird.
     */
    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * Funktion, um einen Nutzer in der Datenbank zu erstellen.
     * @param user das Nutzer-Objekt, das in die Datenbank eingefügt wird.
     */
    @Override
    public void addUser(User user) {
        // Konvertieren des Geburtsdatums in das SQL-Format
        LocalDate birthDate = user.getBirthDate();
        Date sqlDate = Date.valueOf(birthDate);

        try {
            // SQL-Query zum Einfügen eines neuen Nutzers
            String query = "INSERT INTO kunde (UUID, Anrede, Vorname, Nachname, Geburtsdatum) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user.getId().toString()); // UUID
            stmt.setString(2, user.getGender().toString()); // Anrede
            stmt.setString(3, user.getFirstName()); // Vorname
            stmt.setString(4, user.getLastName()); // Nachname
            stmt.setDate(5, sqlDate); // Geburtsdatum
            stmt.executeUpdate(); // SQL-Query ausführen
        } catch (SQLException e) {
            throw new RuntimeException(e); // Fehlerausgabe im Fehlerfall
        }
    }

    /**
     * Funktion, um einen Benutzer aus der Datenbank anhand der ID zu bekommen.
     * @param id die ID, anhand der der Nutzer gesucht wird.
     * @return gibt den anhand der Datenbankdaten erstellten Nutzer zurück.
     */
    @Override
    public ICustomer getUserById(String id) {
        try {
            // SQL-Query zum Abrufen eines Nutzers anhand der UUID
            String query = "SELECT * FROM kunde WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query); // PreparedStatement erstellen
            stmt.setString(1, id); // ID in die Query einfügen
            ResultSet rs = stmt.executeQuery(); // Query ausführen

            // Ergebnis verarbeiten, wenn der Nutzer gefunden wird
            if (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("uuid"))); // UUID setzen
                user.setGender(ICustomer.Gender.valueOf(rs.getString("anrede"))); // Anrede setzen
                user.setFirstName(rs.getString("vorname")); // Vorname setzen
                user.setLastName(rs.getString("nachname")); // Nachname setzen
                user.setBirthDate(rs.getDate("geburtsdatum").toLocalDate()); // Geburtsdatum setzen
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Fehlerausgabe im Fehlerfall
        }
        return null; // Rückgabe null, wenn kein Nutzer gefunden wurde
    }

    /**
     * Funktion, um alle Nutzer aus der Datenbank als Liste auszugeben.
     * @return Liste mit den Nutzer-Objekten, die zuvor für jeden Eintrag erstellt wurden.
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(); // Liste für die Nutzer erstellen
        try {
            // SQL-Query zum Abrufen aller Nutzer
            String query = "SELECT * FROM kunde";
            Statement stmt = connection.createStatement(); // Statement erstellen
            ResultSet rs = stmt.executeQuery(query); // Query ausführen

            // Ergebnis verarbeiten und Nutzerobjekte erstellen
            while (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("uuid"))); // UUID setzen
                user.setGender(ICustomer.Gender.valueOf(rs.getString("anrede"))); // Anrede setzen
                user.setFirstName(rs.getString("vorname")); // Vorname setzen
                user.setLastName(rs.getString("nachname")); // Nachname setzen
                user.setBirthDate(rs.getDate("geburtsdatum").toLocalDate()); // Geburtsdatum setzen
                users.add(user); // Nutzer zur Liste hinzufügen
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Fehlerausgabe im Fehlerfall
        }
        return users; // Rückgabe der Nutzerliste
    }

    /**
     * Funktion, um einen Nutzer aus der Datenbank zu entfernen.
     * @param id die ID des Nutzers, der aus der Datenbank entfernt wird.
     */
    @Override
    public void deleteUser(String id) {
        try {
            // SQL-Query zum Löschen eines Nutzers anhand der UUID
            String query = "DELETE FROM kunde WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query); // PreparedStatement erstellen
            stmt.setString(1, id); // ID in die Query einfügen
            stmt.executeUpdate(); // Query ausführen
        } catch (SQLException e) {
            throw new RuntimeException(e); // Fehlerausgabe im Fehlerfall
        }
    }

    /**
     * Funktion, um einen Nutzer innerhalb der Datenbank zu aktualisieren.
     * @param user das Nutzer-Objekt, das aktualisiert werden soll.
     */
    @Override
    public void updateUser(User user) {
        // Konvertieren des Geburtsdatums in das SQL-Format
        LocalDate birthDate = user.getBirthDate();
        Date sqlDate = Date.valueOf(birthDate);

        try {
            // SQL-Query zum Aktualisieren eines Nutzers
            String query = "UPDATE kunde SET anrede = ?, vorname = ?, nachname = ?, geburtsdatum = ? WHERE uuid = ?";
            PreparedStatement stmt = connection.prepareStatement(query); // PreparedStatement erstellen
            stmt.setString(1, user.getGender().toString()); // Anrede
            stmt.setString(2, user.getFirstName()); // Vorname
            stmt.setString(3, user.getLastName()); // Nachname
            stmt.setDate(4, sqlDate); // Geburtsdatum
            stmt.setString(5, user.getId().toString()); // UUID
            stmt.executeUpdate(); // Query ausführen
        } catch (SQLException e) {
            throw new RuntimeException(e); // Fehlerausgabe im Fehlerfall
        }
    }
}
