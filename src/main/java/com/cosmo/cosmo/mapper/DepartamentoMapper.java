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

import com.cosmo.cosmo.dto.departamento.DepartamentoRequestDTO;
import com.cosmo.cosmo.dto.departamento.DepartamentoResponseDTO;
import com.cosmo.cosmo.entity.Departamento;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoMapper {

    public Departamento toEntity(DepartamentoRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        Departamento departamento = new Departamento();
        departamento.setNome(requestDTO.getNome());
        return departamento;
    }

    public DepartamentoResponseDTO toResponseDTO(Departamento departamento) {
        if (departamento == null) {
            return null;
        }

        return new DepartamentoResponseDTO(
                departamento.getId(),
                departamento.getNome()
        );
    }

    public void updateEntityFromDTO(DepartamentoRequestDTO requestDTO, Departamento departamento) {
        if (requestDTO == null || departamento == null) {
            return;
        }

        departamento.setNome(requestDTO.getNome());
    }
}
