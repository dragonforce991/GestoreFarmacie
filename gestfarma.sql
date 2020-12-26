-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: gestfarma
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
-- Table structure for table `acquisti`
--

DROP TABLE IF EXISTS `acquisti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acquisti` (
  `idAcquisti` int NOT NULL AUTO_INCREMENT,
  `IdPaziente` int DEFAULT NULL,
  `IdUser` int NOT NULL,
  `Totale` decimal(10,0) DEFAULT NULL,
  `DataAcquisto` date DEFAULT NULL,
  `IdFarmacia` int DEFAULT NULL,
  PRIMARY KEY (`idAcquisti`),
  KEY `Operatore_idx` (`IdUser`),
  KEY `TracciaFarmacia_idx` (`IdFarmacia`),
  KEY `TracciaPaziente_idx` (`IdPaziente`),
  CONSTRAINT `TracciaFarmacia` FOREIGN KEY (`IdFarmacia`) REFERENCES `farmacia` (`Id`),
  CONSTRAINT `TracciaOperatore` FOREIGN KEY (`IdUser`) REFERENCES `user` (`Id`),
  CONSTRAINT `TracciaPaziente` FOREIGN KEY (`IdPaziente`) REFERENCES `pazienti` (`idPazienti`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acquisti`
--

LOCK TABLES `acquisti` WRITE;
/*!40000 ALTER TABLE `acquisti` DISABLE KEYS */;
INSERT INTO `acquisti` VALUES (4,2,1,115,NULL,2),(6,2,1,115,NULL,6),(7,2,1,115,NULL,2);
/*!40000 ALTER TABLE `acquisti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acquistiprodotti__r`
--

DROP TABLE IF EXISTS `acquistiprodotti__r`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acquistiprodotti__r` (
  `idAcquistiProdotti__r` int NOT NULL AUTO_INCREMENT,
  `IdAcquisto` int DEFAULT NULL,
  `IdProdotto` int DEFAULT NULL,
  `quantita` int DEFAULT NULL,
  `DataRicetta` date DEFAULT NULL,
  `CodiceRegionale` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idAcquistiProdotti__r`),
  KEY `AcquistoProdotto_idx` (`IdProdotto`),
  KEY `Acquisto_idx` (`IdAcquisto`),
  CONSTRAINT `Acquisto` FOREIGN KEY (`IdAcquisto`) REFERENCES `acquisti` (`idAcquisti`),
  CONSTRAINT `AcquistoProdotto` FOREIGN KEY (`IdProdotto`) REFERENCES `prodotti` (`idProdotto`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acquistiprodotti__r`
--

LOCK TABLES `acquistiprodotti__r` WRITE;
/*!40000 ALTER TABLE `acquistiprodotti__r` DISABLE KEYS */;
INSERT INTO `acquistiprodotti__r` VALUES (1,4,1,1,NULL,NULL),(2,4,3,3,NULL,NULL),(3,6,1,1,NULL,NULL),(4,6,3,3,NULL,NULL),(5,7,1,1,NULL,NULL),(6,7,3,3,NULL,NULL);
/*!40000 ALTER TABLE `acquistiprodotti__r` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `farmacia`
--

DROP TABLE IF EXISTS `farmacia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmacia` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(45) DEFAULT NULL,
  `Indirizzo` varchar(45) DEFAULT NULL,
  `Telefono` varchar(45) DEFAULT NULL,
  `Titolare` int DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `Titolare_Farma_idx` (`Titolare`),
  CONSTRAINT `Titolare_Farma` FOREIGN KEY (`Titolare`) REFERENCES `user` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `farmacia`
--

LOCK TABLES `farmacia` WRITE;
/*!40000 ALTER TABLE `farmacia` DISABLE KEYS */;
INSERT INTO `farmacia` VALUES (2,'Farmacia1','Via indirizzo 1, Milano','02 12345670',1),(6,'Farmacia Prova Servizio','via indirizzo buono','3450129837',7),(7,'Farmacia stazione','via indirizzo civico','345090156457',9);
/*!40000 ALTER TABLE `farmacia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magazzino`
--

DROP TABLE IF EXISTS `magazzino`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magazzino` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `IdProdotto` int DEFAULT NULL,
  `IdFarmacia` int DEFAULT NULL,
  `quantita` int DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UniqueFarmaciaProdotto` (`IdProdotto`,`IdFarmacia`),
  KEY `Prodotto_idx` (`IdProdotto`),
  KEY `Farmacia_idx` (`IdFarmacia`),
  KEY `Farmacia_index` (`IdFarmacia`),
  CONSTRAINT `Farmacia__r` FOREIGN KEY (`IdFarmacia`) REFERENCES `farmacia` (`Id`),
  CONSTRAINT `Prodotto` FOREIGN KEY (`IdProdotto`) REFERENCES `prodotti` (`idProdotto`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magazzino`
--

LOCK TABLES `magazzino` WRITE;
/*!40000 ALTER TABLE `magazzino` DISABLE KEYS */;
INSERT INTO `magazzino` VALUES (14,1,2,21),(15,2,2,30),(16,3,2,59);
/*!40000 ALTER TABLE `magazzino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pages`
--

DROP TABLE IF EXISTS `pages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pages` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Url` varchar(45) DEFAULT NULL,
  `Icon` varchar(45) DEFAULT NULL,
  `Name` varchar(45) DEFAULT NULL,
  `Title` varchar(45) DEFAULT NULL,
  `canCreate` tinyint DEFAULT NULL,
  `canDelete` tinyint DEFAULT NULL,
  `canModify` tinyint DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pages`
--

LOCK TABLES `pages` WRITE;
/*!40000 ALTER TABLE `pages` DISABLE KEYS */;
INSERT INTO `pages` VALUES (1,'/dashboard/users','mdi-factory','Users','Utenti',1,1,1),(2,'/dashboard/farmacia','mdi-factory','Gestione','Gestione',1,1,1),(3,'/dashboard/','mdi-factory','Home','Home',1,1,1),(4,'/dashboard/chat','mdi-factory','Chat','Chat',1,1,1);
/*!40000 ALTER TABLE `pages` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagesrole__r`
--

LOCK TABLES `pagesrole__r` WRITE;
/*!40000 ALTER TABLE `pagesrole__r` DISABLE KEYS */;
INSERT INTO `pagesrole__r` VALUES (1,1,1),(2,2,1),(3,3,1),(4,1,2),(5,2,2),(6,3,2),(7,4,2);
/*!40000 ALTER TABLE `pagesrole__r` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pazienti`
--

DROP TABLE IF EXISTS `pazienti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pazienti` (
  `idPazienti` int NOT NULL AUTO_INCREMENT,
  `Codice_Fiscale` varchar(45) NOT NULL,
  `Nome` varchar(45) DEFAULT NULL,
  `Cognome` varchar(45) DEFAULT NULL,
  `DataDiNascita` date DEFAULT NULL,
  `OperatoreRegistrazione` int DEFAULT NULL,
  `Telefono` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idPazienti`),
  UNIQUE KEY `Codice_Fiscale_UNIQUE` (`Codice_Fiscale`),
  KEY `Operatore_idx` (`OperatoreRegistrazione`),
  CONSTRAINT `Operatore` FOREIGN KEY (`OperatoreRegistrazione`) REFERENCES `user` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pazienti`
--

LOCK TABLES `pazienti` WRITE;
/*!40000 ALTER TABLE `pazienti` DISABLE KEYS */;
INSERT INTO `pazienti` VALUES (2,'DNGDVD99E02F158U','DAVIDE','DANGELO',NULL,0,'3450701614'),(4,'tu1gle99e02f158u','TU','EGLI',NULL,1,'789456123');
/*!40000 ALTER TABLE `pazienti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotti`
--

DROP TABLE IF EXISTS `prodotti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodotti` (
  `idProdotto` int NOT NULL AUTO_INCREMENT,
  `ObbligoRicetta` tinyint NOT NULL,
  `CostoUnitario` double NOT NULL,
  `Nome` varchar(45) NOT NULL,
  `Azienda` varchar(45) DEFAULT NULL,
  `Descizione` varchar(45) DEFAULT NULL,
  `Codice` varchar(45) NOT NULL,
  `ParoleChiave` mediumtext,
  PRIMARY KEY (`idProdotto`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotti`
--

LOCK TABLES `prodotti` WRITE;
/*!40000 ALTER TABLE `prodotti` DISABLE KEYS */;
INSERT INTO `prodotti` VALUES (1,0,10,'Farmaco1','AZIENDA','Farmaco per le cure','123456789','Farmaco-per-le-cure-ibuprofene'),(2,1,35,'provaInsert','provaAzienda','provaDescrizione','789465132','a-b'),(3,0,35,'provaInsert','provaAzienda','provaDescrizione','789465132','a-b');
/*!40000 ALTER TABLE `prodotti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `Id` int NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `icon` varchar(45) DEFAULT NULL,
  `Default_Path` varchar(45) DEFAULT NULL,
  `ricettaEnabled` tinyint DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Regione','mdi-account-tie','/dashboard/users',0),(2,'Titolare_Farmacia','mdi-default','/dashboard/',1),(3,'Operatore_Banco','mdi-default','/dashboard/',0),(4,'Dottore_Farmacista','mdi-default','/dashboard/',1);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `Phone_Number` varchar(45) DEFAULT NULL,
  `Role` int DEFAULT NULL,
  `Password` varchar(45) DEFAULT NULL,
  `Farmacia` int DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `Role_idx` (`Role`),
  KEY `Role_index` (`Role`),
  KEY `Farmacia_idx` (`Farmacia`),
  CONSTRAINT `Farmacia` FOREIGN KEY (`Farmacia`) REFERENCES `farmacia` (`Id`),
  CONSTRAINT `User_Role` FOREIGN KEY (`Role`) REFERENCES `role` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (0,'Regione','Lombardia','regione.lombardia@gmail.com','02 1234567',1,'123456',NULL),(1,'Prova','TF','titolare.farmacia@prova.it','333 333333333',2,'123456',2),(7,'Prova','InsertFarma','provainsertFarma@prova.it','02 78945613',2,'123455',6),(8,'Prova','DottoreFarmacista','provaDottoreFarmacista@prova.it','02 123456789',4,'12345',2),(9,'Utente','Titolare','utenteTitolare@prova.it','02 123456789',2,'123455',7);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-07 15:31:22
