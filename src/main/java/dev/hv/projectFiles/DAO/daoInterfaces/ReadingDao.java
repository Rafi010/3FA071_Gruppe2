package dev.hv.projectFiles.DAO.daoInterfaces;

import dev.hv.model.IReading;
import dev.hv.projectFiles.DAO.entities.Reading;

import java.util.List;

public interface ReadingDao<Reading> {
    void addReading(Reading reading);
    Reading getReadingById(IReading.KindOfMeter kindOfMeter, String id);
    List<Reading> getAllReadings(IReading.KindOfMeter kindOfMeter);
    void updateReading(Reading reading);
    void deleteReading(IReading.KindOfMeter kindOfMeter, String id);
}
