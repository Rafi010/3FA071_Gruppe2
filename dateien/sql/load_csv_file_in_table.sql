--Importieren der CSV-Datei in die Tabelle "kunde".
LOAD DATA LOCAL INFILE 'src/main/resources/kunden_utf8.csv' --"LOCAL" bedeutet, dass die Datei auf dem Rechner liegt.
INTO TABLE kunde --Importiert die Daten aus der CSV-Datei in die Tabelle "kunde".
CHARACTER SET utf8mb4 --Legt den Zeichensatz für die importierten Daten auf UTF-8 mb4 fest
FIELDS TERMINATED BY ',' --Die Felder sind in der CSV-Datei durch Kommas (,) getrennt.
ENCLOSED BY '"' --Gibt an, dass jede Zeile in der Datei mit einem Zeilenumbruch (\n) abgeschlossen wird.
LINES TERMINATED BY '\n' --Gibt an, dass jede Zeile in der Datei mit einem Zeilenumbruch (\n) abgeschlossen wird.
IGNORE 1 ROWS --Ignoriert die erste Spalte bzw. Spaltenüberschrift
(UUID, @Anrede, Vorname, Nachname, @Geburtsdatum)
SET
    Anrede = CASE
                --Setzt die Variable "Anrede" auf 'M', 'W', 'U' oder NULL, basierend auf dem Wert von "@Anrede".
                WHEN @Anrede COLLATE utf8mb4_general_ci = 'Herr' THEN 'M'
                WHEN @Anrede COLLATE utf8mb4_general_ci = 'Frau' THEN 'W'
                WHEN @Anrede COLLATE utf8mb4_general_ci = 'k.A.' THEN 'U'
                ELSE NULL
             END,
    Geburtsdatum = IF(@Geburtsdatum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@Geburtsdatum, '%d.%m.%Y'))
;
--Importieren der CSV-Datei in die Tabelle "heizung"
LOAD DATA LOCAL INFILE 'src/unit_tests/resources/heizung_sql.csv'
INTO TABLE heizung
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(kundenid, zaehlernummer, @datum, zaehlerstand_in_mwh, kommentar)
SET datum = IF(@datum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@datum, '%d.%m.%Y')) --Setzt die Variable "datum" auf NULL, wenn der Wert von "@datum" leer ist; andernfalls wird "@datum" in ein Datum im Format 'DD-MM-YYYY' umgewandelt.
;
--Importieren der CSV-Datei in dei Tabelle "wasser"
LOAD DATA LOCAL INFILE 'src/unit_tests/resources/wasser_sql.csv'
INTO TABLE wasser
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(kundenid, zaehlernummer, @datum, zaehlerstand_in_m³, kommentar)
SET datum = IF(@datum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@datum, '%d.%m.%Y')) --Setzt die Variable "datum" auf NULL, wenn der Wert von "@datum" leer ist; andernfalls wird "@datum" in ein Datum im Format 'DD-MM-YYYY' umgewandelt.
;
--Importieren der CSV-Datei in die Tabelle "strom"
LOAD DATA LOCAL INFILE 'src/unit_tests/resources/strom_sql.csv'
INTO TABLE strom
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(kundenid, zaehlernummer, @datum, zaehlerstand_in_kwh, kommentar)
SET datum = IF(@datum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@datum, '%d.%m.%Y')) --Setzt die Variable "datum" auf NULL, wenn der Wert von "@datum" leer ist; andernfalls wird "@datum" in ein Datum im Format 'DD-MM-YYYY' umgewandelt.
;