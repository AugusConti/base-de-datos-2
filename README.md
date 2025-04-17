# base-de-datos-2
Trabajo práctico de la materia Base de Datos 2

Para crear la base de datos y el usuario:

CREATE USER 'bd2'@'localhost' IDENTIFIED BY 'asdasd';

CREATE DATABASE bd2_tours_21;

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP ON bd2_tours_21.* TO 'bd2'@'localhost';

FLUSH PRIVILEGES;

Para correr los tests, ejecutar en la raiz del proyecto:

mvn clean install
