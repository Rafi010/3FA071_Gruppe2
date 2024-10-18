package dev.hv.dbComm;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCSV {

    private static final Logger log = LoggerFactory.getLogger(ReadCSV.class);

    //main not necessary but for testing purpose
    public static void main(String[] args) {
        String csvFile = "src\\main\\resources\\heizung.csv";
        readFlexibleCSV(csvFile, 0);
        csvFile = "src\\main\\resources\\strom.csv";
        readFlexibleCSV(csvFile, 1);
        csvFile = "src\\main\\resources\\wasser.csv";
        readFlexibleCSV(csvFile, 2);
    }

    // read and write csv files, params: path to csv (example in main method) and int for heizung (0), strom (1), wasser (2)
    public static List<String> readFlexibleCSV(String csvFile, int type) {
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


            //Output SQL ready
            for (String[] dataLine : dataList) {
                if (bo) {
                    toCommand = """                
                            Kunde,Zaehlernummer,%s,%s,%s
                            """.formatted(dataLine[0], dataLine[1], dataLine[2]);
                    bo = false;
                } else {
                    //dataLine[1] = dataLine[1].replace(",", ".");
                    toCommand = """                
                            %s,%s,%s,%s,%s
                            """.formatted(kunde, zaehlernummer,
                            dataLine[0].trim(),  // Datum
                            dataLine[1].replace(",", ".").trim(),  // Zählerstand
                            dataLine.length > 2 && dataLine[2].trim().isEmpty() ? "null" : dataLine[2].trim()); // Kommentar

                }
                // Skip empty commands
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

            return sqlList;
        } catch (IOException e) {
            log.error("IOException: ", e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void writeCSV(String outputFile, List<String> sqlList) {
        try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(outputFile))
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)  // No quotes around fields
                .build()) {

            // Iterate over each string in sqlList
            for (String line : sqlList) {
                // Skip empty or blank lines
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }

                // Split the line by comma into individual columns
                String[] columns = line.split(",");

                // Remove trailing empty or unnecessary columns
                List<String> trimmedColumns = new ArrayList<>();
                for (String column : columns) {
                    // Trim spaces and remove unnecessary quotes
                    column = column.trim();
                    if (!column.isEmpty() && !column.equals("\"")) {  // Ignore empty or just quotes
                        trimmedColumns.add(column);
                    }
                }

                // Convert the trimmed columns back to an array
                String[] finalColumns = trimmedColumns.toArray(new String[0]);

                // Write the cleaned columns to the CSV file
                writer.writeNext(finalColumns);
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }


}
