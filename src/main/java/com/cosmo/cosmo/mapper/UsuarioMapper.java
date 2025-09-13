/*
 * Copyright 2025 Flávio Alexandre Orrico Severiano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cosmo.cosmo.mapper;

import com.cosmo.cosmo.dto.usuario.UsuarioRequestDTO;
import com.cosmo.cosmo.dto.usuario.UsuarioResponseDTO;
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
