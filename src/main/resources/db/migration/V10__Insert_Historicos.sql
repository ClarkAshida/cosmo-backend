-- =====================================================================
-- INSERÇÃO DE HISTÓRICOS DE EQUIPAMENTOS (MySQL Dialect)
-- =====================================================================

-- Cenário 1: Empréstimos Ativos (data_devolucao IS NULL)

-- Notebook (ID 1) para Lucas Pereira Almeida (ID 1 na tabela usuario)
INSERT INTO historico (equipamento_id, usuario_id, data_entrega, observacoes_entrega, url_termo_entrega) VALUES
    (1, 1, '2025-02-10 09:30:00', 'Equipamento novo, entregue na caixa com todos os acessórios.', 'https://storage.alaresinternet.com.br/termos/termo-entrega-lucas-almeida-20250210.pdf');

-- Notebook (ID 2) para Sofia Rodrigues Lima (ID 2 na tabela usuario)
INSERT INTO historico (equipamento_id, usuario_id, data_entrega, observacoes_entrega, url_termo_entrega) VALUES
    (2, 2, '2025-03-15 14:00:00', 'Entregue para a engenheira de software. Notebook de alta performance para desenvolvimento.', 'https://storage.alaresinternet.com.br/termos/termo-entrega-sofia-lima-20250315.pdf');

-- Notebook (ID 4) para Mariana Alves Monteiro (ID 6 na tabela usuario)
INSERT INTO historico (equipamento_id, usuario_id, data_entrega, observacoes_entrega, url_termo_entrega) VALUES
    (4, 6, '2025-04-01 10:00:00', 'Notebook Dell XPS para a gerente de projetos.', 'https://storage.alaresinternet.com.br/termos/termo-entrega-mariana-monteiro-20250401.pdf');

-- Notebook (ID 5) para Pedro Araujo Carvalho (ID 3 na tabela usuario)
INSERT INTO historico (equipamento_id, usuario_id, data_entrega, observacoes_entrega, url_termo_entrega) VALUES
    (5, 3, '2025-01-20 11:45:00', 'MacBook Pro para a coordenação de marketing.', 'https://storage.alaresinternet.com.br/termos/termo-entrega-pedro-carvalho-20250120.pdf');


-- Cenário 2: Histórico Completo (Equipamento já devolvido)

-- Notebook (ID 3) que foi usado por Beatriz Gomes Martins (ID 4 na tabela usuario) e devolvido para manutenção
INSERT INTO historico (equipamento_id, usuario_id, data_entrega, data_devolucao, observacoes_entrega, observacoes_devolucao, url_termo_entrega, url_termo_devolucao) VALUES
    (3, 4, '2024-05-20 09:00:00', '2025-07-15 17:30:00', 'Entregue para uso da especialista em redes.', 'Devolvido com a tela apresentando um arranhão no canto superior. Encaminhado para manutenção conforme status do equipamento.', 'https://storage.alaresinternet.com.br/termos/termo-entrega-beatriz-martins-20240520.pdf', 'https://storage.alaresinternet.com.br/termos/termo-devolucao-beatriz-martins-20250715.pdf');


-- Cenário 3: Múltiplos Históricos para o mesmo equipamento

-- O mesmo Notebook (ID 3) agora está disponível após manutenção - simular empréstimo futuro para Gabriel Rocha Ribeiro (ID 5)
INSERT INTO historico (equipamento_id, usuario_id, data_entrega, observacoes_entrega, url_termo_entrega) VALUES
    (3, 5, '2025-08-01 10:20:00', 'Equipamento retornou da manutenção e foi realocado para o analista de suporte.', 'https://storage.alaresinternet.com.br/termos/termo-entrega-gabriel-ribeiro-20250801.pdf');


-- Cenário 4: Registro de empréstimo cancelado

-- Tentativa de alocação do Notebook (ID 2) para Amanda Lima Martins (ID 9 na tabela usuario)
INSERT INTO historico (equipamento_id, usuario_id, data_entrega, status_registro_historico, motivo_cancelamento, data_cancelamento) VALUES
    (2, 9, '2025-05-10 09:00:00', FALSE, 'Alocação cancelada devido à mudança de departamento da colaboradora antes da data de entrega do equipamento.', '2025-05-09 15:00:00');
