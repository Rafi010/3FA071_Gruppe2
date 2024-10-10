# create db hv, user hv - kein PW
USE mysql;

DROP DATABASE IF EXISTS hv;
CREATE DATABASE hv CHARACTER SET utf8;

DROP USER IF EXISTS 'hv'@'localhost';
CREATE USER 'hv'@'localhost' IDENTIFIED BY '';

GRANT ALL PRIVILEGES ON hv.* TO 'hv'@'localhost';
FLUSH PRIVILEGES;
