package dev.hv.projectFiles.DAO.daoImplementation;

import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.daoInterfaces.CustomerDao;
import dev.hv.projectFiles.DAO.daoInterfaces.ReadingDao;
import dev.hv.projectFiles.DAO.entities.Reading;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReadingDaoImpl implements ReadingDao<Reading> {
    private Connection connection;

    public ReadingDaoImpl(Connection connection) {
        this.connection = connection;
    }

    CustomerDao customerDao = new CustomerDaoImpl(connection);

    @Override
    public void addReading(Reading reading) {
        try {
            String zaehlerstand = "";
            switch (reading.getKindOfMeter()) {
                case STROM -> zaehlerstand = "zaehlerstand_in_kwh";
                case WASSER -> zaehlerstand = "zaehlerstand_in_m³";
                case HEIZUNG -> zaehlerstand = "zaehlerstand_in_mwh";
                case UNBEKANNT -> {
                    return;
                }
            }
            String query = "INSERT INTO ? (kundenid, zaehlernummer, datum, ?, kommentar) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, reading.getKindOfMeter().toString());
            stmt.setString(2, zaehlerstand);
            stmt.setString(3, reading.getCustomer().getId().toString());
            stmt.setString(4, reading.getMeterId());
            stmt.setDate(5, Date.valueOf(reading.getDateOfReading()));
            stmt.setDouble(6, reading.getMeterCount());
            stmt.setString(7, reading.getComment());
            stmt.executeUpdate();
        } catch (SQLException|NullPointerException e) {
            System.out.println("Es wurden nicht alle erforderlichen Werte des Objekts erfüllt.\nEs wurde nicht in die Datenbank gespeichert.");
            e.printStackTrace();
        }
    }

    @Override
    public Reading getReadingById(IReading.KindOfMeter kindOfMeter, String id) {
        if (kindOfMeter == IReading.KindOfMeter.UNBEKANNT) return null;
        try {
            String query = "SELECT * FROM ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, kindOfMeter.toString());
            stmt.setString(2, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Reading reading = new Reading();
                reading.setId(UUID.fromString(rs.getString("kundenid")));
                reading.setComment(rs.getString("kommentar"));
                // `customer` is fetched separately using its ID
                reading.setCustomer(customerDao.getUserById(rs.getString("kundenid")));
                reading.setDateOfReading(rs.getDate("datum").toLocalDate());
                reading.setKindOfMeter(kindOfMeter);
                reading.setMeterCount(rs.getDouble("meter_count"));
                reading.setMeterId(rs.getString("zaehlernummer"));
                return reading;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reading> getAllReadings(IReading.KindOfMeter kindOfMeter) {
        if (kindOfMeter == IReading.KindOfMeter.UNBEKANNT) return null;
        List<Reading> readings = new ArrayList<>();
        try {
            String query = "SELECT * FROM ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, kindOfMeter.toString());
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Reading reading = new Reading();
                reading.setId(UUID.fromString(rs.getString("kundenid")));
                reading.setComment(rs.getString("kommentar"));
                // `customer` is fetched separately using its ID
                reading.setCustomer(customerDao.getUserById(rs.getString("kundenid")));
                reading.setDateOfReading(rs.getDate("datum").toLocalDate());
                reading.setKindOfMeter(kindOfMeter);
                reading.setMeterCount(rs.getDouble("meter_count"));
                reading.setMeterId(rs.getString("zaehlernummer"));
                readings.add(reading);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readings;
    }

    @Override
    public void updateReading(Reading reading) {
        try {
            String zaehlerstand = "";
            switch (reading.getKindOfMeter()) {
                case STROM -> zaehlerstand = "zaehlerstand_in_kwh";
                case WASSER -> zaehlerstand = "zaehlerstand_in_m³";
                case HEIZUNG -> zaehlerstand = "zaehlerstand_in_mwh";
                case UNBEKANNT -> {
                    return;
                }
            }
            String query = "UPDATE ? SET kommentar = ?, kundenid = ?, datum = ?, zaehlernummer = ?, ? = ? WHERE zaehlernummer = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, reading.getKindOfMeter().toString());
            stmt.setString(2, reading.getComment());
            stmt.setString(3, reading.getCustomer().getId().toString());
            stmt.setDate(4, Date.valueOf(reading.getDateOfReading()));
            stmt.setString(5, reading.getMeterId());
            stmt.setString(6, zaehlerstand);
            stmt.setDouble(7, reading.getMeterCount());
            stmt.setString(8, reading.getMeterId());
            stmt.executeUpdate();
        } catch (SQLException|NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReading(IReading.KindOfMeter kindOfMeter, String id) {
        try {
            String query = "DELETE FROM ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, kindOfMeter.toString());
            stmt.setString(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
