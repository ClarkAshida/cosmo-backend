CREATE TABLE empresa (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_empresa_nome ON empresa(nome);
CREATE INDEX idx_empresa_estado ON empresa(estado);
