package com.cosmo.cosmo.service;

import com.cosmo.cosmo.dto.DepartamentoRequestDTO;
import com.cosmo.cosmo.dto.DepartamentoResponseDTO;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.mapper.DepartamentoMapper;
import com.cosmo.cosmo.repository.DepartamentoRepository;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private DepartamentoMapper departamentoMapper;

    public List<DepartamentoResponseDTO> findAll() {
        return departamentoRepository.findAll()
                .stream()
                .map(departamentoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DepartamentoResponseDTO findById(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com id: " + id));
        return departamentoMapper.toResponseDTO(departamento);
    }

    public DepartamentoResponseDTO save(DepartamentoRequestDTO requestDTO) {
        Departamento departamento = departamentoMapper.toEntity(requestDTO);
        departamento = departamentoRepository.save(departamento);
        return departamentoMapper.toResponseDTO(departamento);
    }

    public DepartamentoResponseDTO update(Long id, DepartamentoRequestDTO requestDTO) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com id: " + id));

        departamentoMapper.updateEntityFromDTO(requestDTO, departamento);
        departamento = departamentoRepository.save(departamento);
        return departamentoMapper.toResponseDTO(departamento);
    }

    public void deleteById(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com id: " + id));
        departamentoRepository.delete(departamento);
    }

    // Método auxiliar para outros services
    public Departamento findEntityById(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com id: " + id));
    }
}