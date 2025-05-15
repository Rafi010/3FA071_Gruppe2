--Erstellt die Tabelle Heizung mit den Spalten UUID, KundenID, Zaehlernummer, Datum, Zaehlernummer_in_mhw und Kommentar.
CREATE TABLE IF NOT EXISTS ablesung (
    uuid VARCHAR(255) PRIMARY KEY,
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255) NOT NULL,
    datum DATE NOT NULL,
    zaehlerstand FLOAT NOT NULL,
    kommentar VARCHAR(255),
    kindOfMeter VARCHAR(255) NOT NULL DEFAULT 'UNBEKANNT'
);

--Erstellt die Tabelle Kunde mit den Spalten UUID, Anrede, Nachname, Vorname und Geburtsdatum.
CREATE TABLE IF NOT EXISTS kunde (
    uuid VARCHAR(255) PRIMARY KEY,
    anrede VARCHAR(1) NOT NULL DEFAULT 'U',
    nachname VARCHAR(255) NOT NULL,
    vorname VARCHAR(255) NOT NULL,
    geburtsdatum DATE
);