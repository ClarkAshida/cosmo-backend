package com.cosmo.cosmo.service;

import com.cosmo.cosmo.dto.EquipamentoRequestDTO;
import com.cosmo.cosmo.dto.EquipamentoResponseDTO;
import com.cosmo.cosmo.entity.Equipamento;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.mapper.EquipamentoMapper;
import com.cosmo.cosmo.repository.EquipamentoRepository;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private EquipamentoMapper equipamentoMapper;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private EmpresaService empresaService;

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
        Empresa empresa = empresaService.findEntityById(requestDTO.getEmpresaId());
        Departamento departamento = departamentoService.findEntityById(requestDTO.getDepartamentoId());

        Equipamento equipamento = equipamentoMapper.toEntity(requestDTO, empresa, departamento);
        equipamento = equipamentoRepository.save(equipamento);
        return equipamentoMapper.toResponseDTO(equipamento);
    }

    public EquipamentoResponseDTO update(Long id, EquipamentoRequestDTO requestDTO) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado com id: " + id));

        Empresa empresa = empresaService.findEntityById(requestDTO.getEmpresaId());
        Departamento departamento = departamentoService.findEntityById(requestDTO.getDepartamentoId());

        equipamentoMapper.updateEntityFromDTO(requestDTO, equipamento, empresa, departamento);
        equipamento = equipamentoRepository.save(equipamento);
        return equipamentoMapper.toResponseDTO(equipamento);
    }

    public void deleteById(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado com id: " + id));
        equipamentoRepository.delete(equipamento);
    }

    // Método auxiliar para outros services
    public Equipamento findEntityById(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado com id: " + id));
    }
}
