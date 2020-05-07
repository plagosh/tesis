-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-11-2018 a las 03:36:54
-- Versión del servidor: 10.1.21-MariaDB
-- Versión de PHP: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `php_buscador`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cursos`
--

CREATE TABLE `cursos` (
  `id` int(11) NOT NULL,
  `lenguaje` varchar(50) NOT NULL,
  `descripcion` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabla cursos' ROW_FORMAT=COMPACT;

--
-- Volcado de datos para la tabla `cursos`
--

INSERT INTO `cursos` (`id`, `lenguaje`, `descripcion`) VALUES
(1, 'PHP', 'Aprende como crear una aplicación web completa con PHP.'),
(2, 'OOP', 'Con esta nueva version usted podra gozar de la velocidad con el docente Luis Muñoz'),
(3, 'PHP7', 'A cargo de Doc. Mario Muñoz. PHP es muy intuitivo y muy facil de aprender.'),
(4, 'MYSQL', 'MySQL es muy importante para gestionar su informacion. Aprende de manera muy facil.'),
(5, 'Java', 'La mayoria de programadores crea sus aplicaciones en este robusto lenguaje PHP.');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cursos`
--
ALTER TABLE `cursos`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `cursos` ADD FULLTEXT KEY `Buscar` (`lenguaje`,`descripcion`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `cursos`
--
ALTER TABLE `cursos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
