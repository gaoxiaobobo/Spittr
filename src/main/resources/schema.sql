CREATE DATABASE spittr DEFAULT CHARACTER SET 'utf8' DEFAULT COLLATE 'utf8_unicode_ci';
GRANT ALL ON spittr.* TO 'springAction'@'localhost';

USE spittr;

CREATE TABLE Spitter (
  SpitterId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Revision BIGINT UNSIGNED NOT NULL,
  CreatedDate TIMESTAMP NULL,
  LastModifiedDate TIMESTAMP NULL,
  Username VARCHAR(30) NOT NULL UNIQUE,
  Password VARCHAR(100) NOT NULL,
  FullName VARCHAR(100) NOT NULL,
  Role VARCHAR(30) NULL,
  Email VARCHAR(50) NOT NULL UNIQUE,
  UpdateByEmail BOOLEAN NOT NULL
  ) ENGINE = InnoDB;

CREATE TABLE Spittle (
  SpittleId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Revision BIGINT UNSIGNED NOT NULL,
  CreatedDate TIMESTAMP NULL,
  LastModifiedDate TIMESTAMP NULL,
  CreatedBy VARCHAR(100) NULL,
  UpdatedBy VARCHAR(100) NULL,
  SpitterId BIGINT UNSIGNED NOT NULL,
  Message VARCHAR(2000) NOT NULL,      
  CONSTRAINT Spittle_Spitter FOREIGN KEY (SpitterId) REFERENCES Spitter(SpitterId) ON DELETE CASCADE
  ) ENGINE = InnoDB;