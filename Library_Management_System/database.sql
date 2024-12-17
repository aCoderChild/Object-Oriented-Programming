SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `library`
--

create database final;
use final;

DELIMITER $$
--
-- functions
--
CREATE DEFINER=`root`@`localhost` FUNCTION `generateRandomID` (`len` INT) RETURNS VARCHAR(255) CHARSET utf8mb4 COLLATE utf8mb4_general_ci DETERMINISTIC BEGIN
    DECLARE chars VARCHAR(62) DEFAULT 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    DECLARE result VARCHAR(255) DEFAULT '';
    DECLARE i INT DEFAULT 0;

    WHILE i < len DO
        SET result = CONCAT(result, SUBSTRING(chars, FLOOR(1 + RAND() * CHAR_LENGTH(chars)), 1));
        SET i = i + 1;
    END WHILE;

    RETURN result;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- book table
--

CREATE TABLE `book` (
  `bookID` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `genre` text DEFAULT NULL,
  `author` varchar(255) NOT NULL,
  `imageURL` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `yearPublic` int(4) DEFAULT NULL,
  `datePublish` datetime DEFAULT current_timestamp,
  `viewCount` int(11) NOT NULL DEFAULT 0,
  `borrowCount` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- borrow table
--

CREATE TABLE `borrow` (
  `bookID` varchar(255) NOT NULL,
  `userID` varchar(20) NOT NULL,
  `borrowDate` datetime NOT NULL DEFAULT current_timestamp(),
  `returnDate` datetime GENERATED ALWAYS AS (borrowDate + INTERVAL 1 MONTH) STORED,
  `status` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- highlightbook table
--

CREATE TABLE `highlightbook` (
  `bookID` varchar(255) NOT NULL,
  `rating` FLOAT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- recentbook
--

CREATE TABLE `recentbook` (
  `userID` varchar(20) NOT NULL,
  `bookID` varchar(255) NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `userID` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `fullName` varchar(100) DEFAULT NULL,
  `password` varchar(100) NOT NULL DEFAULT 'root',
  `phone` varchar(20) NOT NULL DEFAULT '0000000000',
  `dateOfBirth` varchar(20) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `profileImage` BLOB -- Added BLOB column for profile images
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`bookID`);

--
-- Chỉ mục cho bảng `borrow`
--
ALTER TABLE `borrow`
  ADD KEY `userID` (`userID`),
  ADD KEY `fk_book_id` (`bookID`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userID`);

--
-- Các ràng buộc cho bảng `borrow`
--
ALTER TABLE `borrow`
  ADD CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`),
  ADD CONSTRAINT `fk_book_id` FOREIGN KEY (`bookID`) REFERENCES `book` (`bookID`) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE report (
    reportID varchar(255),
    title VARCHAR(500),
    content TEXT,
    executionDate DATE,
    status VARCHAR(50),
    userID VARCHAR(20) NOT NULL,
    FOREIGN KEY (userID) REFERENCES user(userID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

alter table highlightbook
add column userID varchar(20) not null;

alter table highlightbook
drop column rating;

-- error
ALTER TABLE `highlightbook`
  ADD CONSTRAINT `fk_book` FOREIGN KEY (`bookID`) REFERENCES `book` (`bookID`),
  ADD CONSTRAINT `fk_highlightbook` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`);
  
  
  alter table user
modify column profileImage mediumblob;

ALTER TABLE `borrow` DROP FOREIGN KEY `fk_book_id`; 
ALTER TABLE `borrow` ADD CONSTRAINT `fk_book_id` FOREIGN KEY (`bookID`) REFERENCES `book`(`bookID`) ON DELETE CASCADE ON UPDATE CASCADE;

-- error
ALTER TABLE `highlightbook` DROP FOREIGN KEY `fk_book`; 
ALTER TABLE `highlightbook` ADD CONSTRAINT `fk_book` FOREIGN KEY (`bookID`) REFERENCES `book`(`bookID`) ON DELETE CASCADE ON UPDATE CASCADE; 
ALTER TABLE `highlightbook` DROP FOREIGN KEY `fk_highlightbook`; 
ALTER TABLE `highlightbook` ADD CONSTRAINT `fk_highlightbook` FOREIGN KEY (`userID`) REFERENCES `user`(`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `borrow` DROP FOREIGN KEY `borrow_ibfk_1`; 
ALTER TABLE `borrow` ADD CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user`(`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `recentbook` ADD CONSTRAINT `fk_recentbook` FOREIGN KEY (`bookID`) REFERENCES `book`(`bookID`) ON DELETE CASCADE ON UPDATE CASCADE; 
ALTER TABLE `recentbook` ADD CONSTRAINT `fk_user_recent` FOREIGN KEY (`userID`) REFERENCES `user`(`userID`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `report` DROP FOREIGN KEY `report_ibfk_1`; ALTER TABLE `report` ADD CONSTRAINT `report_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user`(`userID`) ON DELETE CASCADE ON UPDATE CASCADE;
