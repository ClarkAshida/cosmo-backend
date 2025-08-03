-- Criação da tabela historico para controle de empréstimos de equipamentos

CREATE TABLE historico (
    id BIGINT NOT NULL AUTO_INCREMENT,
    equipamento_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_entrega TIMESTAMP NOT NULL,
    data_devolucao TIMESTAMP NULL,
    observacoes_entrega TEXT,
    observacoes_devolucao TEXT,
    url_termo_entrega VARCHAR(500),
    url_termo_devolucao VARCHAR(500),
    status_registro_historico BOOLEAN NOT NULL DEFAULT TRUE,
    motivo_cancelamento TEXT,
    data_cancelamento TIMESTAMP NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_historico_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamento(id),
    CONSTRAINT fk_historico_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para melhor performance
CREATE INDEX idx_historico_equipamento ON historico(equipamento_id);
CREATE INDEX idx_historico_usuario ON historico(usuario_id);
CREATE INDEX idx_historico_data_entrega ON historico(data_entrega);
CREATE INDEX idx_historico_data_devolucao ON historico(data_devolucao);
CREATE INDEX idx_historico_status ON historico(status_registro_historico);

-- Índice composto para consultas de equipamentos em uso
CREATE INDEX idx_historico_ativo ON historico(equipamento_id, status_registro_historico, data_devolucao);
