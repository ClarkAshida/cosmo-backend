package com.cosmo.cosmo.mapper.equipamento;

import com.cosmo.cosmo.dto.equipamento.DesktopCreateDTO;
import com.cosmo.cosmo.dto.equipamento.DesktopUpdateDTO;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.entity.equipamento.Desktop;
import org.springframework.stereotype.Component;

@Component
public class DesktopMapper {

    public Desktop toEntity(DesktopCreateDTO createDTO, Empresa empresa, Departamento departamento) {
        if (createDTO == null) {
            return null;
        }

        Desktop desktop = new Desktop();

        // Mapear campos comuns de Equipamento
        mapCommonFields(createDTO, desktop, empresa, departamento);

        // Mapear campos específicos de Computador
        mapComputadorFields(createDTO, desktop);

        return desktop;
    }

    public void updateEntity(DesktopUpdateDTO updateDTO, Desktop desktop, Empresa empresa, Departamento departamento) {
        if (updateDTO == null || desktop == null) {
            return;
        }

        // Atualizar campos comuns de Equipamento
        mapCommonFields(updateDTO, desktop, empresa, departamento);

        // Atualizar campos específicos de Computador
        mapComputadorFields(updateDTO, desktop);
    }

    private void mapCommonFields(Object dto, Desktop desktop, Empresa empresa, Departamento departamento) {
        if (dto instanceof DesktopCreateDTO) {
            DesktopCreateDTO createDTO = (DesktopCreateDTO) dto;
            desktop.setNumeroPatrimonio(createDTO.getNumeroPatrimonio());
            desktop.setSerialNumber(createDTO.getSerialNumber());
            desktop.setMarca(createDTO.getMarca());
            desktop.setModelo(createDTO.getModelo());
            desktop.setEstadoConservacao(createDTO.getEstadoConservacao());
            desktop.setStatus(createDTO.getStatus());
            desktop.setTermoResponsabilidade(createDTO.getTermoResponsabilidade());
            desktop.setValor(createDTO.getValor());
            desktop.setNotaFiscal(createDTO.getNotaFiscal());
            desktop.setSiglaEstado(createDTO.getSiglaEstado());
            desktop.setObservacoes(createDTO.getObservacoes());
        } else if (dto instanceof DesktopUpdateDTO) {
            DesktopUpdateDTO updateDTO = (DesktopUpdateDTO) dto;
            desktop.setNumeroPatrimonio(updateDTO.getNumeroPatrimonio());
            desktop.setSerialNumber(updateDTO.getSerialNumber());
            desktop.setMarca(updateDTO.getMarca());
            desktop.setModelo(updateDTO.getModelo());
            desktop.setEstadoConservacao(updateDTO.getEstadoConservacao());
            desktop.setStatus(updateDTO.getStatus());
            desktop.setTermoResponsabilidade(updateDTO.getTermoResponsabilidade());
            desktop.setValor(updateDTO.getValor());
            desktop.setNotaFiscal(updateDTO.getNotaFiscal());
            desktop.setSiglaEstado(updateDTO.getSiglaEstado());
            desktop.setObservacoes(updateDTO.getObservacoes());
        }

        desktop.setEmpresa(empresa);
        desktop.setDepartamento(departamento);
    }

    private void mapComputadorFields(Object dto, Desktop desktop) {
        if (dto instanceof DesktopCreateDTO) {
            DesktopCreateDTO createDTO = (DesktopCreateDTO) dto;
            desktop.setSistemaOperacional(createDTO.getSistemaOperacional());
            desktop.setProcessador(createDTO.getProcessador());
            desktop.setMemoriaRAM(createDTO.getMemoriaRAM());
            desktop.setArmazenamento(createDTO.getArmazenamento());
            desktop.setHostname(createDTO.getHostname());
            desktop.setDominio(createDTO.getDominio());
            desktop.setRemoteAccessEnabled(createDTO.getRemoteAccessEnabled());
            desktop.setAntivirusEnabled(createDTO.getAntivirusEnabled());
            desktop.setStatusPropriedade(createDTO.getStatusPropriedade());
        } else if (dto instanceof DesktopUpdateDTO) {
            DesktopUpdateDTO updateDTO = (DesktopUpdateDTO) dto;
            desktop.setSistemaOperacional(updateDTO.getSistemaOperacional());
            desktop.setProcessador(updateDTO.getProcessador());
            desktop.setMemoriaRAM(updateDTO.getMemoriaRAM());
            desktop.setArmazenamento(updateDTO.getArmazenamento());
            desktop.setHostname(updateDTO.getHostname());
            desktop.setDominio(updateDTO.getDominio());
            desktop.setRemoteAccessEnabled(updateDTO.getRemoteAccessEnabled());
            desktop.setAntivirusEnabled(updateDTO.getAntivirusEnabled());
            desktop.setStatusPropriedade(updateDTO.getStatusPropriedade());
        }
    }
}
