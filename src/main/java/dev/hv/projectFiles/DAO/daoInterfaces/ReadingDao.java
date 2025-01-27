package dev.hv.projectFiles.DAO.daoInterfaces;

import dev.hv.model.IReading;

import java.util.List;

/**
 * Interface welches die Funktionen für die ReadingDao beschreibt.
 */
public interface ReadingDao<Reading> {
    void addReading(IReading reading);
    Reading getReadingById(IReading.KindOfMeter kindOfMeter, String id);
    List<Reading> getAllReadings(IReading.KindOfMeter kindOfMeter);
    void updateReading(IReading reading);
    void deleteReading(IReading.KindOfMeter kindOfMeter, String id);
}
