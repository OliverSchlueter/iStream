CREATE TABLE IF NOT EXISTS `users`
(
    `id`         varchar(255) NOT NULL,
    `username`   varchar(255) NOT NULL,
    `password`   varchar(255) NOT NULL,
    `email`      varchar(255) NOT NULL,
    `created_at` bigint       NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `followers`
(
    `follower_id` varchar(255) NOT NULL,
    `followee_id` varchar(255) NOT NULL,
    `created_at`  bigint       NOT NULL,
    PRIMARY KEY (`follower_id`, `followee_id`),
    FOREIGN KEY (`follower_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`followee_id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `stream_configs`
(
    `user_id`     varchar(255) NOT NULL,
    `title`       varchar(255) NOT NULL,
    `description` varchar(255) NOT NULL,
    `category`    varchar(255) NOT NULL,
    PRIMARY KEY (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);