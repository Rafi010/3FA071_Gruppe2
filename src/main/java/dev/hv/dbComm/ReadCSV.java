package dev.hv.dbComm;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class ReadCSV {

    private static final Logger log = LoggerFactory.getLogger(ReadCSV.class);

    public static void main(String[] args) {
        String csvFile = "C:\\Git\\3FA071_Gruppe2\\src\\main\\resources\\heizung.csv";
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println("Spalte 1: " + line[0] + ", Spalte 2: " + line[1]);
            }
        } catch (IOException e) {
            log.error("e: ", e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}