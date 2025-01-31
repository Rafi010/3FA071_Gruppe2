package dev.hv.projectFiles;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReadCSV {

    private static final Logger log = LoggerFactory.getLogger(ReadCSV.class);

    // main-Methode ist nicht notwendig, aber für Testzwecke eingebaut
    public static void main(String[] args) {
        String csvFile = "src\\main\\resources\\heizung.csv";
        readFlexibleCSV(csvFile, 0);
        csvFile = "src\\main\\resources\\strom.csv";
        readFlexibleCSV(csvFile, 1);
        csvFile = "src\\main\\resources\\wasser.csv";
        readFlexibleCSV(csvFile, 2);
    }

    // CSV-Dateien lesen und schreiben, Parameter: Pfad zur CSV-Datei (Beispiel in der main-Methode) und Integer für Heizung (0), Strom (1), Wasser (2)
    public static void readFlexibleCSV(String csvFile, int type) {
        try {
            // CSVParser erstellen
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();

            List<String[]> dataList;
            List<String> sqlList;
            boolean bo;
            String toCommand;
            String kunde;
            String zaehlernummer;
            String zaehlerstandTitle = null; // Dynamischer Titel für Zählerstand

            try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile))
                    .withCSVParser(parser)
                    .build()) {

                String[] line;
                boolean readingMeta = true;
                dataList = new ArrayList<>();
                sqlList = new ArrayList<>();
                bo = true;

                // Variablen zum Speichern von Metadaten
                kunde = null;
                zaehlernummer = null;

                while ((line = reader.readNext()) != null) {
                    // Leere Zeilen überspringen
                    if (line.length == 0 || (line.length == 1 && line[0].trim().isEmpty())) {
                        continue;
                    }

                    // Überprüfen auf Metadaten: Kunde und Zählernummer
                    if (readingMeta) {
                        if (line[0].equalsIgnoreCase("Kunde")) {
                            kunde = line[1];
                        } else if (line[0].equalsIgnoreCase("Zählernummer")) {
                            zaehlernummer = line[1];
                        } else if (line[0].equalsIgnoreCase("Datum")) {
                            readingMeta = false;
                            zaehlerstandTitle = line[2]; // Dynamischer Titel für Zählerstand
                        }
                    }

                    // Daten lesen, wenn wir über den Metadatenabschnitt hinaus sind
                    if (!readingMeta) {
                        dataList.add(line);
                    }
                }
            }

            // SQL-fertige Ausgabe generieren
            for (String[] dataLine : dataList) {
                if (bo) {
                    toCommand = "uuid,Kunde,Zaehlernummer,Datum,%s,Kommentar".formatted(zaehlerstandTitle);
                    bo = false;
                } else {
                    String comment = dataLine.length > 2 ? dataLine[2].trim() : "";
                    if (comment.startsWith("Zählertausch: neue Nummer")) {
                        String[] parts = comment.split("Nummer");
                        if (parts.length > 1) {
                            zaehlernummer = parts[1].trim();
                        }
                    }

                    // Generiere eine neue UUID für jede Zeile
                    String uuid = UUID.randomUUID().toString();

                    toCommand = "%s,%s,%s,%s,%s,%s".formatted(
                            uuid, // UUID hinzufügen
                            kunde,
                            zaehlernummer,
                            dataLine[0].trim(),
                            dataLine[1].replace(",", ".").trim(),
                            comment.isEmpty() ? "\\N" : comment
                    );
                }
                if (!toCommand.trim().isEmpty()) {
                    sqlList.add(toCommand);
                }
            }

            switch (type) {
                case 0:
                    writeCSV("src\\main\\resources\\heizung_sql.csv", sqlList);
                    break;
                case 1:
                    writeCSV("src\\main\\resources\\strom_sql.csv", sqlList);
                    break;
                case 2:
                    writeCSV("src\\main\\resources\\wasser_sql.csv", sqlList);
                    break;
            }

        } catch (IOException e) {
            log.error("IOException: ", e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }


    public static void writeCSV(String outputFile, List<String> sqlList) {
        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(outputFile))
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)  // Keine Anführungszeichen um die Felder
                .build()) {

            // Iteriert über jede Zeichenkette in sqlList
            for (String line : sqlList) {
                // Leere oder nur Leerzeichen enthaltende Zeilen überspringen
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }

                // Zerlegt die Zeile anhand von Kommas in einzelne Spalten
                String[] columns = line.split(",");

                // Entfernt leere oder unnötige Spalten
                List<String> trimmedColumns = new ArrayList<>();
                for (String column : columns) {
                    // Entfernt Leerzeichen und unnötige Anführungszeichen
                    column = column.trim();
                    if (!column.isEmpty() && !column.equals("\"")) {  // Ignoriert leere oder nur Anführungszeichen enthaltende Spalten
                        trimmedColumns.add(column);
                    }
                }

                // Konvertiert die bereinigten Spalten zurück in ein Array
                String[] finalColumns = trimmedColumns.toArray(new String[0]);

                // Schreibt die bereinigten Spalten in die CSV-Datei
                writer.writeNext(finalColumns);
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben in die CSV-Datei: " + e.getMessage());
        }
    }
}
