-- Criação das tabelas para o modelo de equipamentos com herança JOINED
-- Tabela pai: equipamento (campos comuns a todos os tipos)

CREATE TABLE equipamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    numero_patrimonio VARCHAR(50),
    serial_number VARCHAR(100),
    marca VARCHAR(100),
    modelo VARCHAR(100),
    estado_conservacao ENUM('NOVO', 'REGULAR', 'DANIFICADO') NOT NULL,
    status ENUM('DISPONIVEL', 'EM_USO', 'EM_MANUTENCAO', 'DANIFICADO', 'CRIPTOGRAFADO', 'DESCARTADO') NOT NULL,
    termo_responsabilidade BOOLEAN DEFAULT FALSE,
    valor DECIMAL(10,2),
    nota_fiscal VARCHAR(100),
    empresa_id BIGINT NOT NULL,
    sigla_estado VARCHAR(2),
    departamento_id BIGINT NOT NULL,
    observacoes TEXT,
    status_propriedade ENUM('PROPRIO', 'LOCADO'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_equipamento_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_equipamento_departamento FOREIGN KEY (departamento_id) REFERENCES departamento(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela para computadores (campos comuns a notebooks e desktops)
CREATE TABLE computador (
    id BIGINT NOT NULL,
    sistema_operacional VARCHAR(100),
    processador VARCHAR(100),
    memoria_ram VARCHAR(50),
    armazenamento VARCHAR(100),
    hostname VARCHAR(100),
    dominio VARCHAR(100),
    remote_access_enabled BOOLEAN DEFAULT FALSE,
    antivirus_enabled BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT fk_computador_equipamento FOREIGN KEY (id) REFERENCES equipamento(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela para notebooks
CREATE TABLE notebook (
    id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_notebook_computador FOREIGN KEY (id) REFERENCES computador(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela para desktops
CREATE TABLE desktop (
    id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_desktop_computador FOREIGN KEY (id) REFERENCES computador(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela para celulares
CREATE TABLE celular (
    id BIGINT NOT NULL,
    imei VARCHAR(15) UNIQUE,
    imei2 VARCHAR(15) UNIQUE,
    eid VARCHAR(32) UNIQUE,
    gerenciado_por_mdm BOOLEAN DEFAULT FALSE,
    mdm VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT fk_celular_equipamento FOREIGN KEY (id) REFERENCES equipamento(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela para chips
CREATE TABLE chip (
    id BIGINT NOT NULL,
    numero_telefone VARCHAR(20),
    iccid VARCHAR(22) UNIQUE,
    operadora VARCHAR(50),
    tipo_plano VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT fk_chip_equipamento FOREIGN KEY (id) REFERENCES equipamento(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela para impressoras
CREATE TABLE impressora (
    id BIGINT NOT NULL,
    tipo_impressora VARCHAR(50),
    colorida BOOLEAN DEFAULT FALSE,
    multifuncional BOOLEAN DEFAULT FALSE,
    endereco_ip VARCHAR(15),
    modelo_suprimento VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT fk_impressora_equipamento FOREIGN KEY (id) REFERENCES equipamento(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela para monitores
CREATE TABLE monitor (
    id BIGINT NOT NULL,
    tamanho_tela DECIMAL(4,1),
    resolucao VARCHAR(20),
    PRIMARY KEY (id),
    CONSTRAINT fk_monitor_equipamento FOREIGN KEY (id) REFERENCES equipamento(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para melhor performance
CREATE UNIQUE INDEX idx_equipamento_numero_patrimonio ON equipamento(numero_patrimonio);
CREATE UNIQUE INDEX idx_equipamento_serial_number ON equipamento(serial_number);
CREATE INDEX idx_equipamento_marca ON equipamento(marca);
CREATE INDEX idx_equipamento_modelo ON equipamento(modelo);
CREATE INDEX idx_equipamento_status ON equipamento(status);
CREATE INDEX idx_equipamento_empresa ON equipamento(empresa_id);
CREATE INDEX idx_equipamento_departamento ON equipamento(departamento_id);

-- Índices específicos para computadores
CREATE UNIQUE INDEX idx_computador_hostname ON computador(hostname);
CREATE INDEX idx_computador_sistema_operacional ON computador(sistema_operacional);
CREATE INDEX idx_computador_processador ON computador(processador);

-- Índices específicos para celulares
CREATE UNIQUE INDEX idx_celular_imei ON celular(imei);
CREATE UNIQUE INDEX idx_celular_imei2 ON celular(imei2);
CREATE UNIQUE INDEX idx_celular_eid ON celular(eid);

-- Índices específicos para chips
CREATE UNIQUE INDEX idx_chip_iccid ON chip(iccid);
CREATE INDEX idx_chip_numero_telefone ON chip(numero_telefone);
CREATE INDEX idx_chip_operadora ON chip(operadora);

-- Índices específicos para impressoras
CREATE INDEX idx_impressora_endereco_ip ON impressora(endereco_ip);
CREATE INDEX idx_impressora_tipo ON impressora(tipo_impressora);

-- Índices específicos para monitores
CREATE INDEX idx_monitor_tamanho_tela ON monitor(tamanho_tela);
CREATE INDEX idx_monitor_resolucao ON monitor(resolucao);
