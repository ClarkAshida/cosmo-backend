package com.cosmo.cosmo.mapper;

import com.cosmo.cosmo.dto.equipamento.EquipamentoResponseDTO;
import com.cosmo.cosmo.entity.equipamento.Equipamento;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.mapper.equipamento.EquipamentoMapperFactory;
import com.cosmo.cosmo.enums.TipoEquipamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoMapper {

    @Autowired
    private EquipamentoMapperFactory equipamentoMapperFactory;

    /**
     * Cria um equipamento específico baseado no tipo e DTO fornecido.
     */
    public Equipamento createEquipamento(Object createDTO, TipoEquipamento tipo, Empresa empresa, Departamento departamento) {
        return equipamentoMapperFactory.createEquipamento(createDTO, tipo, empresa, departamento);
    }

    /**
     * Atualiza um equipamento existente com os dados do DTO.
     */
    public void updateEquipamento(Object updateDTO, Equipamento equipamento, Empresa empresa, Departamento departamento) {
        equipamentoMapperFactory.updateEquipamento(updateDTO, equipamento, empresa, departamento);
    }

    /**
     * Converte um equipamento para DTO de resposta com tipo e detalhes específicos.
     */
    public EquipamentoResponseDTO toResponseDTO(Equipamento equipamento) {
        return equipamentoMapperFactory.toResponseDTO(equipamento);
    }

    /**
     * Determina o tipo de equipamento baseado no tipo do DTO.
     */
    public TipoEquipamento getTipoFromDTO(Object dto) {
        return equipamentoMapperFactory.getTipoFromDTO(dto);
    }

    // TODO: Métodos legados - remover após migração completa dos services
    @Deprecated
    public Equipamento toEntity(Object requestDTO, Empresa empresa, Departamento departamento) {
        throw new UnsupportedOperationException("Use createEquipamento() com tipo específico");
    }

    @Deprecated
    public void updateEntityFromDTO(Object requestDTO, Equipamento equipamento, Empresa empresa, Departamento departamento) {
        throw new UnsupportedOperationException("Use updateEquipamento() com DTO específico");
    }
}
