-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 23, 2022 at 05:25 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.1

SET
SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET
time_zone = "+04:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fitbliss`
--

-- --------------------------------------------------------

--
-- Table structure for table `exercises`
--

CREATE TABLE `exercises`
(
    `exerciseID`     int(10) UNSIGNED NOT NULL,
    `exerciseName`   varchar(25) NOT NULL,
    `caloriesPerMin` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `exercises`
--

INSERT INTO `exercises` (`exerciseID`, `exerciseName`, `caloriesPerMin`)
VALUES (1, 'Running', 12),
       (2, 'Stair climbing', 7),
       (3, 'Walking', 4),
       (4, 'Cycling', 12),
       (5, 'Swimming', 7),
       (6, 'Weight training', 6),
       (7, 'Aerobics', 8),
       (8, 'Gymnastics', 6),
       (9, 'Jump rope', 15),
       (10, 'Badminton', 9),
       (11, 'Basketball', 9),
       (12, 'Football', 9),
       (13, 'Tennis', 9),
       (14, 'Volleyball', 12);

-- --------------------------------------------------------

--
-- Table structure for table `histories`
--

CREATE TABLE `histories`
(
    `historyID`  int(10) UNSIGNED NOT NULL,
    `username`   varchar(15) NOT NULL,
    `exerciseID` int(10) UNSIGNED NOT NULL,
    `duration`   int(10) UNSIGNED NOT NULL,
    `date`       date        NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `histories`
--

INSERT INTO `histories` (`historyID`, `username`, `exerciseID`, `duration`, `date`)
VALUES (1, 'Abhijeet', 9, 41, '2022-07-01'),
       (2, 'Abhijeet', 10, 84, '2022-07-01'),
       (3, 'Abhijeet', 8, 89, '2022-07-02'),
       (4, 'Abhijeet', 1, 107, '2022-07-03'),
       (5, 'Abhijeet', 13, 8, '2022-07-03'),
       (6, 'Abhijeet', 3, 111, '2022-07-03'),
       (7, 'Abhijeet', 6, 59, '2022-07-03'),
       (8, 'Abhijeet', 12, 27, '2022-07-03'),
       (9, 'Abhijeet', 5, 12, '2022-07-04'),
       (10, 'Abhijeet', 3, 15, '2022-07-04'),
       (11, 'Abhijeet', 7, 20, '2022-07-04'),
       (12, 'Abhijeet', 13, 52, '2022-07-05'),
       (13, 'Abhijeet', 7, 99, '2022-07-05'),
       (14, 'Abhijeet', 11, 120, '2022-07-05'),
       (15, 'Abhijeet', 12, 95, '2022-07-06'),
       (16, 'Abhijeet', 11, 76, '2022-07-10'),
       (17, 'Abhijeet', 3, 20, '2022-07-12'),
       (18, 'Abhijeet', 10, 41, '2022-07-12'),
       (19, 'Abhijeet', 5, 38, '2022-07-13'),
       (20, 'Abhijeet', 7, 93, '2022-07-13'),
       (21, 'Abhijeet', 6, 35, '2022-07-14'),
       (22, 'Abhijeet', 8, 59, '2022-07-14'),
       (23, 'Abhijeet', 12, 27, '2022-07-14'),
       (24, 'Abhijeet', 7, 30, '2022-07-14'),
       (25, 'Abhijeet', 14, 37, '2022-07-14'),
       (26, 'Abhijeet', 1, 48, '2022-07-14'),
       (27, 'Viman', 12, 96, '2022-07-15'),
       (28, 'Viman', 6, 49, '2022-07-15'),
       (29, 'Viman', 2, 10, '2022-07-15'),
       (30, 'Abhijeet', 7, 27, '2022-07-15'),
       (31, 'Abhijeet', 1, 29, '2022-07-15'),
       (32, 'Viman', 3, 37, '2022-07-15'),
       (33, 'Oudish', 7, 40, '2022-07-16'),
       (34, 'Oudish', 1, 72, '2022-07-16'),
       (35, 'Oudish', 6, 43, '2022-07-16'),
       (36, 'Oudish', 10, 28, '2022-07-16'),
       (37, 'Oudish', 3, 24, '2022-07-16'),
       (38, 'Viman', 9, 22, '2022-07-16'),
       (39, 'Viman', 1, 61, '2022-07-16'),
       (40, 'Abhijeet', 9, 18, '2022-07-16'),
       (41, 'Abhijeet', 14, 34, '2022-07-16'),
       (42, 'Abhijeet', 9, 17, '2022-07-16'),
       (43, 'Viman', 6, 16, '2022-07-16'),
       (44, 'Viman', 3, 41, '2022-07-16'),
       (45, 'Abhijeet', 9, 16, '2022-07-16'),
       (46, 'Abhijeet', 11, 24, '2022-07-16'),
       (47, 'Abhijeet', 2, 41, '2022-07-16'),
       (48, 'Abhijeet', 4, 14, '2022-07-16'),
       (49, 'Viman', 8, 48, '2022-07-20'),
       (50, 'Abhijeet', 5, 25, '2022-07-20'),
       (51, 'Abhijeet', 2, 32, '2022-07-20'),
       (52, 'Oudish', 7, 18, '2022-07-20'),
       (53, 'Abhijeet', 2, 13, '2022-07-20'),
       (54, 'Abhijeet', 4, 37, '2022-07-20'),
       (55, 'Viman', 3, 38, '2022-07-20'),
       (56, 'Abhijeet', 14, 23, '2022-07-20'),
       (57, 'Oudish', 5, 9, '2022-07-20'),
       (58, 'Viman', 11, 37, '2022-07-20'),
       (59, 'Abhijeet', 9, 16, '2022-07-20'),
       (60, 'Abhijeet', 3, 7, '2022-07-20'),
       (61, 'Viman', 1, 13, '2022-07-20'),
       (62, 'Oudish', 3, 101, '2022-07-20'),
       (63, 'Oudish', 6, 16, '2022-07-20'),
       (64, 'Oudish', 10, 27, '2022-07-20'),
       (65, 'Oudish', 8, 23, '2022-07-20'),
       (66, 'James', 9, 13, '2022-07-23'),
       (67, 'James', 3, 48, '2022-07-23'),
       (68, 'James', 9, 17, '2022-07-23'),
       (69, 'James', 14, 32, '2022-07-23'),
       (70, 'James', 3, 45, '2022-07-23'),
       (71, 'James', 6, 107, '2022-07-23'),
       (72, 'James', 3, 14, '2022-07-23');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users`
(
    `username` varchar(15) NOT NULL,
    `height`   int(10) UNSIGNED NOT NULL,
    `weight`   int(10) UNSIGNED NOT NULL,
    `birthYear` year(4) NOT NULL,
    `gender`   enum('Male','Female') NOT NULL,
    `password` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `height`, `weight`, `birthYear`, `gender`, `password`)
VALUES ('Abhijeet', 180, 60, 2001, 'Male', 'DAAAD6E5604E8E17BD9F108D91E26AFE6281DAC8FDA0091040A7A6D7BD9B43B5'),
       ('James', 183, 102, 1975, 'Male', 'EF797C8118F02DFB649607DD5D3F8C7623048C9C063D532CC95C5ED7A898A64F'),
       ('Oudish', 175, 90, 1969, 'Female', 'F3C80551F8C79074CB022E9179501D7620748CC485F5C01701A414A51D477541'),
       ('Viman', 172, 50, 2001, 'Male', '13A5C202E320D0BF9BB2C6E2C7CF380A6F7DE5D392509FEE260B809C893FF2F9');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `exercises`
--
ALTER TABLE `exercises`
    ADD PRIMARY KEY (`exerciseID`);

--
-- Indexes for table `histories`
--
ALTER TABLE `histories`
    ADD PRIMARY KEY (`historyID`),
	ADD KEY `exercise` (`exerciseID`),
	ADD KEY `user` (`username`) USING BTREE;

--
-- Indexes for table `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `exercises`
--
ALTER TABLE `exercises`
    MODIFY `exerciseID` int (10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `histories`
--
ALTER TABLE `histories`
    MODIFY `historyID` int (10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `histories`
--
ALTER TABLE `histories`
    ADD CONSTRAINT `exercise` FOREIGN KEY (`exerciseID`) REFERENCES `exercises` (`exerciseID`) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD CONSTRAINT `username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON
DELETE
CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
