CREATE TABLE IF NOT EXISTS permission (
                                          id BIGINT(20) NOT NULL AUTO_INCREMENT,
                                          description VARCHAR(255) DEFAULT NULL,
                                          PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS user_permission (
                                               id_user BIGINT(20) NOT NULL,
                                               id_permission BIGINT(20) NOT NULL,
                                               PRIMARY KEY (id_user, id_permission),
                                               KEY fk_user_permission_permission (id_permission),
                                               CONSTRAINT fk_user_permission FOREIGN KEY (id_user) REFERENCES users (id),
                                               CONSTRAINT fk_user_permission_permission FOREIGN KEY (id_permission) REFERENCES permission (id)
) ENGINE=InnoDB;

