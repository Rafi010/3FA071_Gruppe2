LOAD DATA LOCAL INFILE 'src/main/resources/kunden_utf8.csv'
INTO TABLE kunde
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(UUID, @Anrede, Vorname, Nachname, @Geburtsdatum)
SET
    Anrede = CASE
                WHEN @Anrede COLLATE utf8mb4_general_ci = 'Herr' THEN 'M'
                WHEN @Anrede COLLATE utf8mb4_general_ci = 'Frau' THEN 'W'
                WHEN @Anrede COLLATE utf8mb4_general_ci = 'k.A.' THEN 'U'
                ELSE NULL
             END,
    Geburtsdatum = IF(@Geburtsdatum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@Geburtsdatum, '%d.%m.%Y'))
;
LOAD DATA LOCAL INFILE 'src/unit_tests/resources/heizung_sql.csv'
INTO TABLE heizung
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(kundenid, zaehlernummer, @datum, zaehlerstand_in_mwh, kommentar)
SET datum = IF(@datum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@datum, '%d.%m.%Y'))
;
LOAD DATA LOCAL INFILE 'src/unit_tests/resources/wasser_sql.csv'
INTO TABLE wasser
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(kundenid, zaehlernummer, @datum, zaehlerstand_in_m³, kommentar)
SET datum = IF(@datum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@datum, '%d.%m.%Y'))
;
LOAD DATA LOCAL INFILE 'src/unit_tests/resources/strom_sql.csv'
INTO TABLE strom
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(kundenid, zaehlernummer, @datum, zaehlerstand_in_kwh, kommentar)
SET datum = IF(@datum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@datum, '%d.%m.%Y'))
;