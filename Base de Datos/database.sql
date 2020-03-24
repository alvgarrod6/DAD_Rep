-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: database
-- ------------------------------------------------------
-- Server version	8.0.18

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
  `iddispositivo` int(11) NOT NULL AUTO_INCREMENT,
  `planta` varchar(45) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `umbralHumedad` int(11) NOT NULL,
  PRIMARY KEY (`iddispositivo`),
  KEY `dispositivo_key_usuario_idx` (`idUsuario`),
  CONSTRAINT `dispositivo_key_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idusuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivo`
--

LOCK TABLES `dispositivo` WRITE;
/*!40000 ALTER TABLE `dispositivo` DISABLE KEYS */;
INSERT INTO `dispositivo` VALUES (1,'Rosa',3,30),(2,'Tulipan',4,20),(3,'tomates',3,50);
/*!40000 ALTER TABLE `dispositivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `humedad`
--

DROP TABLE IF EXISTS `humedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `humedad` (
  `idHumedad` int(11) NOT NULL AUTO_INCREMENT,
  `value` float NOT NULL,
  `timeStamp` bigint(12) NOT NULL,
  `idDispositivo` int(11) NOT NULL,
  `accuracy` float NOT NULL,
  `location` varchar(45) NOT NULL,
  PRIMARY KEY (`idHumedad`),
  KEY `dispositivoFK_idx` (`idDispositivo`),
  CONSTRAINT `humedad_dispositivo` FOREIGN KEY (`idDispositivo`) REFERENCES `dispositivo` (`iddispositivo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `humedad`
--

LOCK TABLES `humedad` WRITE;
/*!40000 ALTER TABLE `humedad` DISABLE KEYS */;
INSERT INTO `humedad` VALUES (1,15,1234578909,1,10,'sala'),(2,15.2,1234578909,1,12,'sala'),(3,16,1234578909,1,10,'sala'),(4,20,1234578909,2,50,'cocina'),(5,22.3,1234578909,3,59,'patio');
/*!40000 ALTER TABLE `humedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riego`
--

DROP TABLE IF EXISTS `riego`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `riego` (
  `idriego` int(11) NOT NULL AUTO_INCREMENT,
  `idusuario` int(11) NOT NULL,
  `iddispositivo` int(11) NOT NULL,
  `idhumedad` int(11) NOT NULL,
  `timestamp` bigint(12) NOT NULL,
  PRIMARY KEY (`idriego`),
  KEY `riego_usuario_idx` (`idusuario`),
  KEY `riego_dispositivo_idx` (`iddispositivo`),
  KEY `riego_humedad_idx` (`idhumedad`),
  CONSTRAINT `riego_dispositivo` FOREIGN KEY (`iddispositivo`) REFERENCES `dispositivo` (`iddispositivo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `riego_humedad` FOREIGN KEY (`idhumedad`) REFERENCES `humedad` (`idHumedad`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `riego_usuario` FOREIGN KEY (`idusuario`) REFERENCES `usuario` (`idusuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riego`
--

LOCK TABLES `riego` WRITE;
/*!40000 ALTER TABLE `riego` DISABLE KEYS */;
INSERT INTO `riego` VALUES (1,4,1,4,1234578909);
/*!40000 ALTER TABLE `riego` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idusuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `contraseña` int(11) NOT NULL,
  PRIMARY KEY (`idusuario`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (3,'marcos','marcos@us.es',180897),(4,'Alvaro','alvaro@us.es',123456),(5,'luismi','luismi@us.es',123456);
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

-- Dump completed on 2020-03-24 19:17:10
