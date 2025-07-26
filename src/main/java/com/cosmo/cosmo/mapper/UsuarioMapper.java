package com.cosmo.cosmo.mapper;

import com.cosmo.cosmo.dto.UsuarioRequestDTO;
import com.cosmo.cosmo.dto.UsuarioResponseDTO;
import com.cosmo.cosmo.entity.Usuario;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    @Autowired
    private DepartamentoMapper departamentoMapper;

    @Autowired
    private EmpresaMapper empresaMapper;

    public Usuario toEntity(UsuarioRequestDTO requestDTO, Departamento departamento, Empresa empresa) {
        if (requestDTO == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(requestDTO.getNome());
        usuario.setEmail(requestDTO.getEmail());
        usuario.setCargo(requestDTO.getCargo());
        usuario.setCpf(requestDTO.getCpf());
        usuario.setCidade(requestDTO.getCidade());
        usuario.setDepartamento(departamento);
        usuario.setEmpresa(empresa);
        usuario.setAtivo(requestDTO.getAtivo() != null ? requestDTO.getAtivo() : true);
        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCargo(),
                usuario.getCpf(),
                usuario.getCidade(),
                departamentoMapper.toResponseDTO(usuario.getDepartamento()),
                empresaMapper.toResponseDTO(usuario.getEmpresa()),
                usuario.getAtivo()
        );
    }

    public void updateEntityFromDTO(UsuarioRequestDTO requestDTO, Usuario usuario,
                                   Departamento departamento, Empresa empresa) {
        if (requestDTO == null || usuario == null) {
            return;
        }

        usuario.setNome(requestDTO.getNome());
        usuario.setEmail(requestDTO.getEmail());
        usuario.setCargo(requestDTO.getCargo());
        usuario.setCpf(requestDTO.getCpf());
        usuario.setCidade(requestDTO.getCidade());
        usuario.setDepartamento(departamento);
        usuario.setEmpresa(empresa);
        usuario.setAtivo(requestDTO.getAtivo() != null ? requestDTO.getAtivo() : true);
    }
}
