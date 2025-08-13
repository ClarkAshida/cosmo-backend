package com.cosmo.cosmo.service;

import com.cosmo.cosmo.controller.UsuarioController;
import com.cosmo.cosmo.dto.geral.PagedResponseDTO;
import com.cosmo.cosmo.dto.usuario.UsuarioRequestDTO;
import com.cosmo.cosmo.dto.usuario.UsuarioResponseDTO;
import com.cosmo.cosmo.dto.departamento.DepartamentoResponseDTO;
import com.cosmo.cosmo.dto.empresa.EmpresaResponseDTO;
import com.cosmo.cosmo.entity.Usuario;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.mapper.UsuarioMapper;
import com.cosmo.cosmo.repository.UsuarioRepository;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.exception.DuplicateResourceException;
import com.cosmo.cosmo.specification.UsuarioSpecification;
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
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private EmpresaService empresaService;

    public PagedResponseDTO<UsuarioResponseDTO> findAll(Pageable pageable) {
        Page<Usuario> page = usuarioRepository.findAll(pageable);

        List<UsuarioResponseDTO> usuarios = page.getContent()
                .stream()
                .map(usuario -> {
                    UsuarioResponseDTO dto = usuarioMapper.toResponseDTO(usuario);
                    return addHateoasLinksWithNestedEntities(dto);
                })
                .collect(Collectors.toList());

        // Criar o embedded map
        Map<String, List<UsuarioResponseDTO>> embedded = new HashMap<>();
        embedded.put("usuarios", usuarios);

        // Criar informações da página
        PagedResponseDTO.PageInfo pageInfo = new PagedResponseDTO.PageInfo(
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );

        PagedResponseDTO<UsuarioResponseDTO> response = new PagedResponseDTO<>(embedded, pageInfo);

        // Adicionar links de navegação HAL
        addPaginationLinks(response, pageable, page);

        return response;
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
        UsuarioResponseDTO dto = usuarioMapper.toResponseDTO(usuario);
        return addHateoasLinksWithNestedEntities(dto);
    }

    public UsuarioResponseDTO save(UsuarioRequestDTO requestDTO) {
        // Validar se email já existe
        if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Já existe um usuário cadastrado com o email: " + requestDTO.getEmail());
        }

        // Validar se CPF já existe
        if (usuarioRepository.existsByCpf(requestDTO.getCpf())) {
            throw new DuplicateResourceException("Já existe um usuário cadastrado com o CPF: " + requestDTO.getCpf());
        }

        Departamento departamento = departamentoService.findEntityById(requestDTO.getDepartamentoId());
        Empresa empresa = empresaService.findEntityById(requestDTO.getEmpresaId());

        Usuario usuario = usuarioMapper.toEntity(requestDTO, departamento, empresa);
        usuario = usuarioRepository.save(usuario);
        UsuarioResponseDTO dto = usuarioMapper.toResponseDTO(usuario);
        return addHateoasLinksWithNestedEntities(dto);
    }

    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));

        // Validar se email já existe em outro usuário
        if (usuarioRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new DuplicateResourceException("Já existe outro usuário cadastrado com o email: " + requestDTO.getEmail());
        }

        // Validar se CPF já existe em outro usuário
        if (usuarioRepository.existsByCpfAndIdNot(requestDTO.getCpf(), id)) {
            throw new DuplicateResourceException("Já existe outro usuário cadastrado com o CPF: " + requestDTO.getCpf());
        }

        Departamento departamento = departamentoService.findEntityById(requestDTO.getDepartamentoId());
        Empresa empresa = empresaService.findEntityById(requestDTO.getEmpresaId());

        usuarioMapper.updateEntityFromDTO(requestDTO, usuario, departamento, empresa);
        usuario = usuarioRepository.save(usuario);
        UsuarioResponseDTO dto = usuarioMapper.toResponseDTO(usuario);
        return addHateoasLinksWithNestedEntities(dto);
    }

    public void deactivateById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public UsuarioResponseDTO reactivateById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));

        usuario.setAtivo(true);
        usuario = usuarioRepository.save(usuario);
        UsuarioResponseDTO dto = usuarioMapper.toResponseDTO(usuario);
        return addHateoasLinksWithNestedEntities(dto);
    }

    // Método auxiliar para outros services
    public Usuario findEntityById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado com id: " + id));
    }

    public PagedResponseDTO<UsuarioResponseDTO> filtrarUsuarios(String nome, String email, String cpf, Pageable pageable) {
        Specification<Usuario> spec = UsuarioSpecification.comFiltros(nome, email, cpf);
        Page<Usuario> page = usuarioRepository.findAll(spec, pageable);

        List<UsuarioResponseDTO> usuarios = page.getContent()
                .stream()
                .map(usuario -> {
                    UsuarioResponseDTO dto = usuarioMapper.toResponseDTO(usuario);
                    return addHateoasLinksWithNestedEntities(dto);
                })
                .collect(Collectors.toList());

        // Criar o embedded map
        Map<String, List<UsuarioResponseDTO>> embedded = new HashMap<>();
        embedded.put("usuarios", usuarios);

        // Criar informações da página
        PagedResponseDTO.PageInfo pageInfo = new PagedResponseDTO.PageInfo(
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );

        PagedResponseDTO<UsuarioResponseDTO> response = new PagedResponseDTO<>(embedded, pageInfo);

        // Adicionar links de navegação HAL
        addPaginationLinksForFiltro(response, pageable, page, nome, email, cpf);

        return response;
    }

    private UsuarioResponseDTO addHateoasLinksWithNestedEntities(UsuarioResponseDTO dto) {
        // Adicionar links HATEOAS para o usuário principal
        addHateoasLinks(dto);

        // Adicionar links HATEOAS para o departamento aninhado
        if (dto.getDepartamento() != null) {
            DepartamentoResponseDTO departamentoComLinks = departamentoService.findById(dto.getDepartamento().getId());
            dto.setDepartamento(departamentoComLinks);
        }

        // Adicionar links HATEOAS para a empresa aninhada
        if (dto.getEmpresa() != null) {
            EmpresaResponseDTO empresaComLinks = empresaService.findById(dto.getEmpresa().getId());
            dto.setEmpresa(empresaComLinks);
        }

        return dto;
    }

    private void addPaginationLinks(PagedResponseDTO<UsuarioResponseDTO> response, Pageable pageable, Page<?> page) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().hasNext() ?
            pageable.getSort().iterator().next().getProperty() : "nome";
        String sortDir = pageable.getSort().iterator().hasNext() ?
            (pageable.getSort().iterator().next().isAscending() ? "asc" : "desc") : "asc";

        // Link para a página atual (self)
        response.add(linkTo(methodOn(UsuarioController.class)
                .getAllUsuarios(currentPage, pageSize, sortBy, sortDir)).withSelfRel());

        // Link para primeira página
        response.add(linkTo(methodOn(UsuarioController.class)
                .getAllUsuarios(0, pageSize, sortBy, sortDir)).withRel("first"));

        // Link para última página
        response.add(linkTo(methodOn(UsuarioController.class)
                .getAllUsuarios(page.getTotalPages() - 1, pageSize, sortBy, sortDir)).withRel("last"));

        // Link para página anterior (se não for a primeira)
        if (page.hasPrevious()) {
            response.add(linkTo(methodOn(UsuarioController.class)
                    .getAllUsuarios(currentPage - 1, pageSize, sortBy, sortDir)).withRel("prev"));
        }

        // Link para próxima página (se não for a última)
        if (page.hasNext()) {
            response.add(linkTo(methodOn(UsuarioController.class)
                    .getAllUsuarios(currentPage + 1, pageSize, sortBy, sortDir)).withRel("next"));
        }
    }

    private void addPaginationLinksForFiltro(PagedResponseDTO<UsuarioResponseDTO> response, Pageable pageable, Page<?> page, String nome, String email, String cpf) {
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = pageable.getSort().iterator().hasNext() ?
                pageable.getSort().iterator().next().getProperty() : "nome";
        String sortDir = pageable.getSort().iterator().hasNext() ?
                (pageable.getSort().iterator().next().isAscending() ? "asc" : "desc") : "asc";

        // Link para a página atual (self)
        response.add(linkTo(methodOn(UsuarioController.class)
                .filtrarUsuarios(nome, email, cpf, currentPage, pageSize, sortBy, sortDir)).withSelfRel());

        // Link para primeira página
        response.add(linkTo(methodOn(UsuarioController.class)
                .filtrarUsuarios(nome, email, cpf, 0, pageSize, sortBy, sortDir)).withRel("first"));

        // Link para última página
        response.add(linkTo(methodOn(UsuarioController.class)
                .filtrarUsuarios(nome, email, cpf, page.getTotalPages() - 1, pageSize, sortBy, sortDir)).withRel("last"));

        // Link para página anterior (se não for a primeira)
        if (page.hasPrevious()) {
            response.add(linkTo(methodOn(UsuarioController.class)
                    .filtrarUsuarios(nome, email, cpf, currentPage - 1, pageSize, sortBy, sortDir)).withRel("prev"));
        }

        // Link para próxima página (se não for a última)
        if (page.hasNext()) {
            response.add(linkTo(methodOn(UsuarioController.class)
                    .filtrarUsuarios(nome, email, cpf, currentPage + 1, pageSize, sortBy, sortDir)).withRel("next"));
        }

        // Link para todos os usuarios
        response.add(linkTo(methodOn(UsuarioController.class)
                .getAllUsuarios(0, 10, "nome", "asc")).withRel("todos-usuarios"));
    }

    private UsuarioResponseDTO addHateoasLinks(UsuarioResponseDTO dto) {
        Long id = dto.getId();

        // Link para si mesmo
        dto.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(id)).withSelfRel());

        // Link para listar todos os usuários
        dto.add(linkTo(methodOn(UsuarioController.class)
                .getAllUsuarios(0, 10, "nome", "asc")).withRel("usuarios"));

        // Link para atualizar
        dto.add(linkTo(methodOn(UsuarioController.class).updateUsuario(id, null)).withRel("update"));

        // Link para deletar (desativar)
        dto.add(linkTo(methodOn(UsuarioController.class).deleteUsuario(id)).withRel("delete"));

        // Link para reativar apenas se o usuário estiver inativo
        if (!dto.getAtivo()) {
            dto.add(linkTo(methodOn(UsuarioController.class).reativarUsuario(id)).withRel("reativar"));
        }

        return dto;
    }
}
