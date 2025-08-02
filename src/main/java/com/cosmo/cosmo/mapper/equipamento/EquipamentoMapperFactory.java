package com.cosmo.cosmo.mapper.equipamento;

import com.cosmo.cosmo.dto.equipamento.*;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.entity.equipamento.*;
import com.cosmo.cosmo.enums.TipoEquipamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoMapperFactory {

    @Autowired
    private NotebookMapper notebookMapper;

    @Autowired
    private DesktopMapper desktopMapper;

    @Autowired
    private CelularMapper celularMapper;

    @Autowired
    private ChipMapper chipMapper;

    @Autowired
    private ImpressoraMapper impressoraMapper;

    @Autowired
    private MonitorMapper monitorMapper;

    @Autowired
    private EquipamentoResponseMapper responseMapper;

    public Equipamento createEquipamento(Object createDTO, TipoEquipamento tipo, Empresa empresa, Departamento departamento) {
        switch (tipo) {
            case NOTEBOOK:
                return notebookMapper.toEntity((NotebookCreateDTO) createDTO, empresa, departamento);
            case DESKTOP:
                return desktopMapper.toEntity((DesktopCreateDTO) createDTO, empresa, departamento);
            case CELULAR:
                return celularMapper.toEntity((CelularCreateDTO) createDTO, empresa, departamento);
            case CHIP:
                return chipMapper.toEntity((ChipCreateDTO) createDTO, empresa, departamento);
            case IMPRESSORA:
                return impressoraMapper.toEntity((ImpressoraCreateDTO) createDTO, empresa, departamento);
            case MONITOR:
                return monitorMapper.toEntity((MonitorCreateDTO) createDTO, empresa, departamento);
            default:
                throw new IllegalArgumentException("Tipo de equipamento não suportado: " + tipo);
        }
    }

    public void updateEquipamento(Object updateDTO, Equipamento equipamento, Empresa empresa, Departamento departamento) {
        if (equipamento instanceof Notebook && updateDTO instanceof NotebookUpdateDTO) {
            notebookMapper.updateEntity((NotebookUpdateDTO) updateDTO, (Notebook) equipamento, empresa, departamento);
        } else if (equipamento instanceof Desktop && updateDTO instanceof DesktopUpdateDTO) {
            desktopMapper.updateEntity((DesktopUpdateDTO) updateDTO, (Desktop) equipamento, empresa, departamento);
        } else if (equipamento instanceof Celular && updateDTO instanceof CelularUpdateDTO) {
            celularMapper.updateEntity((CelularUpdateDTO) updateDTO, (Celular) equipamento, empresa, departamento);
        } else if (equipamento instanceof Chip && updateDTO instanceof ChipUpdateDTO) {
            chipMapper.updateEntity((ChipUpdateDTO) updateDTO, (Chip) equipamento, empresa, departamento);
        } else if (equipamento instanceof Impressora && updateDTO instanceof ImpressoraUpdateDTO) {
            impressoraMapper.updateEntity((ImpressoraUpdateDTO) updateDTO, (Impressora) equipamento, empresa, departamento);
        } else if (equipamento instanceof Monitor && updateDTO instanceof MonitorUpdateDTO) {
            monitorMapper.updateEntity((MonitorUpdateDTO) updateDTO, (Monitor) equipamento, empresa, departamento);
        } else {
            throw new IllegalArgumentException("Tipo de equipamento ou DTO não compatível para atualização");
        }
    }

    public EquipamentoResponseDTO toResponseDTO(Equipamento equipamento) {
        return responseMapper.toResponseDTO(equipamento);
    }

    public TipoEquipamento getTipoFromDTO(Object dto) {
        if (dto instanceof NotebookCreateDTO || dto instanceof NotebookUpdateDTO) {
            return TipoEquipamento.NOTEBOOK;
        } else if (dto instanceof DesktopCreateDTO || dto instanceof DesktopUpdateDTO) {
            return TipoEquipamento.DESKTOP;
        } else if (dto instanceof CelularCreateDTO || dto instanceof CelularUpdateDTO) {
            return TipoEquipamento.CELULAR;
        } else if (dto instanceof ChipCreateDTO || dto instanceof ChipUpdateDTO) {
            return TipoEquipamento.CHIP;
        } else if (dto instanceof ImpressoraCreateDTO || dto instanceof ImpressoraUpdateDTO) {
            return TipoEquipamento.IMPRESSORA;
        } else if (dto instanceof MonitorCreateDTO || dto instanceof MonitorUpdateDTO) {
            return TipoEquipamento.MONITOR;
        } else {
            throw new IllegalArgumentException("Tipo de DTO não suportado: " + dto.getClass().getSimpleName());
        }
    }
}
