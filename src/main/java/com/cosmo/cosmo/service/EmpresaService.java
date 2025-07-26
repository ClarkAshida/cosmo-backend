package com.cosmo.cosmo.service;

import com.cosmo.cosmo.dto.EmpresaRequestDTO;
import com.cosmo.cosmo.dto.EmpresaResponseDTO;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.mapper.EmpresaMapper;
import com.cosmo.cosmo.repository.EmpresaRepository;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaMapper empresaMapper;

    public List<EmpresaResponseDTO> findAll() {
        return empresaRepository.findAll()
                .stream()
                .map(empresaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EmpresaResponseDTO findById(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));
        return empresaMapper.toResponseDTO(empresa);
    }

    public EmpresaResponseDTO save(EmpresaRequestDTO requestDTO) {
        Empresa empresa = empresaMapper.toEntity(requestDTO);
        empresa = empresaRepository.save(empresa);
        return empresaMapper.toResponseDTO(empresa);
    }

    public EmpresaResponseDTO update(Long id, EmpresaRequestDTO requestDTO) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));

        empresaMapper.updateEntityFromDTO(requestDTO, empresa);
        empresa = empresaRepository.save(empresa);
        return empresaMapper.toResponseDTO(empresa);
    }

    public void deleteById(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));
        empresaRepository.delete(empresa);
    }

    // Método auxiliar para outros services
    public Empresa findEntityById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));
    }
}