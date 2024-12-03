--Erstellt die Tabelle Heizung mit den Spalten KundenID, Zaehlernummer, Datum, Zaehlernummer_in_mhw und Kommentar.
CREATE TABLE IF NOT EXISTS heizung (
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255),
    datum DATE,
    zaehlerstand_in_mwh FLOAT,
    kommentar VARCHAR(255)
);
--Erstellt die Tabelle Wasser mit den Spalten KundenID, Zaehlernummer, Datum, Zaehlernummer_in_m³ und Kommentar.
CREATE TABLE IF NOT EXISTS wasser (
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255),
    datum DATE,
    zaehlerstand_in_m³ FLOAT,
    kommentar VARCHAR(255)
);
--Erstellt die Tabelle Strom mit den Spalten KundenID, Zaehlernummer, Datum, Zaehlernummer_in_khw und Kommentar.
CREATE TABLE IF NOT EXISTS strom (
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255),
    datum DATE,
    zaehlerstand_in_kwh FLOAT,
    kommentar VARCHAR(255)
);
--Erstellt die Tabelle Kunde mit den Spalten UUID, Anrede, Nachname, Vorname und Geburtsdatum.
CREATE TABLE IF NOT EXISTS kunde (
    uuid VARCHAR(255),
    anrede VARCHAR(1),
    nachname VARCHAR(255),
    vorname VARCHAR(255),
    geburtsdatum DATE
);