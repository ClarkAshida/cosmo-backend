-- Criação da tabela Usuario
-- Esta tabela representa os usuários do sistema
CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    cargo VARCHAR(255),
    cpf VARCHAR(14) NOT NULL UNIQUE,
    cidade VARCHAR(255),
    departamento_id BIGINT NOT NULL,
    empresa_id BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_usuario_departamento FOREIGN KEY (departamento_id) REFERENCES departamento(id),
    CONSTRAINT fk_usuario_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para melhor performance e constraints de unicidade
CREATE UNIQUE INDEX idx_usuario_email ON usuario(email);
CREATE UNIQUE INDEX idx_usuario_cpf ON usuario(cpf);
CREATE INDEX idx_usuario_nome ON usuario(nome);
CREATE INDEX idx_usuario_departamento ON usuario(departamento_id);
CREATE INDEX idx_usuario_empresa ON usuario(empresa_id);
CREATE INDEX idx_usuario_ativo ON usuario(ativo);
