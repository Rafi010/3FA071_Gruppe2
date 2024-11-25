package dev.hv.projectFiles.DAO.daoInterfaces;

import dev.hv.projectFiles.DAO.entities.Reading;

import java.util.List;

public interface ReadingDao<Reading> {
    void addReading(Reading reading);
    Reading getReadingById(int id);
    List<Reading> getAllReadings();
    void updateReading(Reading reading);
    void deleteReading(int id);
}
