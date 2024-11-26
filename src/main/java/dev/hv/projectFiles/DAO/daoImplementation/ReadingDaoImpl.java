package dev.hv.projectFiles.DAO.daoImplementation;

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
            String query = "INSERT INTO readings (id, comment, customer_id, date_of_reading, kind_of_meter, meter_count, meter_id, substitute) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, reading.getId().toString());
            stmt.setString(2, reading.getComment());
            stmt.setString(3, reading.getCustomer().getId().toString());
            stmt.setDate(4, Date.valueOf(reading.getDateOfReading()));
            stmt.setString(5, reading.getKindOfMeter().toString());
            stmt.setDouble(6, reading.getMeterCount());
            stmt.setString(7, reading.getMeterId());
            stmt.setBoolean(8, reading.getSubstitute());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Reading getReadingById(String id) {
        try {
            String query = "SELECT * FROM readings WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Reading reading = new Reading();
                reading.setId(UUID.fromString(rs.getString("id")));
                reading.setComment(rs.getString("comment"));
                // `customer` is fetched separately using its ID
                reading.setCustomer(customerDao.getUserById(rs.getString("customer_id")));
                reading.setDateOfReading(rs.getDate("date_of_reading").toLocalDate());
                reading.setKindOfMeter(Reading.KindOfMeter.valueOf(rs.getString("kind_of_meter")));
                reading.setMeterCount(rs.getDouble("meter_count"));
                reading.setMeterId(rs.getString("meter_id"));
                reading.setSubstitute(rs.getBoolean("substitute"));
                return reading;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reading> getAllReadings() {
        List<Reading> readings = new ArrayList<>();
        try {
            String query = "SELECT * FROM readings";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Reading reading = new Reading();
                reading.setId(UUID.fromString(rs.getString("id")));
                reading.setComment(rs.getString("comment"));
                // Assuming `customer` is fetched separately using its ID
                reading.setCustomer(customerDao.getUserById(rs.getString("customer_id")));
                reading.setDateOfReading(rs.getDate("date_of_reading").toLocalDate());
                reading.setKindOfMeter(Reading.KindOfMeter.valueOf(rs.getString("kind_of_meter")));
                reading.setMeterCount(rs.getDouble("meter_count"));
                reading.setMeterId(rs.getString("meter_id"));
                reading.setSubstitute(rs.getBoolean("substitute"));
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
            String query = "UPDATE readings SET comment = ?, customer_id = ?, date_of_reading = ?, kind_of_meter = ?, meter_count = ?, meter_id = ?, substitute = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, reading.getComment());
            stmt.setString(2, reading.getCustomer().getId().toString());
            stmt.setDate(3, Date.valueOf(reading.getDateOfReading()));
            stmt.setString(4, reading.getKindOfMeter().toString());
            stmt.setDouble(5, reading.getMeterCount());
            stmt.setString(6, reading.getMeterId());
            stmt.setBoolean(7, reading.getSubstitute());
            stmt.setString(8, reading.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReading(String id) {
        try {
            String query = "DELETE FROM readings WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
