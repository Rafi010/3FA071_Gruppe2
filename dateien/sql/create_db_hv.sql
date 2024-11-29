--Der Befehl erstellt die Datenbank hv mit UTF-8-Zeichensatz, legt einen Benutzer hv ohne Passwort an, entfernt vorhandene Datenbank und Benutzer und gewährt dem Benutzer vollständige Rechte auf die Datenbank.
# create db hv, user hv - kein PW
USE mysql;

DROP DATABASE IF EXISTS hv;
CREATE DATABASE hv CHARACTER SET utf8;

DROP USER IF EXISTS 'hv'@'localhost';
CREATE USER 'hv'@'localhost' IDENTIFIED BY '';

GRANT ALL PRIVILEGES ON hv.* TO 'hv'@'localhost';
FLUSH PRIVILEGES;
