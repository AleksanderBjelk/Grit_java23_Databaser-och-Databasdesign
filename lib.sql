-- MariaDB dump 10.19-11.2.2-MariaDB, for osx10.19 (arm64)
--
-- Host: localhost    Database: Lib
-- ------------------------------------------------------
-- Server version	11.2.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `BorrowedMedia`
--

DROP TABLE IF EXISTS `BorrowedMedia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BorrowedMedia` (
  `Borrowed_id` int(11) NOT NULL AUTO_INCREMENT,
  `Media_id` int(11) DEFAULT NULL,
  `Users_id` int(11) DEFAULT NULL,
  `Borrow_Date` date DEFAULT NULL,
  `Return_Date` date DEFAULT NULL,
  `Status` enum('Active','Returned') NOT NULL DEFAULT 'Active',
  PRIMARY KEY (`Borrowed_id`),
  KEY `Media_id` (`Media_id`),
  KEY `Users_id` (`Users_id`),
  CONSTRAINT `borrowedmedia_ibfk_1` FOREIGN KEY (`Media_id`) REFERENCES `Media` (`Media_id`),
  CONSTRAINT `borrowedmedia_ibfk_2` FOREIGN KEY (`Users_id`) REFERENCES `Users` (`Users_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BorrowedMedia`
--

LOCK TABLES `BorrowedMedia` WRITE;
/*!40000 ALTER TABLE `BorrowedMedia` DISABLE KEYS */;
INSERT INTO `BorrowedMedia` VALUES
(1,10,1,'2024-01-19','2024-02-18','Active'),
(2,9,1,'2024-01-19','2024-02-18','Active'),
(3,11,2,'2024-01-19','2024-01-29','Active'),
(4,5,2,'2024-01-19','2024-01-19','Returned'),
(5,7,2,'2024-01-19','2024-02-18','Active'),
(6,6,2,'2024-01-19','2024-02-18','Active');
/*!40000 ALTER TABLE `BorrowedMedia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Media`
--

DROP TABLE IF EXISTS `Media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Media` (
  `Media_id` int(11) NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) NOT NULL,
  `Creator` varchar(255) NOT NULL,
  `Published` date NOT NULL,
  `Genre` enum('Horror','Adventure','Fact','Science Fiction','Mystery','Young Adult','Romance','Dystopian','Thriller','Rock') NOT NULL,
  `Media` enum('Book','Record') NOT NULL,
  `Available_Copies` enum('YES','NO') NOT NULL DEFAULT 'YES',
  PRIMARY KEY (`Media_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Media`
--

LOCK TABLES `Media` WRITE;
/*!40000 ALTER TABLE `Media` DISABLE KEYS */;
INSERT INTO `Media` VALUES
(1,'Harry Potter and the midgets','JK Standing','2008-03-09','Horror','Book','YES'),
(2,'The not so great Gatsby','F, Scott','1922-08-02','Adventure','Book','YES'),
(3,'Frankenstein','Frankensteins monster','1818-07-23','Fact','Book','YES'),
(4,'The Ilijad','Homer Simpson','1598-12-03','Science Fiction','Book','YES'),
(5,'The Odyssey','Homer Simpson','1592-11-20','Mystery','Book','YES'),
(6,'Pippi Kortstrump','Astrid Lindblad','1945-11-26','Young Adult','Book','NO'),
(7,'Systrarna tigerhjärta','Astrid Lindblad','1973-04-05','Romance','Book','NO'),
(8,'Lotta på snällmakargatan','Astrid Lindblad','1968-07-26','Dystopian','Book','YES'),
(9,'The rights in our stars','John Red','2012-01-10','Thriller','Book','NO'),
(10,'Jag är inte Zlatan Ibrahimovic','Slatan Ibrahimovic','2011-07-31','Romance','Book','NO'),
(11,'Evershort','Foo Wrestlers','2009-12-10','Rock','Record','NO'),
(12,'Bad day','U3','1980-03-20','Rock','Record','YES'),
(13,'Eight Nation Army','The Black Stripes','2003-06-23','Rock','Record','YES');
/*!40000 ALTER TABLE `Media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Reservations`
--

DROP TABLE IF EXISTS `Reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Reservations` (
  `Reservation_id` int(11) NOT NULL AUTO_INCREMENT,
  `Media_id` int(11) DEFAULT NULL,
  `Users_id` int(11) DEFAULT NULL,
  `Reservation_Date` date DEFAULT NULL,
  `Expiration_Date` date DEFAULT NULL,
  PRIMARY KEY (`Reservation_id`),
  KEY `Media_id` (`Media_id`),
  KEY `Users_id` (`Users_id`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`Media_id`) REFERENCES `Media` (`Media_id`),
  CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`Users_id`) REFERENCES `Users` (`Users_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Reservations`
--

LOCK TABLES `Reservations` WRITE;
/*!40000 ALTER TABLE `Reservations` DISABLE KEYS */;
INSERT INTO `Reservations` VALUES
(1,9,2,'2024-01-19','2024-03-19');
/*!40000 ALTER TABLE `Reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `Users_id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Created` date NOT NULL,
  PRIMARY KEY (`Users_id`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES
(1,'Martin','2a6bd0e90270f155c3244c24ba1004149cfafc09a01d38a9051efac5ef79076141613d4a764204677d38c7830a7cf3e7','martin@gail.com','2024-01-19'),
(2,'Aleks','e1a1becd61e5ac156b009f239e88ab05d0156b7fc40e50def8e857a6cc477a214bb665e5a6b1a6e7a35f22e3d9068821','aleks@gmail.com','2024-01-19');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-19 10:55:44
