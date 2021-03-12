-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 12, 2021 at 10:00 AM
-- Server version: 10.4.16-MariaDB
-- PHP Version: 7.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dpro_mb`
--

-- --------------------------------------------------------

--
-- Table structure for table `service_charge`
--

CREATE TABLE `service_charge` (
  `id` int(11) NOT NULL,
  `size` varchar(100) CHARACTER SET utf8 NOT NULL,
  `service_charge` varchar(150) CHARACTER SET utf8 NOT NULL,
  `note` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `pic_evidence` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `service_charge`
--

INSERT INTO `service_charge` (`id`, `size`, `service_charge`, `note`, `pic_evidence`, `created_at`) VALUES
(1, 'small', '500', 'note', '.png', NULL),
(2, 'ใหญ่', '500', '555', NULL, NULL),
(3, 'กลาง', '1500', 'asfasad', NULL, NULL),
(4, 'กลาง', '500', '4356', NULL, NULL),
(5, 'เล็ก', '', '', NULL, NULL),
(6, 'เล็ก', '', '', NULL, NULL),
(7, 'เล็ก', '', '', NULL, NULL),
(8, 'กลาง', '245', '2222', 'images.jpeg', NULL),
(9, 'ใหญ่', '545', '45', 'images (1).jpeg', NULL),
(10, 'เล็ก', '55', '456', 'images (1).jpeg', '2021-03-03 00:40:11'),
(11, 'เล็ก', '', '22', 'images.jpeg', '2021-03-03 00:16:20'),
(12, 'ใหญ่', '55', '214', 'images (1).jpeg', '2021-03-03 00:16:20');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `username` varchar(100) CHARACTER SET utf8 NOT NULL,
  `password` varchar(128) CHARACTER SET utf8 NOT NULL,
  `f_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `l_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `tel` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `f_name`, `l_name`, `tel`, `email`) VALUES
(2, 'dpro', '123456', 'firstname', 'lastname', '0999999999', 'dpro@xam.com'),
(3, 'dpro2', '1234566', 'firstname', 'lastname', '0999999999', 'dpro@xam.com'),
(4, 'dpro3', '123456', 'firstname', 'lastname', '0999999999', 'dpro@xam.com'),
(5, 'sdaf', 'gdsa', 'sadf', 'dsaf', '098889989', 'aasd'),
(6, 'dpro4', '123456', 'firstname', 'lastname', '0999999999', 'dpro@xam.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `service_charge`
--
ALTER TABLE `service_charge`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `service_charge`
--
ALTER TABLE `service_charge`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
