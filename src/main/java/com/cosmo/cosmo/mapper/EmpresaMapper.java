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

import com.cosmo.cosmo.dto.empresa.EmpresaRequestDTO;
import com.cosmo.cosmo.dto.empresa.EmpresaResponseDTO;
import com.cosmo.cosmo.entity.Empresa;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public Empresa toEntity(EmpresaRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        Empresa empresa = new Empresa();
        empresa.setNome(requestDTO.getNome());
        empresa.setEstado(requestDTO.getEstado());
        return empresa;
    }

    public EmpresaResponseDTO toResponseDTO(Empresa empresa) {
        if (empresa == null) {
            return null;
        }

        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getEstado()
        );
    }

    public void updateEntityFromDTO(EmpresaRequestDTO requestDTO, Empresa empresa) {
        if (requestDTO == null || empresa == null) {
            return;
        }

        empresa.setNome(requestDTO.getNome());
        empresa.setEstado(requestDTO.getEstado());
    }
}
