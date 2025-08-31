

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT(20) NOT NULL AUTO_INCREMENT,
                                     email VARCHAR(255) DEFAULT NULL,
                                     first_name VARCHAR(255) DEFAULT NULL,
                                     last_name VARCHAR(255) DEFAULT NULL,
                                     password VARCHAR(255) DEFAULT NULL,
                                     account_non_expired BIT(1) NOT NULL DEFAULT b'1',
                                     account_non_locked BIT(1) NOT NULL DEFAULT b'1',
                                     credentials_non_expired BIT(1) NOT NULL DEFAULT b'1',
                                     enabled BIT(1) NOT NULL DEFAULT b'1',
                                     PRIMARY KEY (id),
                                     UNIQUE KEY uk_email (email)
) ENGINE=InnoDB;

