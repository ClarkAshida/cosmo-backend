package com.cosmo.cosmo.service;

import com.cosmo.cosmo.dto.EquipamentoRequestDTO;
import com.cosmo.cosmo.dto.EquipamentoResponseDTO;
import com.cosmo.cosmo.entity.Equipamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.mapper.EquipamentoMapper;
import com.cosmo.cosmo.repository.EquipamentoRepository;
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

    public EquipamentoResponseDTO save(EquipamentoRequestDTO requestDTO) {
        // Validar campos únicos antes de salvar
        validateUniqueFieldsForCreation(requestDTO);

        Empresa empresa = empresaService.findEntityById(requestDTO.getEmpresaId());
        Departamento departamento = departamentoService.findEntityById(requestDTO.getDepartamentoId());

        Equipamento equipamento = equipamentoMapper.toEntity(requestDTO, empresa, departamento);
        equipamento = equipamentoRepository.save(equipamento);
        return equipamentoMapper.toResponseDTO(equipamento);
    }

    public EquipamentoResponseDTO update(Long id, EquipamentoRequestDTO requestDTO) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado com id: " + id));

        // Validar campos únicos antes de atualizar (excluindo o próprio equipamento)
        validateUniqueFieldsForUpdate(requestDTO, id);

        Empresa empresa = empresaService.findEntityById(requestDTO.getEmpresaId());
        Departamento departamento = departamentoService.findEntityById(requestDTO.getDepartamentoId());

        equipamentoMapper.updateEntityFromDTO(requestDTO, equipamento, empresa, departamento);
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

    /**
     * Valida se os campos únicos não estão duplicados na criação de um novo equipamento
     */
    private void validateUniqueFieldsForCreation(EquipamentoRequestDTO requestDTO) {
        // Validar número de patrimônio
        if (requestDTO.getNumeroPatrimonio() != null &&
            equipamentoRepository.existsByNumeroPatrimonio(requestDTO.getNumeroPatrimonio())) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o número de patrimônio: " + requestDTO.getNumeroPatrimonio());
        }

        // Validar serial number
        if (requestDTO.getSerialNumber() != null &&
            equipamentoRepository.existsBySerialNumber(requestDTO.getSerialNumber())) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o serial number: " + requestDTO.getSerialNumber());
        }

        // Validar IMEI
        if (requestDTO.getImei() != null &&
            equipamentoRepository.existsByImei(requestDTO.getImei())) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o IMEI: " + requestDTO.getImei());
        }

        // Validar EID
        if (requestDTO.getEid() != null &&
            equipamentoRepository.existsByEid(requestDTO.getEid())) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o EID: " + requestDTO.getEid());
        }

        // Validar número de telefone
        if (requestDTO.getNumeroTelefone() != null &&
            equipamentoRepository.existsByNumeroTelefone(requestDTO.getNumeroTelefone())) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o número de telefone: " + requestDTO.getNumeroTelefone());
        }

        // Validar ICCID
        if (requestDTO.getIccid() != null &&
            equipamentoRepository.existsByIccid(requestDTO.getIccid())) {
            throw new DuplicateResourceException("Já existe um equipamento cadastrado com o ICCID: " + requestDTO.getIccid());
        }
    }

    /**
     * Valida se os campos únicos não estão duplicados na atualização de um equipamento existente
     */
    private void validateUniqueFieldsForUpdate(EquipamentoRequestDTO requestDTO, Long equipamentoId) {
        // Validar número de patrimônio
        if (requestDTO.getNumeroPatrimonio() != null &&
            equipamentoRepository.existsByNumeroPatrimonioAndIdNot(requestDTO.getNumeroPatrimonio(), equipamentoId)) {
            throw new DuplicateResourceException("Já existe outro equipamento cadastrado com o número de patrimônio: " + requestDTO.getNumeroPatrimonio());
        }

        // Validar serial number
        if (requestDTO.getSerialNumber() != null &&
            equipamentoRepository.existsBySerialNumberAndIdNot(requestDTO.getSerialNumber(), equipamentoId)) {
            throw new DuplicateResourceException("Já existe outro equipamento cadastrado com o serial number: " + requestDTO.getSerialNumber());
        }

        // Validar IMEI
        if (requestDTO.getImei() != null &&
            equipamentoRepository.existsByImeiAndIdNot(requestDTO.getImei(), equipamentoId)) {
            throw new DuplicateResourceException("Já existe outro equipamento cadastrado com o IMEI: " + requestDTO.getImei());
        }

        // Validar EID
        if (requestDTO.getEid() != null &&
            equipamentoRepository.existsByEidAndIdNot(requestDTO.getEid(), equipamentoId)) {
            throw new DuplicateResourceException("Já existe outro equipamento cadastrado com o EID: " + requestDTO.getEid());
        }

        // Validar número de telefone
        if (requestDTO.getNumeroTelefone() != null &&
            equipamentoRepository.existsByNumeroTelefoneAndIdNot(requestDTO.getNumeroTelefone(), equipamentoId)) {
            throw new DuplicateResourceException("Já existe outro equipamento cadastrado com o número de telefone: " + requestDTO.getNumeroTelefone());
        }

        // Validar ICCID
        if (requestDTO.getIccid() != null &&
            equipamentoRepository.existsByIccidAndIdNot(requestDTO.getIccid(), equipamentoId)) {
            throw new DuplicateResourceException("Já existe outro equipamento cadastrado com o ICCID: " + requestDTO.getIccid());
        }
    }
}
