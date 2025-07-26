package com.cosmo.cosmo.enums;

public enum StatusEquipamento {
    DISPONIVEL,      // Em estoque, pronto para uso
    EM_USO,          // Alocado a um usuário
    EM_MANUTENCAO,   // Em reparo
    DANIFICADO,      // Danificado sem conserto
    BAIXADO          // Retirado de operação (vendido, descartado)
}

