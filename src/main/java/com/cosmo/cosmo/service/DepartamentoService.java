package com.cosmo.cosmo.service;

import com.cosmo.cosmo.controller.DepartamentoController;
import com.cosmo.cosmo.dto.departamento.DepartamentoRequestDTO;
import com.cosmo.cosmo.dto.departamento.DepartamentoResponseDTO;
import com.cosmo.cosmo.dto.geral.PagedResponseDTO;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.mapper.DepartamentoMapper;
import com.cosmo.cosmo.repository.DepartamentoRepository;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private DepartamentoMapper departamentoMapper;

    public PagedResponseDTO<DepartamentoResponseDTO> findAll(Pageable pageable) {
        Page<Departamento> page = departamentoRepository.findAll(pageable);

        List<DepartamentoResponseDTO> departamentos = page.getContent()
                .stream()
                .map(departamento -> {
                    DepartamentoResponseDTO dto = departamentoMapper.toResponseDTO(departamento);
                    return addHateoasLinks(dto);
                })
                .collect(Collectors.toList());

        // Criar o embedded map
        Map<String, List<DepartamentoResponseDTO>> embedded = new HashMap<>();
        embedded.put("departamentos", departamentos);

        // Criar informações da página
        PagedResponseDTO.PageInfo pageInfo = new PagedResponseDTO.PageInfo(
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );

        PagedResponseDTO<DepartamentoResponseDTO> response = new PagedResponseDTO<>(embedded, pageInfo);

        // Adicionar links de navegação HAL
        addPaginationLinks(response, pageable, page);

        return response;
    }

    public DepartamentoResponseDTO findById(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com id: " + id));
        DepartamentoResponseDTO dto = departamentoMapper.toResponseDTO(departamento);
        return addHateoasLinks(dto);
    }

    public DepartamentoResponseDTO save(DepartamentoRequestDTO requestDTO) {
        Departamento departamento = departamentoMapper.toEntity(requestDTO);
        departamento = departamentoRepository.save(departamento);
        DepartamentoResponseDTO dto = departamentoMapper.toResponseDTO(departamento);
        return addHateoasLinks(dto);
    }

    public DepartamentoResponseDTO update(Long id, DepartamentoRequestDTO requestDTO) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com id: " + id));

        departamentoMapper.updateEntityFromDTO(requestDTO, departamento);
        departamento = departamentoRepository.save(departamento);
        DepartamentoResponseDTO dto = departamentoMapper.toResponseDTO(departamento);
        return addHateoasLinks(dto);
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

    private DepartamentoResponseDTO addHateoasLinks(DepartamentoResponseDTO dto) {
        Long id = dto.getId();

        // Link para si mesmo
        dto.add(linkTo(methodOn(DepartamentoController.class).getDepartamentoById(id)).withSelfRel());

        // Link para listar todos os departamentos
        dto.add(linkTo(methodOn(DepartamentoController.class)
                .getAllDepartamentos(0, 10, "nome", "asc")).withRel("departamentos"));

        // Link para atualizar
        dto.add(linkTo(methodOn(DepartamentoController.class).updateDepartamento(id, null)).withRel("update"));

        // Link para deletar
        dto.add(linkTo(methodOn(DepartamentoController.class).deleteDepartamento(id)).withRel("delete"));

        return dto;
    }

    private void addPaginationLinks(PagedResponseDTO<DepartamentoResponseDTO> response, Pageable pageable, Page<?> page) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().hasNext() ?
                pageable.getSort().iterator().next().getProperty() : "nome";
        String sortDir = pageable.getSort().iterator().hasNext() ?
                (pageable.getSort().iterator().next().isAscending() ? "asc" : "desc") : "asc";

        // Link para a página atual (self)
        response.add(linkTo(methodOn(DepartamentoController.class)
                .getAllDepartamentos(currentPage, pageSize, sortBy, sortDir)).withSelfRel());

        // Link para primeira página
        response.add(linkTo(methodOn(DepartamentoController.class)
                .getAllDepartamentos(0, pageSize, sortBy, sortDir)).withRel("first"));

        // Link para última página
        response.add(linkTo(methodOn(DepartamentoController.class)
                .getAllDepartamentos(page.getTotalPages() - 1, pageSize, sortBy, sortDir)).withRel("last"));

        // Link para página anterior (se não for a primeira)
        if (page.hasPrevious()) {
            response.add(linkTo(methodOn(DepartamentoController.class)
                    .getAllDepartamentos(currentPage - 1, pageSize, sortBy, sortDir)).withRel("prev"));
        }

        // Link para próxima página (se não for a última)
        if (page.hasNext()) {
            response.add(linkTo(methodOn(DepartamentoController.class)
                    .getAllDepartamentos(currentPage + 1, pageSize, sortBy, sortDir)).withRel("next"));
        }
    }
}