create table community.notification
(
    id         VARCHAR(255) not null,
    notifier   VARCHAR(255) not null,
    receiver   VARCHAR(255) not null,
    outer_id   VARCHAR(255) not null,
    type       int          not null,
    gmt_create BIGINT       null,
    status     int          not null,
    constraint notification_pk
        primary key (id)
);