--Erstellt die Tabelle Heizung mit den Spalten KundenID, Zaehlernummer, Datum, Zaehlernummer_in_mhw und Kommentar.
CREATE TABLE IF NOT EXISTS heizung (
    kundenid VARCHAR(255) NOT NULL,
    zaehlernummer VARCHAR(255) NOT NULL UNIQUE,
    datum DATE NOT NULL,
    zaehlerstand_in_mwh FLOAT NOT NULL,
    kommentar VARCHAR(255) NOT NULL
);
--Erstellt die Tabelle Wasser mit den Spalten KundenID, Zaehlernummer, Datum, Zaehlernummer_in_m³ und Kommentar.
CREATE TABLE IF NOT EXISTS wasser (
    kundenid VARCHAR(255) NOT NULL,
    zaehlernummer VARCHAR(255) NOT NULL UNIQUE,
    datum DATE NOT NULL,
    zaehlerstand_in_m3 FLOAT NOT NULL,
    kommentar VARCHAR(255) NOT NULL
);
--Erstellt die Tabelle Strom mit den Spalten KundenID, Zaehlernummer, Datum, Zaehlernummer_in_khw und Kommentar.
CREATE TABLE IF NOT EXISTS strom (
    kundenid VARCHAR(255) NOT NULL,
    zaehlernummer VARCHAR(255) NOT NULL UNIQUE,
    datum DATE NOT NULL,
    zaehlerstand_in_kwh FLOAT NOT NULL,
    kommentar VARCHAR(255) NOT NULL
);
--Erstellt die Tabelle Kunde mit den Spalten UUID, Anrede, Nachname, Vorname und Geburtsdatum.
CREATE TABLE IF NOT EXISTS kunde (
    uuid VARCHAR(255) NOT NULL UNIQUE,
    anrede VARCHAR(1) NOT NULL,
    nachname VARCHAR(255) NOT NULL,
    vorname VARCHAR(255) NOT NULL,
    geburtsdatum DATE NOT NULL
);