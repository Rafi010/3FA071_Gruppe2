--Erstellt die Tabelle Heizung mit den Spalten UUID, KundenID, Zaehlernummer, Datum, Zaehlernummer_in_mhw und Kommentar.
CREATE TABLE IF NOT EXISTS heizung (
    uuid VARCHAR(255) PRIMARY KEY,
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255) NOT NULL,
    datum DATE NOT NULL,
    zaehlerstand_in_mwh FLOAT NOT NULL,
    kommentar VARCHAR(255)
);
--Erstellt die Tabelle Wasser mit den Spalten UUID, KundenID, Zaehlernummer, Datum, Zaehlernummer_in_m³ und Kommentar.
CREATE TABLE IF NOT EXISTS wasser (
    uuid VARCHAR(255) PRIMARY KEY,
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255) NOT NULL,
    datum DATE NOT NULL,
    zaehlerstand_in_m3 FLOAT NOT NULL,
    kommentar VARCHAR(255)
);
--Erstellt die Tabelle Strom mit den Spalten UUID, KundenID, Zaehlernummer, Datum, Zaehlernummer_in_khw und Kommentar.
CREATE TABLE IF NOT EXISTS strom (
    uuid VARCHAR(255) PRIMARY KEY,
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255) NOT NULL,
    datum DATE NOT NULL,
    zaehlerstand_in_kwh FLOAT NOT NULL,
    kommentar VARCHAR(255)
);
--Erstellt die Tabelle Kunde mit den Spalten UUID, Anrede, Nachname, Vorname und Geburtsdatum.
CREATE TABLE IF NOT EXISTS kunde (
    uuid VARCHAR(255) PRIMARY KEY,
    anrede VARCHAR(1) NOT NULL DEFAULT 'U',
    nachname VARCHAR(255) NOT NULL,
    vorname VARCHAR(255) NOT NULL,
    geburtsdatum DATE
);