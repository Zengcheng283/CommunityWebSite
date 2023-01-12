CREATE TABLE comment
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id    BIGINT NOT NULL /*父类id*/,
    type         INT    NOT NULL /*类型*/,
    commentator  BIGINT    NOT NULL /*创建人*/,
    gmt_create   BIGINT NOT NULL /*创建时间*/,
    gmt_modified BIGINT NOT NULL /*更新时间*/,
    like_count   BIGINT DEFAULT 0,
    content      VARCHAR(1024),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;