package com.cosmo.cosmo.mapper.equipamento;

import com.cosmo.cosmo.dto.equipamento.ImpressoraCreateDTO;
import com.cosmo.cosmo.dto.equipamento.ImpressoraUpdateDTO;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.entity.equipamento.Impressora;
import org.springframework.stereotype.Component;

@Component
public class ImpressoraMapper {

    public Impressora toEntity(ImpressoraCreateDTO createDTO, Empresa empresa, Departamento departamento) {
        if (createDTO == null) {
            return null;
        }

        Impressora impressora = new Impressora();

        // Mapear campos comuns de Equipamento
        mapCommonFields(createDTO, impressora, empresa, departamento);

        // Mapear campos específicos de Impressora
        mapImpressoraFields(createDTO, impressora);

        return impressora;
    }

    public void updateEntity(ImpressoraUpdateDTO updateDTO, Impressora impressora, Empresa empresa, Departamento departamento) {
        if (updateDTO == null || impressora == null) {
            return;
        }

        // Atualizar campos comuns de Equipamento
        mapCommonFields(updateDTO, impressora, empresa, departamento);

        // Atualizar campos específicos de Impressora
        mapImpressoraFields(updateDTO, impressora);
    }

    private void mapCommonFields(Object dto, Impressora impressora, Empresa empresa, Departamento departamento) {
        if (dto instanceof ImpressoraCreateDTO) {
            ImpressoraCreateDTO createDTO = (ImpressoraCreateDTO) dto;
            impressora.setNumeroPatrimonio(createDTO.getNumeroPatrimonio());
            impressora.setSerialNumber(createDTO.getSerialNumber());
            impressora.setMarca(createDTO.getMarca());
            impressora.setModelo(createDTO.getModelo());
            impressora.setEstadoConservacao(createDTO.getEstadoConservacao());
            impressora.setStatus(createDTO.getStatus());
            impressora.setTermoResponsabilidade(createDTO.getTermoResponsabilidade());
            impressora.setValor(createDTO.getValor());
            impressora.setNotaFiscal(createDTO.getNotaFiscal());
            impressora.setSiglaEstado(createDTO.getSiglaEstado());
            impressora.setObservacoes(createDTO.getObservacoes());
        } else if (dto instanceof ImpressoraUpdateDTO) {
            ImpressoraUpdateDTO updateDTO = (ImpressoraUpdateDTO) dto;
            impressora.setNumeroPatrimonio(updateDTO.getNumeroPatrimonio());
            impressora.setSerialNumber(updateDTO.getSerialNumber());
            impressora.setMarca(updateDTO.getMarca());
            impressora.setModelo(updateDTO.getModelo());
            impressora.setEstadoConservacao(updateDTO.getEstadoConservacao());
            impressora.setStatus(updateDTO.getStatus());
            impressora.setTermoResponsabilidade(updateDTO.getTermoResponsabilidade());
            impressora.setValor(updateDTO.getValor());
            impressora.setNotaFiscal(updateDTO.getNotaFiscal());
            impressora.setSiglaEstado(updateDTO.getSiglaEstado());
            impressora.setObservacoes(updateDTO.getObservacoes());
        }

        impressora.setEmpresa(empresa);
        impressora.setDepartamento(departamento);
    }

    private void mapImpressoraFields(Object dto, Impressora impressora) {
        if (dto instanceof ImpressoraCreateDTO) {
            ImpressoraCreateDTO createDTO = (ImpressoraCreateDTO) dto;
            impressora.setTipoImpressora(createDTO.getTipoImpressora());
            impressora.setColorida(createDTO.getColorida());
            impressora.setMultifuncional(createDTO.getMultifuncional());
            impressora.setEnderecoIP(createDTO.getEnderecoIP());
            impressora.setModeloSuprimento(createDTO.getModeloSuprimento());
        } else if (dto instanceof ImpressoraUpdateDTO) {
            ImpressoraUpdateDTO updateDTO = (ImpressoraUpdateDTO) dto;
            impressora.setTipoImpressora(updateDTO.getTipoImpressora());
            impressora.setColorida(updateDTO.getColorida());
            impressora.setMultifuncional(updateDTO.getMultifuncional());
            impressora.setEnderecoIP(updateDTO.getEnderecoIP());
            impressora.setModeloSuprimento(updateDTO.getModeloSuprimento());
        }
    }
}
