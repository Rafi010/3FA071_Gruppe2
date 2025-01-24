package dev.hv.projectFiles.DAO.daoImplementation;

import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Reading;
import dev.hv.projectFiles.DAO.entities.Customer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Implementierung des ReadingDao-Interfaces für die Verwaltung von Messwerten (Readings) in der Datenbank.
 * Diese Klasse bietet Methoden zum Hinzufügen, Abrufen, Aktualisieren und Löschen von Messwertdaten.
 */
public class ReadingDaoImpl implements ReadingDao<Reading> {
    private final Connection connection; // Verbindung zur Datenbank
    private final CustomerDao<Customer> customerDao; // DAO für Kunden
    private final Validator validator;

    /**
     * Konstruktor, der die Datenbankverbindung initialisiert.
     * @param connection die Datenbankverbindung
     */
    public ReadingDaoImpl(Connection connection) {
        this.connection = connection;
        this.customerDao = new CustomerDaoImpl(connection); // Initialisierung des CustomerDao
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Fügt einen neuen Messwert in die Datenbank ein.
     * @param reading das Messwert-Objekt, das hinzugefügt werden soll
     */
    @Override
    public void addReading(IReading reading) {

        validateReading(reading);

        try {
            String zaehlerstand = "";
            // Bestimmung der Spalte basierend auf der Zählerart
            switch (reading.getKindOfMeter()) {
                case STROM -> zaehlerstand = "zaehlerstand_in_kwh";
                case WASSER -> zaehlerstand = "zaehlerstand_in_m³";
                case HEIZUNG -> zaehlerstand = "zaehlerstand_in_mwh";
                case UNBEKANNT -> {
                    return; // Falls die Art unbekannt ist, keine Aktion
                }
            }

            // SQL-Query für das Einfügen eines neuen Messwerts
            String query = "INSERT INTO ? (kundenid, zaehlernummer, datum, ?, kommentar) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, reading.getKindOfMeter().toString().toLowerCase()); // table to insert into depending on kind of meter
            stmt.setString(2, zaehlerstand); // name of column to change the zählerstand depending on kind of meter
            stmt.setString(3, reading.getCustomer().getId().toString()); // Kunden-ID
            stmt.setString(4, reading.getMeterId()); // Zählernummer
            stmt.setDate(5, Date.valueOf(reading.getDateOfReading())); // Datum
            stmt.setDouble(6, reading.getMeterCount()); // Zählerstand
            stmt.setString(7, reading.getComment()); // Kommentar
            stmt.executeUpdate(); // SQL ausführen
        } catch (SQLException | NullPointerException e) {
            System.out.println("Es wurden nicht alle erforderlichen Werte des Objekts erfüllt.\nEs wurde nicht in die Datenbank gespeichert.");

        }
    }

    /**
     * Holt einen Messwert aus der Datenbank basierend auf der Zählerart und ID.
     * @param kindOfMeter die Art des Zählers
     * @param id die ID des Messwerts
     * @return das gefundene Reading-Objekt oder null, falls nicht gefunden
     */
    @Override
    public Reading getReadingById(IReading.KindOfMeter kindOfMeter, String id) {
        if (kindOfMeter == IReading.KindOfMeter.UNBEKANNT) return null; // Kein Abruf für unbekannte Zählerart
        String metre = kindOfMeter.toString().toLowerCase().replace("'", "");
        try {
            String query = "SELECT * FROM ? WHERE kundenid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, metre);
            stmt.setString(2, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Reading reading = new Reading();
                reading.setId(UUID.fromString(rs.getString("kundenid"))); // Kunden-ID
                reading.setComment(rs.getString("kommentar")); // Kommentar
                reading.setCustomer(customerDao.getCustomerById(rs.getString("kundenid"))); // Kundenobjekt abrufen
                reading.setDateOfReading(rs.getDate("datum").toLocalDate()); // Datum
                reading.setKindOfMeter(kindOfMeter); // Zählerart
                reading.setMeterCount(rs.getDouble("zaehlerstand")); // Zählerstand
                reading.setMeterId(rs.getString("zaehlernummer")); // Zählernummer
                return reading;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Falls kein Messwert gefunden wurde
    }

    /**
     * Holt alle Messwerte einer bestimmten Zählerart aus der Datenbank.
     * @param kindOfMeter die Art des Zählers
     * @return Liste mit Reading-Objekten
     */
    @Override
    public List<Reading> getAllReadings(IReading.KindOfMeter kindOfMeter) {
        if (kindOfMeter == IReading.KindOfMeter.UNBEKANNT) return null; // Keine Abfrage für unbekannte Zählerart
        List<Reading> readings = new ArrayList<>();
        String metre = kindOfMeter.toString().toLowerCase().replace("'", "");
        try {
            String query = "SELECT * FROM ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, metre);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reading reading = new Reading();
                reading.setId(UUID.fromString(rs.getString("kundenid"))); // Kunden-ID
                reading.setComment(rs.getString("kommentar")); // Kommentar
                reading.setCustomer(customerDao.getCustomerById(rs.getString("kundenid"))); // Kundenobjekt abrufen
                reading.setDateOfReading(rs.getDate("datum").toLocalDate()); // Datum
                reading.setKindOfMeter(kindOfMeter); // Zählerart
                reading.setMeterCount(rs.getDouble("zaehlerstand")); // Zählerstand
                reading.setMeterId(rs.getString("zaehlernummer")); // Zählernummer
                readings.add(reading); // Messwert zur Liste hinzufügen
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return readings; // Liste mit Messwerten zurückgeben
    }

    /**
     * Aktualisiert einen bestehenden Messwert in der Datenbank.
     * @param reading das Reading-Objekt mit aktualisierten Werten
     */
    @Override
    public void updateReading(IReading reading) {

        validateReading(reading);

        try {
            String zaehlerstand = "";
            switch (reading.getKindOfMeter()) {
                case STROM -> zaehlerstand = "zaehlerstand_in_kwh";
                case WASSER -> zaehlerstand = "zaehlerstand_in_m³";
                case HEIZUNG -> zaehlerstand = "zaehlerstand_in_mwh";
                case UNBEKANNT -> {
                    return; // Keine Aktualisierung für unbekannte Zählerart
                }
            }

            // SQL-Query für das Aktualisieren eines Messwerts
            String query = "UPDATE ? SET kommentar = ?, kundenid = ?, datum = ?, zaehlernummer = ?, ? = ? WHERE zaehlernummer = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, reading.getKindOfMeter().toString().toLowerCase()); //table to update in depending on kind of meter
            stmt.setString(2, reading.getComment()); // Kommentar
            stmt.setString(3, reading.getCustomer().getId().toString()); // Kunden-ID
            stmt.setDate(4, Date.valueOf(reading.getDateOfReading())); // Datum
            stmt.setString(5, reading.getMeterId()); // Zählernummer
            stmt.setString(6, zaehlerstand); //kind of Zählerstand depending on kind of meter
            stmt.setDouble(7, reading.getMeterCount()); // Zählerstand
            stmt.setString(8, reading.getMeterId()); // Zählernummer für WHERE-Bedingung
            stmt.executeUpdate(); // SQL ausführen
        } catch (SQLException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Löscht einen Messwert aus der Datenbank basierend auf Zählerart und ID.
     * @param kindOfMeter die Art des Zählers
     * @param id die ID des Messwerts
     */
    @Override
    public void deleteReading(IReading.KindOfMeter kindOfMeter, String id) {
        String metre = kindOfMeter.toString().toLowerCase().replace("'", "");
        System.out.println(metre);
        try {
            String query = "DELETE FROM ? WHERE kundenid = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, metre);
            stmt.setString(2, id);
            stmt.executeUpdate(); // SQL ausführen
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateReading(IReading reading) {
        Set<ConstraintViolation<IReading>> violations = validator.validate(reading);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Validation failed for Reading:\n");

            // Add all validation failures to the error message
            for (ConstraintViolation<IReading> violation : violations) {
                sb.append("Property: ")
                        .append(violation.getPropertyPath())
                        .append(" - ")
                        .append(violation.getMessage()) // validation message
                        .append("\n");
            }

            // Throw an exception with the validation error message
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
