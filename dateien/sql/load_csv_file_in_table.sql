--Importieren der CSV-Datei in die Tabelle "kunde".

--"LOCAL" bedeutet, dass die Datei auf dem Rechner liegt.
LOAD DATA LOCAL INFILE 'src/main/resources/kunden_utf8.csv'
--Importiert die Daten aus der CSV-Datei in die Tabelle "kunde".
INTO TABLE kunde
--Legt den Zeichensatz für die importierten Daten auf UTF-8 mb4 fest
CHARACTER SET utf8mb4
--Die Felder sind in der CSV-Datei durch Kommas (,) getrennt.
FIELDS TERMINATED BY ','
--Gibt an, dass jede Zeile in der Datei mit einem Zeilenumbruch (\n) abgeschlossen wird.
ENCLOSED BY '"'
--Gibt an, dass jede Zeile in der Datei mit einem Zeilenumbruch (\n) abgeschlossen wird.
LINES TERMINATED BY '\n'
--Ignoriert die erste Spalte bzw. Spaltenüberschrift
IGNORE 1 ROWS
(UUID, @Anrede, Vorname, Nachname, @Geburtsdatum)
SET
    Anrede = CASE
                --
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
--Setzt die Variable "datum" auf NULL, wenn der Wert von "@datum" leer ist; andernfalls wird "@datum" in ein Datum im Format 'DD-MM-YYYY' umgewandelt.
SET datum = IF(@datum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@datum, '%d.%m.%Y'))
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
--Setzt die Variable "datum" auf NULL, wenn der Wert von "@datum" leer ist; andernfalls wird "@datum" in ein Datum im Format 'DD-MM-YYYY' umgewandelt.
SET datum = IF(@datum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@datum, '%d.%m.%Y'))
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
--Setzt die Variable "datum" auf NULL, wenn der Wert von "@datum" leer ist; andernfalls wird "@datum" in ein Datum im Format 'DD-MM-YYYY' umgewandelt.
SET datum = IF(@datum COLLATE utf8mb4_general_ci = '', NULL, STR_TO_DATE(@datum, '%d.%m.%Y'))
;