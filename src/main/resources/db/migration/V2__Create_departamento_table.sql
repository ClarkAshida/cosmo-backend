-- Criação da tabela Departamento
-- Esta tabela representa os departamentos do sistema
CREATE TABLE departamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para melhor performance
CREATE INDEX idx_departamento_nome ON departamento(nome);
