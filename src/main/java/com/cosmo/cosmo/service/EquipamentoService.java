package com.cosmo.cosmo.service;

import com.cosmo.cosmo.dto.equipamento.*;
import com.cosmo.cosmo.entity.equipamento.Equipamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.enums.TipoEquipamento;
import com.cosmo.cosmo.mapper.EquipamentoMapper;
import com.cosmo.cosmo.repository.EquipamentoRepository;
import com.cosmo.cosmo.repository.equipamento.EquipamentoRepositoryFactory;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private EquipamentoRepositoryFactory repositoryFactory;

    @Autowired
    private EquipamentoMapper equipamentoMapper;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private DepartamentoService departamentoService;

    public List<EquipamentoResponseDTO> findAll() {
        return equipamentoRepository.findAll()
                .stream()
                .map(equipamentoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EquipamentoResponseDTO findById(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado com id: " + id));
        return equipamentoMapper.toResponseDTO(equipamento);
    }

    // Métodos específicos para criação por tipo de equipamento
    public EquipamentoResponseDTO createNotebook(NotebookCreateDTO createDTO) {
        return createEquipamento(createDTO, TipoEquipamento.NOTEBOOK);
    }

    public EquipamentoResponseDTO createDesktop(DesktopCreateDTO createDTO) {
        return createEquipamento(createDTO, TipoEquipamento.DESKTOP);
    }

    public EquipamentoResponseDTO createCelular(CelularCreateDTO createDTO) {
        return createEquipamento(createDTO, TipoEquipamento.CELULAR);
    }

    public EquipamentoResponseDTO createChip(ChipCreateDTO createDTO) {
        return createEquipamento(createDTO, TipoEquipamento.CHIP);
    }

    public EquipamentoResponseDTO createImpressora(ImpressoraCreateDTO createDTO) {
        return createEquipamento(createDTO, TipoEquipamento.IMPRESSORA);
    }

    public EquipamentoResponseDTO createMonitor(MonitorCreateDTO createDTO) {
        return createEquipamento(createDTO, TipoEquipamento.MONITOR);
    }

    // Métodos específicos para atualização por tipo de equipamento
    public EquipamentoResponseDTO updateNotebook(Long id, NotebookUpdateDTO updateDTO) {
        return updateEquipamento(id, updateDTO);
    }

    public EquipamentoResponseDTO updateDesktop(Long id, DesktopUpdateDTO updateDTO) {
        return updateEquipamento(id, updateDTO);
    }

    public EquipamentoResponseDTO updateCelular(Long id, CelularUpdateDTO updateDTO) {
        return updateEquipamento(id, updateDTO);
    }

    public EquipamentoResponseDTO updateChip(Long id, ChipUpdateDTO updateDTO) {
        return updateEquipamento(id, updateDTO);
    }

    public EquipamentoResponseDTO updateImpressora(Long id, ImpressoraUpdateDTO updateDTO) {
        return updateEquipamento(id, updateDTO);
    }

    public EquipamentoResponseDTO updateMonitor(Long id, MonitorUpdateDTO updateDTO) {
        return updateEquipamento(id, updateDTO);
    }

    // Método genérico para criação
    private EquipamentoResponseDTO createEquipamento(Object createDTO, TipoEquipamento tipo) {
        // Validar campos únicos antes de salvar
        validateUniqueFieldsForCreation(createDTO, tipo);

        // Obter empresa e departamento
        Long empresaId = getEmpresaIdFromDTO(createDTO);
        Long departamentoId = getDepartamentoIdFromDTO(createDTO);

        Empresa empresa = empresaService.findEntityById(empresaId);
        Departamento departamento = departamentoService.findEntityById(departamentoId);

        // Criar equipamento usando o mapper específico
        Equipamento equipamento = equipamentoMapper.createEquipamento(createDTO, tipo, empresa, departamento);
        equipamento = equipamentoRepository.save(equipamento);

        return equipamentoMapper.toResponseDTO(equipamento);
    }

    // Método genérico para atualização
    private EquipamentoResponseDTO updateEquipamento(Long id, Object updateDTO) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado com id: " + id));

        TipoEquipamento tipo = equipamentoMapper.getTipoFromDTO(updateDTO);

        // Validar campos únicos antes de atualizar
        validateUniqueFieldsForUpdate(updateDTO, tipo, id);

        // Obter empresa e departamento
        Long empresaId = getEmpresaIdFromDTO(updateDTO);
        Long departamentoId = getDepartamentoIdFromDTO(updateDTO);

        Empresa empresa = empresaService.findEntityById(empresaId);
        Departamento departamento = departamentoService.findEntityById(departamentoId);

        // Atualizar usando o mapper específico
        equipamentoMapper.updateEquipamento(updateDTO, equipamento, empresa, departamento);
        equipamento = equipamentoRepository.save(equipamento);

        return equipamentoMapper.toResponseDTO(equipamento);
    }

    public void deleteById(Long id) {
        if (!equipamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipamento não encontrado com id: " + id);
        }
        equipamentoRepository.deleteById(id);
    }

    // Método auxiliar para outros services
    public Equipamento findEntityById(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado com id: " + id));
    }

    /**
     * Atualiza apenas o status de um equipamento
     * Método auxiliar para uso do HistoricoService
     */
    @Transactional
    public void updateEntityStatus(Long equipamentoId, StatusEquipamento novoStatus) {
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado com id: " + equipamentoId));

        equipamento.setStatus(novoStatus);
        equipamentoRepository.save(equipamento);
    }

    // Métodos para buscar por tipo específico
    public List<EquipamentoResponseDTO> findByTipo(TipoEquipamento tipo) {
        Class<? extends Equipamento> entityClass = getEntityClassByTipo(tipo);
        return equipamentoRepository.findByTipo(entityClass)
                .stream()
                .map(equipamentoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Long countByTipo(TipoEquipamento tipo) {
        Class<? extends Equipamento> entityClass = getEntityClassByTipo(tipo);
        return equipamentoRepository.countByTipo(entityClass);
    }

    // Métodos auxiliares para extrair IDs dos DTOs
    private Long getEmpresaIdFromDTO(Object dto) {
        if (dto instanceof NotebookCreateDTO) return ((NotebookCreateDTO) dto).getEmpresaId();
        if (dto instanceof NotebookUpdateDTO) return ((NotebookUpdateDTO) dto).getEmpresaId();
        if (dto instanceof DesktopCreateDTO) return ((DesktopCreateDTO) dto).getEmpresaId();
        if (dto instanceof DesktopUpdateDTO) return ((DesktopUpdateDTO) dto).getEmpresaId();
        if (dto instanceof CelularCreateDTO) return ((CelularCreateDTO) dto).getEmpresaId();
        if (dto instanceof CelularUpdateDTO) return ((CelularUpdateDTO) dto).getEmpresaId();
        if (dto instanceof ChipCreateDTO) return ((ChipCreateDTO) dto).getEmpresaId();
        if (dto instanceof ChipUpdateDTO) return ((ChipUpdateDTO) dto).getEmpresaId();
        if (dto instanceof ImpressoraCreateDTO) return ((ImpressoraCreateDTO) dto).getEmpresaId();
        if (dto instanceof ImpressoraUpdateDTO) return ((ImpressoraUpdateDTO) dto).getEmpresaId();
        if (dto instanceof MonitorCreateDTO) return ((MonitorCreateDTO) dto).getEmpresaId();
        if (dto instanceof MonitorUpdateDTO) return ((MonitorUpdateDTO) dto).getEmpresaId();
        throw new IllegalArgumentException("Tipo de DTO não suportado para extração de empresa ID");
    }

    private Long getDepartamentoIdFromDTO(Object dto) {
        if (dto instanceof NotebookCreateDTO) return ((NotebookCreateDTO) dto).getDepartamentoId();
        if (dto instanceof NotebookUpdateDTO) return ((NotebookUpdateDTO) dto).getDepartamentoId();
        if (dto instanceof DesktopCreateDTO) return ((DesktopCreateDTO) dto).getDepartamentoId();
        if (dto instanceof DesktopUpdateDTO) return ((DesktopUpdateDTO) dto).getDepartamentoId();
        if (dto instanceof CelularCreateDTO) return ((CelularCreateDTO) dto).getDepartamentoId();
        if (dto instanceof CelularUpdateDTO) return ((CelularUpdateDTO) dto).getDepartamentoId();
        if (dto instanceof ChipCreateDTO) return ((ChipCreateDTO) dto).getDepartamentoId();
        if (dto instanceof ChipUpdateDTO) return ((ChipUpdateDTO) dto).getDepartamentoId();
        if (dto instanceof ImpressoraCreateDTO) return ((ImpressoraCreateDTO) dto).getDepartamentoId();
        if (dto instanceof ImpressoraUpdateDTO) return ((ImpressoraUpdateDTO) dto).getDepartamentoId();
        if (dto instanceof MonitorCreateDTO) return ((MonitorCreateDTO) dto).getDepartamentoId();
        if (dto instanceof MonitorUpdateDTO) return ((MonitorUpdateDTO) dto).getDepartamentoId();
        throw new IllegalArgumentException("Tipo de DTO não suportado para extração de departamento ID");
    }

    private Class<? extends Equipamento> getEntityClassByTipo(TipoEquipamento tipo) {
        switch (tipo) {
            case NOTEBOOK: return com.cosmo.cosmo.entity.equipamento.Notebook.class;
            case DESKTOP: return com.cosmo.cosmo.entity.equipamento.Desktop.class;
            case CELULAR: return com.cosmo.cosmo.entity.equipamento.Celular.class;
            case CHIP: return com.cosmo.cosmo.entity.equipamento.Chip.class;
            case IMPRESSORA: return com.cosmo.cosmo.entity.equipamento.Impressora.class;
            case MONITOR: return com.cosmo.cosmo.entity.equipamento.Monitor.class;
            default: throw new IllegalArgumentException("Tipo de equipamento não suportado: " + tipo);
        }
    }

    /**
     * Valida se os campos únicos não estão duplicados na criação de um novo equipamento
     */
    private void validateUniqueFieldsForCreation(Object createDTO, TipoEquipamento tipo) {
        // Validar campos comuns
        validateCommonUniqueFields(createDTO, null);

        // Validar campos específicos por tipo
        validateSpecificUniqueFields(createDTO, tipo, null);
    }

    /**
     * Valida se os campos únicos não estão duplicados na atualização de um equipamento existente
     */
    private void validateUniqueFieldsForUpdate(Object updateDTO, TipoEquipamento tipo, Long equipamentoId) {
        // Validar campos comuns
        validateCommonUniqueFields(updateDTO, equipamentoId);

        // Validar campos específicos por tipo
        validateSpecificUniqueFields(updateDTO, tipo, equipamentoId);
    }

    private void validateCommonUniqueFields(Object dto, Long excludeId) {
        String numeroPatrimonio = getNumeroPatrimonioFromDTO(dto);
        String serialNumber = getSerialNumberFromDTO(dto);

        // Validar número de patrimônio
        if (numeroPatrimonio != null) {
            boolean exists = excludeId != null
                ? equipamentoRepository.existsByNumeroPatrimonioAndIdNot(numeroPatrimonio, excludeId)
                : equipamentoRepository.existsByNumeroPatrimonio(numeroPatrimonio);

            if (exists) {
                throw new DuplicateResourceException("Já existe um equipamento cadastrado com o número de patrimônio: " + numeroPatrimonio);
            }
        }

        // Validar serial number
        if (serialNumber != null) {
            boolean exists = excludeId != null
                ? equipamentoRepository.existsBySerialNumberAndIdNot(serialNumber, excludeId)
                : equipamentoRepository.existsBySerialNumber(serialNumber);

            if (exists) {
                throw new DuplicateResourceException("Já existe um equipamento cadastrado com o serial number: " + serialNumber);
            }
        }
    }

    private void validateSpecificUniqueFields(Object dto, TipoEquipamento tipo, Long excludeId) {
        Class<? extends Equipamento> entityClass = getEntityClassByTipo(tipo);

        switch (tipo) {
            case CELULAR:
                validateCelularUniqueFields(dto, entityClass, excludeId);
                break;
            case CHIP:
                validateChipUniqueFields(dto, entityClass, excludeId);
                break;
            case IMPRESSORA:
                validateImpressoraUniqueFields(dto, entityClass, excludeId);
                break;
            case NOTEBOOK:
            case DESKTOP:
                validateComputadorUniqueFields(dto, entityClass, excludeId);
                break;
            // Monitor não tem campos únicos específicos
        }
    }

    private void validateCelularUniqueFields(Object dto, Class<? extends Equipamento> entityClass, Long excludeId) {
        String imei = getFieldFromDTO(dto, "imei");
        String imei2 = getFieldFromDTO(dto, "imei2");
        String eid = getFieldFromDTO(dto, "eid");

        if (imei != null && repositoryFactory.existsUniqueField(entityClass, "imei", imei, excludeId)) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o IMEI: " + imei);
        }

        if (imei2 != null && repositoryFactory.existsUniqueField(entityClass, "imei2", imei2, excludeId)) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o IMEI2: " + imei2);
        }

        if (eid != null && repositoryFactory.existsUniqueField(entityClass, "eid", eid, excludeId)) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o EID: " + eid);
        }
    }

    private void validateChipUniqueFields(Object dto, Class<? extends Equipamento> entityClass, Long excludeId) {
        String iccid = getFieldFromDTO(dto, "iccid");
        String numeroTelefone = getFieldFromDTO(dto, "numeroTelefone");

        if (iccid != null && repositoryFactory.existsUniqueField(entityClass, "iccid", iccid, excludeId)) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o ICCID: " + iccid);
        }

        if (numeroTelefone != null && repositoryFactory.existsUniqueField(entityClass, "numeroTelefone", numeroTelefone, excludeId)) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o número de telefone: " + numeroTelefone);
        }
    }

    private void validateImpressoraUniqueFields(Object dto, Class<? extends Equipamento> entityClass, Long excludeId) {
        String enderecoIP = getFieldFromDTO(dto, "enderecoIP");

        if (enderecoIP != null && repositoryFactory.existsUniqueField(entityClass, "enderecoIP", enderecoIP, excludeId)) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o endereço IP: " + enderecoIP);
        }
    }

    private void validateComputadorUniqueFields(Object dto, Class<? extends Equipamento> entityClass, Long excludeId) {
        String hostname = getFieldFromDTO(dto, "hostname");

        if (hostname != null && repositoryFactory.existsUniqueField(entityClass, "hostname", hostname, excludeId)) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o hostname: " + hostname);
        }
    }

    // Métodos auxiliares para extrair campos dos DTOs
    private String getNumeroPatrimonioFromDTO(Object dto) {
        try {
            return (String) dto.getClass().getMethod("getNumeroPatrimonio").invoke(dto);
        } catch (Exception e) {
            return null;
        }
    }

    private String getSerialNumberFromDTO(Object dto) {
        try {
            return (String) dto.getClass().getMethod("getSerialNumber").invoke(dto);
        } catch (Exception e) {
            return null;
        }
    }

    private String getFieldFromDTO(Object dto, String fieldName) {
        try {
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            return (String) dto.getClass().getMethod(methodName).invoke(dto);
        } catch (Exception e) {
            return null;
        }
    }

    // Métodos legados - marcados como deprecated para migração gradual
    @Deprecated
    public EquipamentoResponseDTO save(Object requestDTO) {
        throw new UnsupportedOperationException("Use os métodos específicos de criação por tipo (createNotebook, createCelular, etc.)");
    }

    @Deprecated
    public EquipamentoResponseDTO update(Long id, Object requestDTO) {
        throw new UnsupportedOperationException("Use os métodos específicos de atualização por tipo (updateNotebook, updateCelular, etc.)");
    }
}
