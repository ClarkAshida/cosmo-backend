-- =====================================================================
-- INSERÇÃO DE NOTEBOOKS (5 UNIDADES)
-- =====================================================================

-- Notebook 1
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-NTB-00001', 'SN-DELL-A1B2C3D4', 'Dell', 'Latitude 7430', 'NOVO', 'EM_USO', 7500.00, 'NF-2025-1001', 1, 'SP', 29, 'PROPRIO', 'Notebook para time de desenvolvimento.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'Windows 11 Pro', 'Intel Core i7-1265U', '16GB DDR4', '512GB SSD NVMe', 'ALA-NTB-00001', 'alaresinternet.com.br', TRUE, TRUE);
INSERT INTO notebook (id) VALUES (LAST_INSERT_ID());
COMMIT;

-- Notebook 2
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-NTB-00002', 'SN-LEN-E5F6G7H8', 'Lenovo', 'ThinkPad T14', 'NOVO', 'DISPONIVEL', 8200.50, 'NF-2025-1002', 7, 'RN', 49, 'LOCADO', 'Equipamento em estoque para novos colaboradores.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'Windows 11 Pro', 'AMD Ryzen 7 PRO 6850U', '32GB DDR5', '1TB SSD NVMe', 'ALA-NTB-00002', 'alaresinternet.com.br', FALSE, TRUE);
INSERT INTO notebook (id) VALUES (LAST_INSERT_ID());
COMMIT;

-- Notebook 3
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-NTB-00003', 'SN-HP-I9J0K1L2', 'HP', 'EliteBook 840 G9', 'REGULAR', 'EM_MANUTENCAO', 6900.00, 'NF-2024-980', 10, 'MG', 2, 'PROPRIO', 'Tela com pequeno arranhão no canto superior.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'Windows 10 Pro', 'Intel Core i5-1235U', '16GB DDR4', '256GB SSD NVMe', 'ALA-NTB-00003', 'alaresinternet.com.br', TRUE, TRUE);
INSERT INTO notebook (id) VALUES (LAST_INSERT_ID());
COMMIT;

-- Notebook 4
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-NTB-00004', 'SN-DELL-M3N4O5P6', 'Dell', 'XPS 15', 'NOVO', 'EM_USO', 9850.00, 'NF-2025-1003', 12, 'BA', 30, 'PROPRIO', 'Notebook para equipe de Design/UX.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'Windows 11 Pro', 'Intel Core i9-13900H', '32GB DDR5', '1TB SSD NVMe', 'ALA-NTB-00004', 'alaresinternet.com.br', TRUE, TRUE);
INSERT INTO notebook (id) VALUES (LAST_INSERT_ID());
COMMIT;

-- Notebook 5
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-NTB-00005', 'SN-APPLE-Q7R8S9T0', 'Apple', 'MacBook Pro 14"', 'NOVO', 'EM_USO', 15500.00, 'NF-2025-1004', 1, 'SP', 29, 'LOCADO', 'Uso exclusivo da diretoria.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'macOS Sonoma', 'Apple M3 Pro', '18GB Unificada', '512GB SSD', 'ALA-MBP-00001', 'alaresinternet.com.br', TRUE, FALSE);
INSERT INTO notebook (id) VALUES (LAST_INSERT_ID());
COMMIT;


-- =====================================================================
-- INSERÇÃO DE DESKTOPS (5 UNIDADES)
-- =====================================================================

-- Desktop 1
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-DSK-00001', 'SN-DSK-DELL-A1B2C', 'Dell', 'OptiPlex 7000', 'NOVO', 'EM_USO', 5500.00, 'NF-2025-1005', 2, 'PR', 36, 'PROPRIO', 'Desktop para equipe de Governança de TI.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'Windows 11 Pro', 'Intel Core i7-12700', '16GB DDR4', '512GB SSD + 1TB HDD', 'ALA-DSK-00001', 'alaresinternet.com.br', TRUE, TRUE);
INSERT INTO desktop (id) VALUES (LAST_INSERT_ID());
COMMIT;

-- Desktop 2
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-DSK-00002', 'SN-DSK-HP-D3E4F', 'HP', 'ProDesk 400 G9', 'REGULAR', 'EM_USO', 4200.00, 'NF-2024-981', 18, 'SP', 2, 'PROPRIO', 'Utilizado no setor administrativo.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'Windows 10 Pro', 'Intel Core i5-12500', '8GB DDR4', '256GB SSD NVMe', 'ALA-DSK-00002', 'alaresinternet.com.br', FALSE, TRUE);
INSERT INTO desktop (id) VALUES (LAST_INSERT_ID());
COMMIT;

-- Desktop 3
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-DSK-00003', 'SN-DSK-LEN-G5H6I', 'Lenovo', 'ThinkCentre M70q', 'NOVO', 'DISPONIVEL', 4800.75, 'NF-2025-1006', 6, 'CE', 53, 'LOCADO', 'Desktop compacto para novos projetos.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'Ubuntu 24.04 LTS', 'Intel Core i5-13400T', '16GB DDR4', '512GB SSD NVMe', 'ALA-DSK-00003', 'alaresinternet.com.br', TRUE, FALSE);
INSERT INTO desktop (id) VALUES (LAST_INSERT_ID());
COMMIT;

-- Desktop 4
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-DSK-00004', 'SN-DSK-DELL-J7K8L', 'Dell', 'Vostro 3910', 'REGULAR', 'DANIFICADO', 3900.00, 'NF-2024-982', 8, 'PB', 47, 'PROPRIO', 'Fonte de alimentação queimada.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'Windows 11 Home', 'Intel Core i3-12100', '8GB DDR4', '256GB SSD', 'ALA-DSK-00004', 'alaresinternet.com.br', FALSE, TRUE);
INSERT INTO desktop (id) VALUES (LAST_INSERT_ID());
COMMIT;

-- Desktop 5
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-DSK-00005', 'SN-DSK-HP-M9N0O', 'HP', 'OMEN 25L', 'NOVO', 'EM_USO', 9500.00, 'NF-2025-1007', 14, 'SP', 30, 'PROPRIO', 'Estação de trabalho para edição de vídeo.');
INSERT INTO computador (id, sistema_operacional, processador, memoria_ram, armazenamento, hostname, dominio, remote_access_enabled, antivirus_enabled)
VALUES (LAST_INSERT_ID(), 'Windows 11 Pro', 'Intel Core i7-13700K', '32GB DDR5', '1TB SSD NVMe', 'ALA-DSK-00005', 'alaresinternet.com.br', TRUE, TRUE);
INSERT INTO desktop (id) VALUES (LAST_INSERT_ID());
COMMIT;


-- =====================================================================
-- INSERÇÃO DE CELULARES (5 UNIDADES)
-- =====================================================================

-- Celular 1
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CEL-00001', 'SN-CEL-APL-P1Q2R3', 'Apple', 'iPhone 15 Pro', 'NOVO', 'EM_USO', 9200.00, 'NF-2025-1008', 21, 'SP', 8, 'PROPRIO', 'Celular corporativo para CEO.');
INSERT INTO celular (id, imei, imei2, eid, gerenciado_por_mdm, mdm)
VALUES (LAST_INSERT_ID(), '353929093849583', '353929093849584', '89049032005008882111234023459876', TRUE, 'Microsoft Intune');
COMMIT;

-- Celular 2
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CEL-00002', 'SN-CEL-SAM-S4T5U6', 'Samsung', 'Galaxy S25 Ultra', 'NOVO', 'EM_USO', 8500.00, 'NF-2025-1009', 20, 'MG', 4, 'LOCADO', 'Celular para gerente comercial.');
INSERT INTO celular (id, imei, imei2, eid, gerenciado_por_mdm, mdm)
VALUES (LAST_INSERT_ID(), '358823083498721', '358823083498722', '89049032005008882111234023459877', TRUE, 'Microsoft Intune');
COMMIT;

-- Celular 3
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CEL-00003', 'SN-CEL-MOT-V7W8X9', 'Motorola', 'Edge 40', 'REGULAR', 'DISPONIVEL', 3500.00, 'NF-2024-983', 15, 'BA', 52, 'PROPRIO', 'Celular de backup.');
INSERT INTO celular (id, imei, imei2, eid, gerenciado_por_mdm, mdm)
VALUES (LAST_INSERT_ID(), '359987654321098', NULL, NULL, FALSE, NULL);
COMMIT;

-- Celular 4
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CEL-00004', 'SN-CEL-APL-Y0Z1A2', 'Apple', 'iPhone 14', 'REGULAR', 'EM_USO', 5200.00, 'NF-2024-984', 1, 'SP', 1, 'LOCADO', 'Celular para analista de RH.');
INSERT INTO celular (id, imei, imei2, eid, gerenciado_por_mdm, mdm)
VALUES (LAST_INSERT_ID(), '354808071234567', NULL, '89049032005008882111234023459878', TRUE, 'Microsoft Intune');
COMMIT;

-- Celular 5
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CEL-00005', 'SN-CEL-SAM-B3C4D5', 'Samsung', 'Galaxy A55', 'NOVO', 'DISPONIVEL', 2800.00, 'NF-2025-1010', 7, 'RN', 41, 'PROPRIO', 'Celular para equipe de campo.');
INSERT INTO celular (id, imei, imei2, eid, gerenciado_por_mdm, mdm)
VALUES (LAST_INSERT_ID(), '351234567890123', '351234567890124', NULL, TRUE, 'Samsung Knox');
COMMIT;


-- =====================================================================
-- INSERÇÃO DE CHIPS (5 UNIDADES)
-- =====================================================================

-- Chip 1
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CHP-00001', 'ICCID-895501', 'Claro', 'eSIM', 'NOVO', 'EM_USO', 0.00, 'NF-2025-1008', 21, 'SP', 8, 'PROPRIO', 'Chip atrelado ao celular ALA-CEL-00001.');
INSERT INTO chip (id, numero_telefone, iccid, operadora, tipo_plano)
VALUES (LAST_INSERT_ID(), '(11) 98888-1001', '89550112345678901234', 'Claro', 'Plano Corporativo 50GB');
COMMIT;

-- Chip 2
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CHP-00002', 'ICCID-895502', 'Vivo', 'Nano SIM', 'NOVO', 'EM_USO', 0.00, 'NF-2025-1009', 20, 'MG', 4, 'PROPRIO', 'Chip atrelado ao celular ALA-CEL-00002.');
INSERT INTO chip (id, numero_telefone, iccid, operadora, tipo_plano)
VALUES (LAST_INSERT_ID(), '(31) 97777-2002', '89550298765432109876', 'Vivo', 'Plano Corporativo 50GB');
COMMIT;

-- Chip 3
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CHP-00003', 'ICCID-895504', 'TIM', 'Nano SIM', 'NOVO', 'DISPONIVEL', 0.00, 'NF-2025-1011', 7, 'RN', 41, 'PROPRIO', 'Chip de dados para modem 4G.');
INSERT INTO chip (id, numero_telefone, iccid, operadora, tipo_plano)
VALUES (LAST_INSERT_ID(), '(84) 96666-3003', '89550412312312312312', 'TIM', 'Plano M2M 10GB');
COMMIT;

-- Chip 4
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CHP-00004', 'ICCID-895501A', 'Claro', 'Nano SIM', 'REGULAR', 'DISPONIVEL', 0.00, 'NF-2024-985', 7, 'RN', 41, 'PROPRIO', 'Chip reserva para equipe de campo.');
INSERT INTO chip (id, numero_telefone, iccid, operadora, tipo_plano)
VALUES (LAST_INSERT_ID(), '(84) 98888-4004', '89550145645645645645', 'Claro', 'Plano Corporativo 20GB');
COMMIT;

-- Chip 5
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-CHP-00005', 'ICCID-895502B', 'Vivo', 'eSIM', 'NOVO', 'DISPONIVEL', 0.00, 'NF-2025-1012', 1, 'SP', 1, 'PROPRIO', 'eSIM para ativação em novo aparelho.');
INSERT INTO chip (id, numero_telefone, iccid, operadora, tipo_plano)
VALUES (LAST_INSERT_ID(), '(11) 97777-5005', '89550278978978978978', 'Vivo', 'Plano Corporativo 20GB');
COMMIT;


-- =====================================================================
-- INSERÇÃO DE IMPRESSORAS (5 UNIDADES)
-- =====================================================================

-- Impressora 1
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-IMP-00001', 'SN-IMP-HP-A1B2C3', 'HP', 'LaserJet Pro M428fdw', 'NOVO', 'EM_USO', 2800.00, 'NF-2025-1013', 1, 'SP', 2, 'PROPRIO', 'Impressora do departamento Administrativo.');
INSERT INTO impressora (id, tipo_impressora, colorida, multifuncional, endereco_ip, modelo_suprimento)
VALUES (LAST_INSERT_ID(), 'LASER', FALSE, TRUE, '192.168.1.100', 'Toner HP 58A');
COMMIT;

-- Impressora 2
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-IMP-00002', 'SN-IMP-EPS-D4E5F6', 'Epson', 'EcoTank L3250', 'REGULAR', 'EM_USO', 1200.00, 'NF-2024-986', 7, 'RN', 49, 'LOCADO', 'Impressora para marketing.');
INSERT INTO impressora (id, tipo_impressora, colorida, multifuncional, endereco_ip, modelo_suprimento)
VALUES (LAST_INSERT_ID(), 'JATO_DE_TINTA', TRUE, TRUE, '192.168.2.101', 'Refil T544');
COMMIT;

-- Impressora 3
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-IMP-00003', 'SN-IMP-BRO-G7H8I9', 'Brother', 'HL-L8360CDW', 'NOVO', 'DISPONIVEL', 4500.00, 'NF-2025-1014', 6, 'CE', 33, 'PROPRIO', 'Impressora colorida de alto volume.');
INSERT INTO impressora (id, tipo_impressora, colorida, multifuncional, endereco_ip, modelo_suprimento)
VALUES (LAST_INSERT_ID(), 'LASER', TRUE, FALSE, '192.168.3.102', 'Toner TN-416');
COMMIT;

-- Impressora 4
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-IMP-00004', 'SN-IMP-HP-J0K1L2', 'HP', 'OfficeJet Pro 9020', 'DANIFICADO', 'DANIFICADO', 2100.00, 'NF-2024-987', 10, 'MG', 15, 'PROPRIO', 'Bandeja de papel não alimenta.');
INSERT INTO impressora (id, tipo_impressora, colorida, multifuncional, endereco_ip, modelo_suprimento)
VALUES (LAST_INSERT_ID(), 'JATO_DE_TINTA', TRUE, TRUE, '192.168.4.103', 'Cartucho HP 964');
COMMIT;

-- Impressora 5
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-IMP-00005', 'SN-IMP-ZEB-M3N4O5', 'Zebra', 'ZD421', 'NOVO', 'EM_USO', 1800.00, 'NF-2025-1015', 18, 'SP', 44, 'PROPRIO', 'Impressora de etiquetas para logística.');
INSERT INTO impressora (id, tipo_impressora, colorida, multifuncional, endereco_ip, modelo_suprimento)
VALUES (LAST_INSERT_ID(), 'TERMICA', FALSE, FALSE, '192.168.5.104', 'Ribbon Cera 110x74');
COMMIT;


-- =====================================================================
-- INSERÇÃO DE MONITORES (5 UNIDADES)
-- =====================================================================

-- Monitor 1
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-MON-00001', 'SN-MON-DELL-P6Q7R8', 'Dell', 'UltraSharp U2422H', 'NOVO', 'EM_USO', 1800.00, 'NF-2025-1005', 2, 'PR', 36, 'PROPRIO', 'Pareado com o desktop ALA-DSK-00001.');
INSERT INTO monitor (id, tamanho_tela, resolucao)
VALUES (LAST_INSERT_ID(), 24.0, '1920x1080');
COMMIT;

-- Monitor 2
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-MON-00002', 'SN-MON-LG-S9T0U1', 'LG', 'UltraGear 27GN800-B', 'NOVO', 'EM_USO', 2500.00, 'NF-2025-1007', 14, 'SP', 30, 'PROPRIO', 'Pareado com o desktop ALA-DSK-00005.');
INSERT INTO monitor (id, tamanho_tela, resolucao)
VALUES (LAST_INSERT_ID(), 27.0, '2560x1440');
COMMIT;

-- Monitor 3
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-MON-00003', 'SN-MON-SAM-V2W3X4', 'Samsung', 'Odyssey G5', 'REGULAR', 'DISPONIVEL', 2200.00, 'NF-2024-988', 1, 'SP', 37, 'LOCADO', 'Monitor extra para a ilha de produtos.');
INSERT INTO monitor (id, tamanho_tela, resolucao)
VALUES (LAST_INSERT_ID(), 32.0, '2560x1440');
COMMIT;

-- Monitor 4
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-MON-00004', 'SN-MON-DELL-Y5Z6A7', 'Dell', 'P2222H', 'REGULAR', 'EM_USO', 950.00, 'NF-2024-989', 18, 'SP', 2, 'PROPRIO', 'Monitor secundário para o setor administrativo.');
INSERT INTO monitor (id, tamanho_tela, resolucao)
VALUES (LAST_INSERT_ID(), 21.5, '1920x1080');
COMMIT;

-- Monitor 5
START TRANSACTION;
INSERT INTO equipamento (numero_patrimonio, serial_number, marca, modelo, estado_conservacao, status, valor, nota_fiscal, empresa_id, sigla_estado, departamento_id, status_propriedade, observacoes)
VALUES ('ALA-MON-00005', 'SN-MON-LG-B8C9D0', 'LG', '22MP410-B', 'DANIFICADO', 'DESCARTADO', 800.00, 'NF-2024-990', 8, 'PB', 47, 'PROPRIO', 'Tela trincada, enviado para descarte ecológico.');
INSERT INTO monitor (id, tamanho_tela, resolucao)
VALUES (LAST_INSERT_ID(), 21.5, '1920x1080');
COMMIT;