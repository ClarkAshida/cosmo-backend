package com.cosmo.cosmo.mapper.equipamento;

import com.cosmo.cosmo.dto.equipamento.*;
import com.cosmo.cosmo.dto.empresa.EmpresaResponseDTO;
import com.cosmo.cosmo.dto.departamento.DepartamentoResponseDTO;
import com.cosmo.cosmo.entity.equipamento.*;
import com.cosmo.cosmo.enums.TipoEquipamento;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoResponseMapper {

    public EquipamentoResponseDTO toResponseDTO(Equipamento equipamento) {
        if (equipamento == null) {
            return null;
        }

        EquipamentoResponseDTO responseDTO = new EquipamentoResponseDTO();

        // Mapear campos comuns
        mapCommonFields(equipamento, responseDTO);

        // Determinar tipo e mapear detalhes específicos
        if (equipamento instanceof Notebook) {
            responseDTO.setTipo(TipoEquipamento.NOTEBOOK.name());
            responseDTO.setDetails(mapComputadorDetails((Notebook) equipamento));
        } else if (equipamento instanceof Desktop) {
            responseDTO.setTipo(TipoEquipamento.DESKTOP.name());
            responseDTO.setDetails(mapComputadorDetails((Desktop) equipamento));
        } else if (equipamento instanceof Celular) {
            responseDTO.setTipo(TipoEquipamento.CELULAR.name());
            responseDTO.setDetails(mapCelularDetails((Celular) equipamento));
        } else if (equipamento instanceof Chip) {
            responseDTO.setTipo(TipoEquipamento.CHIP.name());
            responseDTO.setDetails(mapChipDetails((Chip) equipamento));
        } else if (equipamento instanceof Impressora) {
            responseDTO.setTipo(TipoEquipamento.IMPRESSORA.name());
            responseDTO.setDetails(mapImpressoraDetails((Impressora) equipamento));
        } else if (equipamento instanceof Monitor) {
            responseDTO.setTipo(TipoEquipamento.MONITOR.name());
            responseDTO.setDetails(mapMonitorDetails((Monitor) equipamento));
        }

        return responseDTO;
    }

    private void mapCommonFields(Equipamento equipamento, EquipamentoResponseDTO responseDTO) {
        responseDTO.setId(equipamento.getId());
        responseDTO.setNumeroPatrimonio(equipamento.getNumeroPatrimonio());
        responseDTO.setSerialNumber(equipamento.getSerialNumber());
        responseDTO.setMarca(equipamento.getMarca());
        responseDTO.setModelo(equipamento.getModelo());
        responseDTO.setEstadoConservacao(equipamento.getEstadoConservacao());
        responseDTO.setStatus(equipamento.getStatus());
        responseDTO.setTermoResponsabilidade(equipamento.getTermoResponsabilidade());
        responseDTO.setValor(equipamento.getValor());
        responseDTO.setNotaFiscal(equipamento.getNotaFiscal());
        responseDTO.setObservacoes(equipamento.getObservacoes());
        responseDTO.setStatusPropriedade(equipamento.getStatusPropriedade());
        responseDTO.setCreatedAt(equipamento.getCreatedAt());
        responseDTO.setUpdatedAt(equipamento.getUpdatedAt());

        // Mapear objetos aninhados em vez de campos redundantes
        if (equipamento.getEmpresa() != null) {
            EmpresaResponseDTO empresaDTO = new EmpresaResponseDTO();
            empresaDTO.setId(equipamento.getEmpresa().getId());
            empresaDTO.setNome(equipamento.getEmpresa().getNome());
            empresaDTO.setEstado(equipamento.getEmpresa().getEstado());
            responseDTO.setEmpresa(empresaDTO);
        }

        if (equipamento.getDepartamento() != null) {
            DepartamentoResponseDTO departamentoDTO = new DepartamentoResponseDTO();
            departamentoDTO.setId(equipamento.getDepartamento().getId());
            departamentoDTO.setNome(equipamento.getDepartamento().getNome());
            responseDTO.setDepartamento(departamentoDTO);
        }
    }

    private ComputadorDetailsDTO mapComputadorDetails(Computador computador) {
        ComputadorDetailsDTO details = new ComputadorDetailsDTO();
        details.setSistemaOperacional(computador.getSistemaOperacional());
        details.setProcessador(computador.getProcessador());
        details.setMemoriaRAM(computador.getMemoriaRAM());
        details.setArmazenamento(computador.getArmazenamento());
        details.setHostname(computador.getHostname());
        details.setDominio(computador.getDominio());
        details.setRemoteAccessEnabled(computador.getRemoteAccessEnabled());
        details.setAntivirusEnabled(computador.getAntivirusEnabled());
        return details;
    }

    private CelularDetailsDTO mapCelularDetails(Celular celular) {
        CelularDetailsDTO details = new CelularDetailsDTO();
        details.setImei(celular.getImei());
        details.setImei2(celular.getImei2());
        details.setEid(celular.getEid());
        details.setGerenciadoPorMDM(celular.getGerenciadoPorMDM());
        details.setMDM(celular.getMDM());
        return details;
    }

    private ChipDetailsDTO mapChipDetails(Chip chip) {
        ChipDetailsDTO details = new ChipDetailsDTO();
        details.setNumeroTelefone(chip.getNumeroTelefone());
        details.setIccid(chip.getIccid());
        details.setOperadora(chip.getOperadora());
        details.setTipoPlano(chip.getTipoPlano());
        return details;
    }

    private ImpressoraDetailsDTO mapImpressoraDetails(Impressora impressora) {
        ImpressoraDetailsDTO details = new ImpressoraDetailsDTO();
        details.setTipoImpressora(impressora.getTipoImpressora());
        details.setColorida(impressora.getColorida());
        details.setMultifuncional(impressora.getMultifuncional());
        details.setEnderecoIP(impressora.getEnderecoIP());
        details.setModeloSuprimento(impressora.getModeloSuprimento());
        return details;
    }

    private MonitorDetailsDTO mapMonitorDetails(Monitor monitor) {
        MonitorDetailsDTO details = new MonitorDetailsDTO();
        details.setTamanhoTela(monitor.getTamanhoTela());
        details.setResolucao(monitor.getResolucao());
        return details;
    }
}
