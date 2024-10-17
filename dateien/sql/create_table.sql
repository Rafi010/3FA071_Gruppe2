CREATE TABLE IF NOT EXISTS heizung (
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255),
    datum DATE,
    zaehlerstand FLOAT
);

CREATE TABLE IF NOT EXISTS wasser (
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255),
    datum DATE,
    zaehlerstand FLOAT
);

CREATE TABLE IF NOT EXISTS strom (
    kundenid VARCHAR(255),
    zaehlernummer VARCHAR(255),
    datum DATE,
    zaehlerstand FLOAT
);

CREATE TABLE IF NOT EXISTS kunde (
    uuid VARCHAR(255),
    anrede VARCHAR(255),
    nachname VARCHAR(255),
    vorname VARCHAR(255),
    geburtsdatum DATE
);
CREATE TABLE IF NOT EXISTS test_kunde (
    uuid VARCHAR(255),
    anrede VARCHAR(255),
    nachname VARCHAR(255),
    vorname VARCHAR(255),
    geburtsdatum DATE
);