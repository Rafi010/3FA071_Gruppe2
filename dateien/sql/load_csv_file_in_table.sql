LOAD DATA LOCAL INFILE 'src/main/resources/kunden_utf8.csv'
INTO TABLE kunde
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(UUID, Anrede, Vorname, Nachname, @Geburtsdatum)
SET Geburtsdatum = STR_TO_DATE(@Geburtsdatum, '%d.%m.%Y');