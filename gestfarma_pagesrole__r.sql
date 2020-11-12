-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gestfarma
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `pagesrole__r`
--

DROP TABLE IF EXISTS `pagesrole__r`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagesrole__r` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `IdPage` int DEFAULT NULL,
  `IdRole` int DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `role_idx` (`IdRole`),
  KEY `Page_idx` (`IdPage`),
  CONSTRAINT `Page` FOREIGN KEY (`IdPage`) REFERENCES `pages` (`Id`),
  CONSTRAINT `role` FOREIGN KEY (`IdRole`) REFERENCES `role` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagesrole__r`
--

LOCK TABLES `pagesrole__r` WRITE;
/*!40000 ALTER TABLE `pagesrole__r` DISABLE KEYS */;
INSERT INTO `pagesrole__r` VALUES (22,3,1),(23,3,2),(26,4,1),(27,4,2),(28,4,3),(29,4,4),(30,1,1),(31,1,2),(32,5,2),(33,5,3),(34,5,4),(35,6,2),(36,6,3),(37,6,4),(38,2,2),(39,2,3);
/*!40000 ALTER TABLE `pagesrole__r` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-12 23:01:46
