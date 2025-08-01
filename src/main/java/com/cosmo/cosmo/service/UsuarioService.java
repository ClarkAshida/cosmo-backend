package com.cosmo.cosmo.service;

import com.cosmo.cosmo.dto.UsuarioRequestDTO;
import com.cosmo.cosmo.dto.UsuarioResponseDTO;
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
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
        return usuarioMapper.toResponseDTO(usuario);
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
        return usuarioMapper.toResponseDTO(usuario);
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
        return usuarioMapper.toResponseDTO(usuario);
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
        return usuarioMapper.toResponseDTO(usuario);
    }

    // Método auxiliar para outros services
    public Usuario findEntityById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }
}