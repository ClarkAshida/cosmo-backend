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

        EquipamentoResponseDTO responseDTO = new EquipamentoResponseDTO();
        responseDTO.setId(equipamento.getId());
        responseDTO.setNumeroPatrimonio(equipamento.getNumeroPatrimonio());
        responseDTO.setSerialNumber(equipamento.getSerialNumber());
        responseDTO.setImei(equipamento.getImei());
        responseDTO.setEid(equipamento.getEid());
        responseDTO.setNumeroTelefone(equipamento.getNumeroTelefone());
        responseDTO.setIccid(equipamento.getIccid());
        responseDTO.setTipoEquipamento(equipamento.getTipoEquipamento());
        responseDTO.setMarca(equipamento.getMarca());
        responseDTO.setModelo(equipamento.getModelo());
        responseDTO.setEstadoConservacao(equipamento.getEstadoConservacao());
        responseDTO.setTermoResponsabilidade(equipamento.getTermoResponsabilidade());
        responseDTO.setEmpresa(empresaMapper.toResponseDTO(equipamento.getEmpresa()));
        responseDTO.setSiglaEstado(equipamento.getSiglaEstado());
        responseDTO.setDepartamento(departamentoMapper.toResponseDTO(equipamento.getDepartamento()));
        responseDTO.setSistemaOperacional(equipamento.getSistemaOperacional());
        responseDTO.setProcessador(equipamento.getProcessador());
        responseDTO.setArmazenamento(equipamento.getArmazenamento());
        responseDTO.setHostname(equipamento.getHostname());
        responseDTO.setDominio(equipamento.getDominio());
        responseDTO.setRemoteAccessEnabled(equipamento.getRemoteAccessEnabled());
        responseDTO.setAntivirusEnabled(equipamento.getAntivirusEnabled());
        responseDTO.setValor(equipamento.getValor());
        responseDTO.setNotaFiscal(equipamento.getNotaFiscal());
        responseDTO.setObservacoes(equipamento.getObservacoes());
        responseDTO.setStatus(equipamento.getStatus());

        return responseDTO;
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
