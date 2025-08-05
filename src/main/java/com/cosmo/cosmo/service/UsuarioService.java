package com.cosmo.cosmo.service;

import com.cosmo.cosmo.controller.UsuarioController;
import com.cosmo.cosmo.dto.UsuarioRequestDTO;
import com.cosmo.cosmo.dto.UsuarioResponseDTO;
import com.cosmo.cosmo.dto.DepartamentoResponseDTO;
import com.cosmo.cosmo.dto.EmpresaResponseDTO;
import com.cosmo.cosmo.entity.Usuario;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.mapper.UsuarioMapper;
import com.cosmo.cosmo.repository.UsuarioRepository;
import com.cosmo.cosmo.exception.ResourceNotFoundException;
import com.cosmo.cosmo.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuario -> {
                    UsuarioResponseDTO dto = usuarioMapper.toResponseDTO(usuario);
                    return addHateoasLinksWithNestedEntities(dto);
                })
                .collect(Collectors.toList());
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
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
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

    private UsuarioResponseDTO addHateoasLinks(UsuarioResponseDTO dto) {
        Long id = dto.getId();

        // Link para si mesmo
        dto.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(id)).withSelfRel());

        // Link para listar todos os usuários
        dto.add(linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"));

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