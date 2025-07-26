package com.cosmo.cosmo.mapper;

import com.cosmo.cosmo.dto.EquipamentoRequestDTO;
import com.cosmo.cosmo.dto.EquipamentoResponseDTO;
import com.cosmo.cosmo.entity.Equipamento;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoMapper {

    @Autowired
    private DepartamentoMapper departamentoMapper;

    @Autowired
    private EmpresaMapper empresaMapper;

    public Equipamento toEntity(EquipamentoRequestDTO requestDTO, Empresa empresa, Departamento departamento) {
        if (requestDTO == null) {
            return null;
        }

        Equipamento equipamento = new Equipamento();

        // Identificadores básicos
        equipamento.setNumeroPatrimonio(requestDTO.getNumeroPatrimonio());
        equipamento.setSerialNumber(requestDTO.getSerialNumber());
        equipamento.setImei(requestDTO.getImei());

        // Propriedades específicas para celular
        equipamento.setEid(requestDTO.getEid());
        equipamento.setNumeroTelefone(requestDTO.getNumeroTelefone());
        equipamento.setIccid(requestDTO.getIccid());

        // Informações do equipamento
        equipamento.setTipoEquipamento(requestDTO.getTipoEquipamento());
        equipamento.setMarca(requestDTO.getMarca());
        equipamento.setModelo(requestDTO.getModelo());
        equipamento.setEstadoConservacao(requestDTO.getEstadoConservacao());
        equipamento.setTermoResponsabilidade(requestDTO.getTermoResponsabilidade() != null ?
                                           requestDTO.getTermoResponsabilidade() : false);

        // Localização
        equipamento.setEmpresa(empresa);
        equipamento.setSiglaEstado(requestDTO.getSiglaEstado());
        equipamento.setDepartamento(departamento);

        // Informações técnicas
        equipamento.setSistemaOperacional(requestDTO.getSistemaOperacional());
        equipamento.setProcessador(requestDTO.getProcessador());
        equipamento.setArmazenamento(requestDTO.getArmazenamento());
        equipamento.setHostname(requestDTO.getHostname());
        equipamento.setDominio(requestDTO.getDominio());

        // Configurações de segurança
        equipamento.setRemoteAccessEnabled(requestDTO.getRemoteAccessEnabled() != null ?
                                         requestDTO.getRemoteAccessEnabled() : false);
        equipamento.setAntivirusEnabled(requestDTO.getAntivirusEnabled() != null ?
                                      requestDTO.getAntivirusEnabled() : false);

        // Informações de compra
        equipamento.setValor(requestDTO.getValor());
        equipamento.setNotaFiscal(requestDTO.getNotaFiscal());
        equipamento.setObservacoes(requestDTO.getObservacoes());
        equipamento.setStatus(requestDTO.getStatus());

        return equipamento;
    }

    public EquipamentoResponseDTO toResponseDTO(Equipamento equipamento) {
        if (equipamento == null) {
            return null;
        }

        return new EquipamentoResponseDTO(
                equipamento.getId(),
                equipamento.getNumeroPatrimonio(),
                equipamento.getSerialNumber(),
                equipamento.getImei(),
                equipamento.getEid(),
                equipamento.getNumeroTelefone(),
                equipamento.getIccid(),
                equipamento.getTipoEquipamento(),
                equipamento.getMarca(),
                equipamento.getModelo(),
                equipamento.getEstadoConservacao(),
                equipamento.getTermoResponsabilidade(),
                empresaMapper.toResponseDTO(equipamento.getEmpresa()),
                equipamento.getSiglaEstado(),
                departamentoMapper.toResponseDTO(equipamento.getDepartamento()),
                equipamento.getSistemaOperacional(),
                equipamento.getProcessador(),
                equipamento.getArmazenamento(),
                equipamento.getHostname(),
                equipamento.getDominio(),
                equipamento.getRemoteAccessEnabled(),
                equipamento.getAntivirusEnabled(),
                equipamento.getValor(),
                equipamento.getNotaFiscal(),
                equipamento.getCreatedAt(),
                equipamento.getUpdatedAt(),
                equipamento.getObservacoes(),
                equipamento.getStatus()
        );
    }

    public void updateEntityFromDTO(EquipamentoRequestDTO requestDTO, Equipamento equipamento,
                                   Empresa empresa, Departamento departamento) {
        if (requestDTO == null || equipamento == null) {
            return;
        }

        // Identificadores básicos
        equipamento.setNumeroPatrimonio(requestDTO.getNumeroPatrimonio());
        equipamento.setSerialNumber(requestDTO.getSerialNumber());
        equipamento.setImei(requestDTO.getImei());

        // Propriedades específicas para celular
        equipamento.setEid(requestDTO.getEid());
        equipamento.setNumeroTelefone(requestDTO.getNumeroTelefone());
        equipamento.setIccid(requestDTO.getIccid());

        // Informações do equipamento
        equipamento.setTipoEquipamento(requestDTO.getTipoEquipamento());
        equipamento.setMarca(requestDTO.getMarca());
        equipamento.setModelo(requestDTO.getModelo());
        equipamento.setEstadoConservacao(requestDTO.getEstadoConservacao());
        equipamento.setTermoResponsabilidade(requestDTO.getTermoResponsabilidade() != null ?
                                           requestDTO.getTermoResponsabilidade() : false);

        // Localização
        equipamento.setEmpresa(empresa);
        equipamento.setSiglaEstado(requestDTO.getSiglaEstado());
        equipamento.setDepartamento(departamento);

        // Informações técnicas
        equipamento.setSistemaOperacional(requestDTO.getSistemaOperacional());
        equipamento.setProcessador(requestDTO.getProcessador());
        equipamento.setArmazenamento(requestDTO.getArmazenamento());
        equipamento.setHostname(requestDTO.getHostname());
        equipamento.setDominio(requestDTO.getDominio());

        // Configurações de segurança
        equipamento.setRemoteAccessEnabled(requestDTO.getRemoteAccessEnabled() != null ?
                                         requestDTO.getRemoteAccessEnabled() : false);
        equipamento.setAntivirusEnabled(requestDTO.getAntivirusEnabled() != null ?
                                      requestDTO.getAntivirusEnabled() : false);

        // Informações de compra
        equipamento.setValor(requestDTO.getValor());
        equipamento.setNotaFiscal(requestDTO.getNotaFiscal());
        equipamento.setObservacoes(requestDTO.getObservacoes());
        equipamento.setStatus(requestDTO.getStatus());
    }
}
