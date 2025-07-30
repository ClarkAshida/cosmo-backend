-- Criação da tabela Equipamento
-- Esta tabela representa os equipamentos do sistema
CREATE TABLE equipamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    numero_patrimonio VARCHAR(255),
    serial_number VARCHAR(255),
    imei VARCHAR(15),
    eid VARCHAR(32),
    numero_telefone VARCHAR(20),
    iccid VARCHAR(20),
    tipo_equipamento ENUM('NOTEBOOK', 'DESKTOP', 'CELULAR', 'CHIP', 'IMPRESSORA', 'MONITOR', 'PROJETOR', 'ROTEADOR', 'SWITCH', 'SERVIDOR', 'OUTRO') NOT NULL,
    marca VARCHAR(255),
    modelo VARCHAR(255),
    estado_conservacao ENUM('NOVO', 'REGULAR', 'DANIFICADO') NOT NULL,
    termo_responsabilidade BOOLEAN,
    empresa_id BIGINT NOT NULL,
    sigla_estado VARCHAR(2),
    departamento_id BIGINT NOT NULL,
    sistema_operacional VARCHAR(255),
    processador VARCHAR(255),
    armazenamento VARCHAR(255),
    hostname VARCHAR(255),
    dominio VARCHAR(255),
    remote_access_enabled BOOLEAN,
    antivirus_enabled BOOLEAN,
    valor DECIMAL(10,2),
    nota_fiscal VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    observacoes TEXT,
    status ENUM('DISPONIVEL', 'EM_USO', 'EM_MANUTENCAO', 'DANIFICADO', 'CRIPTOGRAFADO', 'DESCARTADO') NOT NULL DEFAULT 'DISPONIVEL',
    PRIMARY KEY (id),
    CONSTRAINT fk_equipamento_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_equipamento_departamento FOREIGN KEY (departamento_id) REFERENCES departamento(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices únicos para campos de identificação (MySQL não suporta WHERE em UNIQUE INDEX)
CREATE UNIQUE INDEX idx_equipamento_numero_patrimonio ON equipamento(numero_patrimonio);
CREATE UNIQUE INDEX idx_equipamento_serial_number ON equipamento(serial_number);
CREATE UNIQUE INDEX idx_equipamento_imei ON equipamento(imei);
CREATE UNIQUE INDEX idx_equipamento_eid ON equipamento(eid);
CREATE UNIQUE INDEX idx_equipamento_numero_telefone ON equipamento(numero_telefone);
CREATE UNIQUE INDEX idx_equipamento_iccid ON equipamento(iccid);

-- Índices para melhor performance
CREATE INDEX idx_equipamento_tipo ON equipamento(tipo_equipamento);
CREATE INDEX idx_equipamento_status ON equipamento(status);
CREATE INDEX idx_equipamento_marca_modelo ON equipamento(marca, modelo);
CREATE INDEX idx_equipamento_empresa ON equipamento(empresa_id);
CREATE INDEX idx_equipamento_departamento ON equipamento(departamento_id);
CREATE INDEX idx_equipamento_created_at ON equipamento(created_at);
