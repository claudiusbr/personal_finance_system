
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `test_personal_finance` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `test_personal_finance`;
DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `idcategory` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`idcategory`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Bank'),(3,'groceries'),(2,'hardware');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency` (
  `idcurrency` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`idcurrency`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `currency` WRITE;
/*!40000 ALTER TABLE `currency` DISABLE KEYS */;
INSERT INTO `currency` VALUES (1,'GBP');
/*!40000 ALTER TABLE `currency` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entry` (
  `identry` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `category_id` int(11) NOT NULL,
  `description_id` int(11) NOT NULL,
  `currency_id` int(11) NOT NULL,
  PRIMARY KEY (`identry`),
  KEY `fk_entry_1_idx` (`category_id`),
  KEY `fk_entry_2_idx` (`description_id`),
  KEY `fk_entry_3_idx` (`currency_id`),
  CONSTRAINT `fk_entry_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`idcategory`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_entry_2` FOREIGN KEY (`description_id`) REFERENCES `entry_description` (`id_entry_description`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_entry_3` FOREIGN KEY (`currency_id`) REFERENCES `currency` (`idcurrency`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `entry` WRITE;
/*!40000 ALTER TABLE `entry` DISABLE KEYS */;
INSERT INTO `entry` VALUES (1,-500,1,1,1),(2,500,1,3,1),(3,-600,1,2,1),(4,600,3,2,1),(5,-30,1,3,1),(6,30,1,3,1),(7,-40,1,4,1),(8,40,1,4,1);
/*!40000 ALTER TABLE `entry` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `entry_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entry_description` (
  `id_entry_description` int(11) NOT NULL AUTO_INCREMENT,
  `date_created` datetime NOT NULL,
  `date_recorded` datetime NOT NULL,
  `value` varchar(100) NOT NULL,
  PRIMARY KEY (`id_entry_description`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `entry_description` WRITE;
/*!40000 ALTER TABLE `entry_description` DISABLE KEYS */;
INSERT INTO `entry_description` VALUES (1,'2017-04-21 00:00:00','2017-04-21 00:00:00','a new laptop from PCWorld'),(2,'2018-04-21 00:00:00','2018-04-21 00:00:00','a laptop from some store I found'),(3,'2018-03-10 00:00:00','2018-03-10 00:00:00','weekly shopping from local grocer'),(4,'2018-03-17 00:00:00','2018-03-17 00:00:00','monthly shopping from supermarket');
/*!40000 ALTER TABLE `entry_description` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `pattern`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pattern` (
  `idpattern` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(100) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`idpattern`),
  UNIQUE KEY `value_UNIQUE` (`value`),
  KEY `fk_pattern_1_idx` (`category_id`),
  CONSTRAINT `fk_pattern_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`idcategory`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `pattern` WRITE;
/*!40000 ALTER TABLE `pattern` DISABLE KEYS */;
INSERT INTO `pattern` VALUES (1,'new laptop',2),(2,'laptop from some store',2),(3,'weekly shopping',3),(4,'monthly shopping',3);
/*!40000 ALTER TABLE `pattern` ENABLE KEYS */;
UNLOCK TABLES;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `test_personal_finance` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `test_personal_finance`;
DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `idcategory` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`idcategory`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Bank'),(3,'groceries'),(2,'hardware');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `currency` (
  `idcurrency` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`idcurrency`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `currency` WRITE;
/*!40000 ALTER TABLE `currency` DISABLE KEYS */;
INSERT INTO `currency` VALUES (1,'GBP');
/*!40000 ALTER TABLE `currency` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entry` (
  `identry` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,0) NOT NULL,
  `category_id` int(11) NOT NULL,
  `description_id` int(11) NOT NULL,
  `currency_id` int(11) NOT NULL,
  PRIMARY KEY (`identry`),
  KEY `fk_entry_1_idx` (`category_id`),
  KEY `fk_entry_2_idx` (`description_id`),
  KEY `fk_entry_3_idx` (`currency_id`),
  CONSTRAINT `fk_entry_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`idcategory`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_entry_2` FOREIGN KEY (`description_id`) REFERENCES `entry_description` (`id_entry_description`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_entry_3` FOREIGN KEY (`currency_id`) REFERENCES `currency` (`idcurrency`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `entry` WRITE;
/*!40000 ALTER TABLE `entry` DISABLE KEYS */;
INSERT INTO `entry` VALUES (1,-500,1,1,1),(2,500,1,3,1),(3,-600,1,2,1),(4,600,3,2,1),(5,-30,1,3,1),(6,30,1,3,1),(7,-40,1,4,1),(8,40,1,4,1);
/*!40000 ALTER TABLE `entry` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `entry_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entry_description` (
  `id_entry_description` int(11) NOT NULL AUTO_INCREMENT,
  `date_created` datetime NOT NULL,
  `date_recorded` datetime NOT NULL,
  `value` varchar(100) NOT NULL,
  PRIMARY KEY (`id_entry_description`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `entry_description` WRITE;
/*!40000 ALTER TABLE `entry_description` DISABLE KEYS */;
INSERT INTO `entry_description` VALUES (1,'2017-04-21 00:00:00','2017-04-21 00:00:00','a new laptop from PCWorld'),(2,'2018-04-21 00:00:00','2018-04-21 00:00:00','a laptop from some store I found'),(3,'2018-03-10 00:00:00','2018-03-10 00:00:00','weekly shopping from local grocer'),(4,'2018-03-17 00:00:00','2018-03-17 00:00:00','monthly shopping from supermarket');
/*!40000 ALTER TABLE `entry_description` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `pattern`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pattern` (
  `idpattern` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(100) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`idpattern`),
  UNIQUE KEY `value_UNIQUE` (`value`),
  KEY `fk_pattern_1_idx` (`category_id`),
  CONSTRAINT `fk_pattern_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`idcategory`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `pattern` WRITE;
/*!40000 ALTER TABLE `pattern` DISABLE KEYS */;
INSERT INTO `pattern` VALUES (1,'new laptop',2),(2,'laptop from some store',2),(3,'weekly shopping',3),(4,'monthly shopping',3);
/*!40000 ALTER TABLE `pattern` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

