CREATE TABLE `user`
(
    `id`           BIGINT NOT NULL AUTO_INCREMENT,
    `account_id`   varchar(100) DEFAULT NULL,
    `name`         varchar(50)  DEFAULT NULL,
    `token`        char(36)     DEFAULT NULL,
    `gmt_create`   BIGINT       DEFAULT NULL,
    `gmt_modified` BIGINT       DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;