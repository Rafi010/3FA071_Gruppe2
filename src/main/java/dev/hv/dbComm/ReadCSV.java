package dev.hv.dbComm;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCSV {

    private static final Logger log = LoggerFactory.getLogger(ReadCSV.class);

    public static void main(String[] args) {
        String csvFile = "C:\\Git\\3FA071_Gruppe2\\src\\main\\resources\\heizung.csv";
        readFlexibleCSV(csvFile);
        csvFile = "C:\\Git\\3FA071_Gruppe2\\src\\main\\resources\\strom.csv";
        readFlexibleCSV(csvFile);
        csvFile = "C:\\Git\\3FA071_Gruppe2\\src\\main\\resources\\wasser.csv";
        readFlexibleCSV(csvFile);

    }

    public static void readFlexibleCSV(String csvFile) {


        try {
            //Create CSVParser
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')  // Set the separator to semicolon
                    .build();

            // Pass the parser to the CSVReader
            CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile))
                    .withCSVParser(parser)  // Use the custom parser
                    .build();

            String[] line;
            boolean readingMeta = true;
            List<String[]> dataList = new ArrayList<>();
            List<String> sqlList = new ArrayList<>();
            boolean bo = true;
            String toCommand;

            // Variables to store meta information
            String kunde = null;
            String zaehlernummer = null;

            while ((line = reader.readNext()) != null) {
                // Skip empty lines
                if (line.length == 0 || (line.length == 1 && line[0].trim().isEmpty())) {
                    continue;
                }

                // Check for Meta Data: Kunde and Zählernummer
                if (readingMeta) {
                    if (line[0].equalsIgnoreCase("Kunde")) {
                        kunde = line[1];  // Store Kunde value
                    } else if (line[0].equalsIgnoreCase("Zählernummer")) {
                        zaehlernummer = line[1];  // Store Zählernummer value
                    } else if (line[0].equalsIgnoreCase("Datum")) {
                        readingMeta = false;  // We reached the actual data section
                    }
                }

                // Read actual data if we are past the metadata section
                if (!readingMeta) {
                    dataList.add(line);
                }
            }

            // Output the meta data
            System.out.println("Kunde: " + kunde);
            System.out.println("Zählernummer: " + zaehlernummer);

            // Output the actual data
            System.out.println("\nDaten:");

            for (String[] dataLine : dataList) {
                if (bo) {
                    toCommand = """                
                            "Kunde", "Zaehlernummer","%s","%s","%s"
                            """.formatted(dataLine[0], dataLine[1], dataLine[2]);
                    bo = false;
                } else {
                    dataLine[1] = dataLine[1].replace(",", ".");
                    System.out.println(dataLine[0] + ", " + dataLine[1] + ", " + dataLine[2]);
                    toCommand = """                
                            "%s","%s","%s","%s","%s"
                            """.formatted(kunde, zaehlernummer, dataLine[0], dataLine[1], dataLine[2]);
                    //kunde + zaehlernummer + dataLine[0] + dataLine[1] + dataLine[2];
                }
                sqlList.add(toCommand);
            }

        } catch (IOException e) {
            log.error("IOException: ", e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
