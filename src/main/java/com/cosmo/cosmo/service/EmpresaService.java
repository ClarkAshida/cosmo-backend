package com.cosmo.cosmo.service;

import com.cosmo.cosmo.controller.EmpresaController;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaMapper empresaMapper;

    public List<EmpresaResponseDTO> findAll() {
        return empresaRepository.findAll()
                .stream()
                .map(empresa -> {
                    EmpresaResponseDTO dto = empresaMapper.toResponseDTO(empresa);
                    return addHateoasLinks(dto);
                })
                .collect(Collectors.toList());
    }

    public EmpresaResponseDTO findById(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));
        EmpresaResponseDTO dto = empresaMapper.toResponseDTO(empresa);
        return addHateoasLinks(dto);
    }

    public EmpresaResponseDTO save(EmpresaRequestDTO requestDTO) {
        Empresa empresa = empresaMapper.toEntity(requestDTO);
        empresa = empresaRepository.save(empresa);
        EmpresaResponseDTO dto = empresaMapper.toResponseDTO(empresa);
        return addHateoasLinks(dto);
    }

    public EmpresaResponseDTO update(Long id, EmpresaRequestDTO requestDTO) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com id: " + id));

        empresaMapper.updateEntityFromDTO(requestDTO, empresa);
        empresa = empresaRepository.save(empresa);
        EmpresaResponseDTO dto = empresaMapper.toResponseDTO(empresa);
        return addHateoasLinks(dto);
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

    private EmpresaResponseDTO addHateoasLinks(EmpresaResponseDTO dto) {
        Long id = dto.getId();

        // Link para si mesmo
        dto.add(linkTo(methodOn(EmpresaController.class).getEmpresaById(id)).withSelfRel());

        // Link para listar todas as empresas
        dto.add(linkTo(methodOn(EmpresaController.class).getAllEmpresas()).withRel("empresas"));

        // Link para atualizar
        dto.add(linkTo(methodOn(EmpresaController.class).updateEmpresa(id, null)).withRel("update"));

        // Link para deletar
        dto.add(linkTo(methodOn(EmpresaController.class).deleteEmpresa(id)).withRel("delete"));

        return dto;
    }
}