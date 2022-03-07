DROP DATABASE IF EXISTS `car_reserve_system`;
CREATE DATABASE `car_reserve_system`;
USE `car_reserve_system`;

CREATE TABLE `car_storage` (
  `car_id` int(11) NOT NULL,
  `car_type` varchar(50) NOT NULL,
  PRIMARY KEY (`car_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `car_storage` VALUES (1,'sedan');
INSERT INTO `car_storage` VALUES (2,'SUV');
INSERT INTO `car_storage` VALUES (3,'SUV');
INSERT INTO `car_storage` VALUES (4,'van');
INSERT INTO `car_storage` VALUES (5,'van');
INSERT INTO `car_storage` VALUES (6,'van');


CREATE TABLE `users` (
  `user_id` varchar(50) NOT NULL,
  `user_type` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `users` VALUES ('001','customer','hello001');
INSERT INTO `users` VALUES ('002','customer','hello002');
INSERT INTO `users` VALUES ('003','customer','hello003');
INSERT INTO `users` VALUES ('004','customer','hello004');
INSERT INTO `users` VALUES ('005','customer','hello005');
INSERT INTO `users` VALUES ('006','customer','hello101');
INSERT INTO `users` VALUES ('007','customer','hello102');


CREATE TABLE `reserve` (
  `order_id` int(11) NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `car_id` int(11) NOT NULL,
  `car_type` varchar(50) NOT NULL,
  `start_date_time` DATETIME NOT NULL,
  /*`start_time` varchar(50) NOT NULL,*/
  `num_of_days` int(11) NOT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `reserve` VALUES (123,'001', 1,'sedan',STR_TO_DATE("04-17-2022 15:40:00", "%m-%d-%Y %H:%i:%s"),'2');
INSERT INTO `reserve` VALUES (124,'002', 1,'sedan',STR_TO_DATE("04-13-2022 15:00:00", "%m-%d-%Y %H:%i:%s"),'3');
INSERT INTO `reserve` VALUES (125,'003', 2,'SUV',STR_TO_DATE("04-16-2022 10:30:00", "%m-%d-%Y %H:%i:%s"),'2');
INSERT INTO `reserve` VALUES (126,'001', 4,'van',STR_TO_DATE("04-14-2022 09:00:00", "%m-%d-%Y %H:%i:%s"),'1');
INSERT INTO `reserve` VALUES (127,'004', 4,'van',STR_TO_DATE("04-16-2022 17:00:00", "%m-%d-%Y %H:%i:%s"),'7');
INSERT INTO `reserve` VALUES (128,'005', 5,'van',STR_TO_DATE("04-15-2022 13:00:00", "%m-%d-%Y %H:%i:%s"),'6');