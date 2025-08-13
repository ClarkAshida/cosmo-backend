package com.cosmo.cosmo.service;

import com.cosmo.cosmo.controller.EmpresaController;
import com.cosmo.cosmo.dto.empresa.EmpresaRequestDTO;
import com.cosmo.cosmo.dto.empresa.EmpresaResponseDTO;
import com.cosmo.cosmo.dto.geral.PagedResponseDTO;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.mapper.EmpresaMapper;
import com.cosmo.cosmo.repository.EmpresaRepository;
import com.cosmo.cosmo.specification.EmpresaSpecification;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaMapper empresaMapper;

    public PagedResponseDTO<EmpresaResponseDTO> findAll(Pageable pageable) {
        Page<Empresa> page = empresaRepository.findAll(pageable);

        List<EmpresaResponseDTO> empresas = page.getContent()
                .stream()
                .map(empresa -> {
                    EmpresaResponseDTO dto = empresaMapper.toResponseDTO(empresa);
                    return addHateoasLinks(dto);
                })
                .collect(Collectors.toList());

        // Criar o embedded map
        Map<String, List<EmpresaResponseDTO>> embedded = new HashMap<>();
        embedded.put("empresas", empresas);

        // Criar informações da página
        PagedResponseDTO.PageInfo pageInfo = new PagedResponseDTO.PageInfo(
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );

        PagedResponseDTO<EmpresaResponseDTO> response = new PagedResponseDTO<>(embedded, pageInfo);

        // Adicionar links de navegação HAL
        addPaginationLinks(response, pageable, page);

        return response;
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

    public PagedResponseDTO<EmpresaResponseDTO> filtrarEmpresas(String nome, Pageable pageable) {
        Specification<Empresa> spec = EmpresaSpecification.comFiltros(nome);
        Page<Empresa> page = empresaRepository.findAll(spec, pageable);

        List<EmpresaResponseDTO> empresas = page.getContent()
                .stream()
                .map(empresa -> {
                    EmpresaResponseDTO dto = empresaMapper.toResponseDTO(empresa);
                    return addHateoasLinks(dto);
                })
                .collect(Collectors.toList());

        // Criar o embedded map
        Map<String, List<EmpresaResponseDTO>> embedded = new HashMap<>();
        embedded.put("empresas", empresas);

        // Criar informações da página
        PagedResponseDTO.PageInfo pageInfo = new PagedResponseDTO.PageInfo(
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );

        PagedResponseDTO<EmpresaResponseDTO> response = new PagedResponseDTO<>(embedded, pageInfo);

        // Adicionar links de navegação HAL
        addPaginationLinksForFiltro(response, pageable, page, nome);

        return response;
    }

    private EmpresaResponseDTO addHateoasLinks(EmpresaResponseDTO dto) {
        Long id = dto.getId();

        // Link para si mesmo
        dto.add(linkTo(methodOn(EmpresaController.class).getEmpresaById(id)).withSelfRel());

        // Link para listar todas as empresas
        dto.add(linkTo(methodOn(EmpresaController.class)
                .getAllEmpresas(0, 10, "nome", "asc")).withRel("empresas"));

        // Link para atualizar
        dto.add(linkTo(methodOn(EmpresaController.class).updateEmpresa(id, null)).withRel("update"));

        // Link para deletar
        dto.add(linkTo(methodOn(EmpresaController.class).deleteEmpresa(id)).withRel("delete"));

        return dto;
    }

    private void addPaginationLinks(PagedResponseDTO<EmpresaResponseDTO> response, Pageable pageable, Page<?> page) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().hasNext() ?
                pageable.getSort().iterator().next().getProperty() : "nome";
        String sortDir = pageable.getSort().iterator().hasNext() ?
                (pageable.getSort().iterator().next().isAscending() ? "asc" : "desc") : "asc";

        // Link para a página atual (self)
        response.add(linkTo(methodOn(EmpresaController.class)
                .getAllEmpresas(currentPage, pageSize, sortBy, sortDir)).withSelfRel());

        // Link para primeira página
        response.add(linkTo(methodOn(EmpresaController.class)
                .getAllEmpresas(0, pageSize, sortBy, sortDir)).withRel("first"));

        // Link para última página
        response.add(linkTo(methodOn(EmpresaController.class)
                .getAllEmpresas(page.getTotalPages() - 1, pageSize, sortBy, sortDir)).withRel("last"));

        // Link para página anterior (se não for a primeira)
        if (page.hasPrevious()) {
            response.add(linkTo(methodOn(EmpresaController.class)
                    .getAllEmpresas(currentPage - 1, pageSize, sortBy, sortDir)).withRel("prev"));
        }

        // Link para próxima página (se não for a última)
        if (page.hasNext()) {
            response.add(linkTo(methodOn(EmpresaController.class)
                    .getAllEmpresas(currentPage + 1, pageSize, sortBy, sortDir)).withRel("next"));
        }
    }

    private void addPaginationLinksForFiltro(PagedResponseDTO<EmpresaResponseDTO> response, Pageable pageable, Page<?> page, String nome) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().hasNext() ?
                pageable.getSort().iterator().next().getProperty() : "nome";
        String sortDir = pageable.getSort().iterator().hasNext() ?
                (pageable.getSort().iterator().next().isAscending() ? "asc" : "desc") : "asc";

        // Link para a página atual (self)
        response.add(linkTo(methodOn(EmpresaController.class)
                .filtrarEmpresas(nome, currentPage, pageSize, sortBy, sortDir)).withSelfRel());

        // Link para primeira página
        response.add(linkTo(methodOn(EmpresaController.class)
                .filtrarEmpresas(nome, 0, pageSize, sortBy, sortDir)).withRel("first"));

        // Link para última página
        response.add(linkTo(methodOn(EmpresaController.class)
                .filtrarEmpresas(nome, page.getTotalPages() - 1, pageSize, sortBy, sortDir)).withRel("last"));

        // Link para página anterior (se não for a primeira)
        if (page.hasPrevious()) {
            response.add(linkTo(methodOn(EmpresaController.class)
                    .filtrarEmpresas(nome, currentPage - 1, pageSize, sortBy, sortDir)).withRel("prev"));
        }

        // Link para próxima página (se não for a última)
        if (page.hasNext()) {
            response.add(linkTo(methodOn(EmpresaController.class)
                    .filtrarEmpresas(nome, currentPage + 1, pageSize, sortBy, sortDir)).withRel("next"));
        }

        // Link para todas as empresas
        response.add(linkTo(methodOn(EmpresaController.class)
                .getAllEmpresas(0, 10, "nome", "asc")).withRel("todas-empresas"));
    }
}