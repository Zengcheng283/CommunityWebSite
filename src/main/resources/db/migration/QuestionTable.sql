CREATE TABLE `question` (
  `id` VARCHAR(255) NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `description` TEXT,
  `gmt_create` BIGINT DEFAULT NULL,
  `gmt_modified` BIGINT DEFAULT NULL,
  `creator` BIGINT DEFAULT NULL,
  `comment_count` int DEFAULT '0',
  `view_count` int DEFAULT '0',
  `like_count` int DEFAULT '0',
  `tag` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;