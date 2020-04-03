-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: riegoauto
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `dispositivo`
--

DROP TABLE IF EXISTS `dispositivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispositivo` (
  `iddispositivo` int NOT NULL,
  `ip` varchar(45) DEFAULT NULL,
  `idusuario` int NOT NULL,
  `initialTimestamp` bigint NOT NULL,
  PRIMARY KEY (`iddispositivo`),
  KEY `dispositivo_usuario_idx` (`idusuario`),
  CONSTRAINT `dispositivo_usuario` FOREIGN KEY (`idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo`
--

LOCK TABLES `dispositivo` WRITE;
/*!40000 ALTER TABLE `dispositivo` DISABLE KEYS */;
INSERT INTO `dispositivo` VALUES (82664,'192.168.0.0',1,5442132132),(82665,'192.168.0.1',2,56432132132);
/*!40000 ALTER TABLE `dispositivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riego`
--

DROP TABLE IF EXISTS `riego`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `riego` (
  `idriego` int NOT NULL AUTO_INCREMENT,
  `timestamp` bigint NOT NULL,
  `humedadRiego` int NOT NULL,
  `autoManual` tinyint NOT NULL,
  `idsensor` int NOT NULL,
  PRIMARY KEY (`idriego`),
  KEY `riego_sensor_idx` (`idsensor`),
  CONSTRAINT `riego_sensor` FOREIGN KEY (`idsensor`) REFERENCES `sensor` (`idsensor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riego`
--

LOCK TABLES `riego` WRITE;
/*!40000 ALTER TABLE `riego` DISABLE KEYS */;
INSERT INTO `riego` VALUES (1,132156456,720,1,2),(2,531321321,700,0,1);
/*!40000 ALTER TABLE `riego` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor`
--

DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor` (
  `idsensor` int NOT NULL AUTO_INCREMENT,
  `iddisp` int NOT NULL,
  `planta` varchar(45) NOT NULL,
  `umbral` int NOT NULL,
  `potencia` int NOT NULL,
  PRIMARY KEY (`idsensor`),
  KEY `sensor_dispositivo_idx` (`iddisp`),
  CONSTRAINT `sensor_dispositivo` FOREIGN KEY (`iddisp`) REFERENCES `dispositivo` (`iddispositivo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor`
--

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` VALUES (1,82664,'Tulipan',500,45),(2,82665,'Girasol',700,23);
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_value`
--

DROP TABLE IF EXISTS `sensor_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor_value` (
  `idsensor_value` int NOT NULL AUTO_INCREMENT,
  `idsensor` int NOT NULL,
  `value` float NOT NULL,
  `accuracy` float NOT NULL,
  `timestamp` bigint NOT NULL,
  PRIMARY KEY (`idsensor_value`),
  KEY `sensor_value_sensor_idx` (`idsensor`),
  CONSTRAINT `sensor_value_sensor` FOREIGN KEY (`idsensor`) REFERENCES `sensor` (`idsensor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_value`
--

LOCK TABLES `sensor_value` WRITE;
/*!40000 ALTER TABLE `sensor_value` DISABLE KEYS */;
INSERT INTO `sensor_value` VALUES (4,1,700,1,16515151),(5,1,720,3,1321651),(6,2,530,1,1132121),(7,1,750,2,13546),(8,1,350,1,15165),(10,1,820,2,5412312);
/*!40000 ALTER TABLE `sensor_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idusuario` int NOT NULL AUTO_INCREMENT,
  `user` varchar(45) NOT NULL,
  `pass` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `dni` varchar(45) DEFAULT NULL,
  ` birthdate` bigint DEFAULT NULL,
  PRIMARY KEY (`idusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'alvgarrod6','root','Alvaro','Garcia','00000000T',132124325354),(2,'hdsgfh778','root','Manolo','Sanchez','87987897g',87987897);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-03 12:05:27
