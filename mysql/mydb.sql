-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_data_source`
--

DROP TABLE IF EXISTS `t_data_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_data_source` (
  `name` varchar(50) NOT NULL,
  `path` varchar(128) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_data_source`
--

LOCK TABLES `t_data_source` WRITE;
/*!40000 ALTER TABLE `t_data_source` DISABLE KEYS */;
INSERT INTO `t_data_source` VALUES ('CaseWesternReserveUniversityData','D:/1hongquan/graduation_life/graduate_design/data/AllCode/PythonTrain/data/CaseWesternReserveUniversityData/',1);
/*!40000 ALTER TABLE `t_data_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_device_download`
--

DROP TABLE IF EXISTS `t_device_download`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_device_download` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `device_id` int(10) DEFAULT NULL,
  `model_over_id` int(10) DEFAULT NULL,
  `submit_date` datetime DEFAULT NULL,
  `download_date` datetime DEFAULT NULL,
  `user_id` int(10) DEFAULT NULL,
  `status` varchar(3) DEFAULT 'No',
  `task_name` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_device_download`
--

LOCK TABLES `t_device_download` WRITE;
/*!40000 ALTER TABLE `t_device_download` DISABLE KEYS */;
INSERT INTO `t_device_download` VALUES (39,1,146,'2021-04-25 17:47:41','2021-04-25 17:49:04',26,'Yes','C'),(40,1,146,'2021-04-25 17:51:10','2021-04-25 17:51:16',26,'Yes','C'),(41,1,153,'2021-04-25 17:52:05','2021-04-25 17:52:12',26,'Yes','G');
/*!40000 ALTER TABLE `t_device_download` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_device_info`
--

DROP TABLE IF EXISTS `t_device_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_device_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `train_number` varchar(10) DEFAULT NULL,
  `vehicle_number` int(10) DEFAULT NULL,
  `bogie` int(10) DEFAULT NULL,
  `version` varchar(10) DEFAULT NULL,
  `status_of_device` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_device_info`
--

LOCK TABLES `t_device_info` WRITE;
/*!40000 ALTER TABLE `t_device_info` DISABLE KEYS */;
INSERT INTO `t_device_info` VALUES (1,'G520',12345,1,'1.0.1',1);
/*!40000 ALTER TABLE `t_device_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_model_name`
--

DROP TABLE IF EXISTS `t_model_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_model_name` (
  `name` varchar(30) NOT NULL,
  `path` varchar(100) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_model_name`
--

LOCK TABLES `t_model_name` WRITE;
/*!40000 ALTER TABLE `t_model_name` DISABLE KEYS */;
INSERT INTO `t_model_name` VALUES ('1','../resource/model/1.svg',44),('F','../resource/model/F.svg',45);
/*!40000 ALTER TABLE `t_model_name` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_signal`
--

DROP TABLE IF EXISTS `t_signal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_signal` (
  `train_number` varchar(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `vehicle_number` int(10) DEFAULT NULL,
  `bogie` int(10) DEFAULT NULL,
  `axle` int(10) DEFAULT NULL,
  `path_of_vibrate` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_signal`
--

LOCK TABLES `t_signal` WRITE;
/*!40000 ALTER TABLE `t_signal` DISABLE KEYS */;
INSERT INTO `t_signal` VALUES ('G520','2023-04-25',12345,1,0,'2023_04_25_20_44_11',978),('G520','2023-04-25',12345,1,0,'2023_04_25_23_25_53',979),('G520','2024-04-26',12345,1,0,'2024_04_26_00_06_20',980),('G520','2023-04-25',12345,1,0,'2023_04_25_21_24_38',981),('G520','2023-04-25',12345,1,0,'2023_04_25_22_05_04',982),('G520','2023-04-25',12345,1,0,'2023_04_25_23_46_06',983),('G520','2023-04-25',12345,1,0,'2023_04_25_22_25_20',984),('G520','2023-04-25',12345,1,0,'2023_04_25_21_44_52',985),('G520','2023-04-25',12345,1,0,'2023_04_25_23_05_43',986),('G520','2023-04-25',12345,1,0,'2023_04_25_21_04_24',987),('G520','2023-04-25',12345,1,0,'2023_04_25_22_45_32',988),('G520','2024-04-26',12345,1,0,'2024_04_26_09_42_59',989),('G520','2024-04-26',12345,1,0,'2024_04_26_09_43_07',990),('G520','2024-04-26',12345,1,0,'2024_04_26_09_43_11',991),('G520','2024-04-26',12345,1,0,'2024_04_26_09_42_56',992),('G520','2024-04-26',12345,1,0,'2024_04_26_09_43_03',993),('G520','2024-04-26',12345,1,0,'2024_04_26_09_44_42',994),('G520','2024-04-26',12345,1,0,'2024_04_26_09_44_45',995),('G520','2024-04-26',12345,1,0,'2024_04_26_09_54_15',996),('G520','2024-04-26',12345,1,0,'2024_04_26_09_54_26',997),('G520','2024-04-26',12345,1,0,'2024_04_26_09_54_39',998),('G520','2024-04-26',12345,1,0,'2024_04_26_09_55_35',999),('G520','2024-04-26',12345,1,0,'2024_04_26_09_55_14',1000),('G520','2024-04-26',12345,1,0,'2024_04_26_09_56_49',1001),('G520','2024-04-26',12345,1,0,'2024_04_26_09_58_51',1002);
/*!40000 ALTER TABLE `t_signal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_task_info`
--

DROP TABLE IF EXISTS `t_task_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_task_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `model_name` varchar(30) DEFAULT NULL,
  `source_name` varchar(100) DEFAULT NULL,
  `pre_parameter` varchar(10) DEFAULT NULL,
  `task_name` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `parameter` varchar(10) DEFAULT NULL,
  `status` varchar(8) DEFAULT 'ready',
  `process` int(11) DEFAULT '0',
  `loss_function` varchar(30) DEFAULT NULL,
  `learn_rate_update_strategy` varchar(20) DEFAULT NULL,
  `learn_rate` float DEFAULT NULL,
  `batch_size` int(11) DEFAULT NULL,
  `iter_num` int(11) DEFAULT NULL,
  `test_acc` float DEFAULT NULL,
  `metric` varchar(30) DEFAULT NULL,
  `momentum` float DEFAULT NULL,
  `accumulator` float DEFAULT NULL,
  `beta_1` float DEFAULT NULL,
  `beta_2` float DEFAULT NULL,
  `epsilon` float DEFAULT NULL,
  `rho` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_task_info`
--

LOCK TABLES `t_task_info` WRITE;
/*!40000 ALTER TABLE `t_task_info` DISABLE KEYS */;
INSERT INTO `t_task_info` VALUES (144,'1','CaseWesternReserveUniversityData','none','A','2021-04-16 02:11:45',NULL,'running',0,'categorical_crossentropy','adam',0.001,50,50,0.57,'Accuracy',0,0.1,0.9,0.999,0.0000001,0.95),(145,'1','CaseWesternReserveUniversityData','none','B','2021-04-16 02:12:04',NULL,'success',100,'categorical_crossentropy','adam',0.001,50,40,2.72,'Accuracy',0,0.1,0.9,0.999,0.0000001,0.95),(146,'1','CaseWesternReserveUniversityData','none','C','2021-04-16 02:12:15',NULL,'success',100,'categorical_crossentropy','adam',0.001,50,60,2.56,'Accuracy',0,0.1,0.9,0.999,0.0000001,0.95),(147,'1','CaseWesternReserveUniversityData','none','D','2021-04-16 02:42:36',NULL,'success',100,'categorical_crossentropy','adam',0.001,50,50,63.78,'CategoricalAccuracy',0,0.1,0.9,0.999,0.0000001,0.95),(148,'1','CaseWesternReserveUniversityData',NULL,'F','2021-04-16 02:58:20',NULL,'success',100,'categorical_crossentropy','adam',0.001,50,5,50.55,'CategoricalAccuracy',0,0.1,0.9,0.999,0.0000001,0.95),(149,'1','CaseWesternReserveUniversityData','none','H','2021-04-16 05:54:28',NULL,'ready',0,'categorical_crossentropy','adam',0.001,50,100,NULL,'CategoricalAccuracy',0,0.1,0.9,0.999,0.0000001,0.95),(150,'1','CaseWesternReserveUniversityData','none','I','2021-04-16 05:55:06',NULL,'ready',0,'categorical_crossentropy','adam',0.001,50,100,NULL,'CategoricalAccuracy',0,0.1,0.9,0.999,0.0000001,0.95),(151,'1','CaseWesternReserveUniversityData',NULL,'K','2021-04-16 05:58:08',NULL,'ready',0,'categorical_crossentropy','adam',0.001,50,100,NULL,'CategoricalAccuracy',0,0.1,0.9,0.999,0.0000001,0.95),(152,'1','CaseWesternReserveUniversityData','none','L','2021-04-16 06:01:32',NULL,'ready',0,'categorical_crossentropy','adam',0.001,50,100,NULL,'CategoricalAccuracy',0,0.1,0.9,0.999,0.0000001,0.95),(153,'1','CaseWesternReserveUniversityData','none','G','2021-04-25 07:04:37',NULL,'success',100,'categorical_crossentropy','adam',0.001,50,100,74.44,'CategoricalAccuracy',0,0.1,0.9,0.999,0.0000001,0.95);
/*!40000 ALTER TABLE `t_task_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_train_bear_info`
--

DROP TABLE IF EXISTS `t_train_bear_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_train_bear_info` (
  `train_number` varchar(10) DEFAULT NULL,
  `vehicle_number` int(10) DEFAULT NULL,
  `bogie` int(1) DEFAULT NULL,
  `axle` int(1) DEFAULT NULL,
  `status_of_vib` int(10) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_train_bear_info`
--

LOCK TABLES `t_train_bear_info` WRITE;
/*!40000 ALTER TABLE `t_train_bear_info` DISABLE KEYS */;
INSERT INTO `t_train_bear_info` VALUES ('G520',12345,1,1,1,1);
/*!40000 ALTER TABLE `t_train_bear_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `age` int(10) DEFAULT NULL,
  `gender` varchar(5) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tel` varchar(15) DEFAULT NULL,
  `mail` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES ('hq','1234',23,'man',1,'13141212447','553932780@qq.com'),('zhangli','Zhqzl08070815.',NULL,NULL,25,NULL,NULL),('hongquan','Zhqzl08070815.',NULL,NULL,26,NULL,NULL);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-26 10:34:19
