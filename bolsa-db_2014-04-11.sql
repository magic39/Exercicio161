-- phpMyAdmin SQL Dump
-- version 4.0.5
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1:3307

-- Generation Time: Apr 11, 2014 at 11:44 AM
-- Server version: 5.5.33
-- PHP Version: 5.4.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `bolsa`
--

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `clave` varchar(50) NOT NULL,
  `capital` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `usuario`
--

INSERT INTO `usuario` (`id`, `login`, `clave`, `capital`) VALUES
(1, 'Antonio', 'abc123.', 99480.3),
(2, 'Alberto', 'bombur', 20000),
(3, 'Paspallas', 'qwerty', 30000);

-- --------------------------------------------------------

--
-- Table structure for table `usuarios_valores`
--

CREATE TABLE `usuarios_valores` (
  `login` char(20) NOT NULL,
  `id_valor` int(11) NOT NULL,
  `cantidade` int(11) NOT NULL,
  PRIMARY KEY (`login`,`id_valor`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `usuarios_valores`
--

INSERT INTO `usuarios_valores` (`login`, `id_valor`, `cantidade`) VALUES
('Antonio', 1, 300);

-- --------------------------------------------------------

--
-- Table structure for table `valores`
--

CREATE TABLE `valores` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `valor` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `valores`
--

INSERT INTO `valores` (`id`, `nome`, `valor`) VALUES
(1, 'Santander', 1.7323),
(2, 'Movistar', 1.42601),
(3, 'Repsol', 1.02681);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
