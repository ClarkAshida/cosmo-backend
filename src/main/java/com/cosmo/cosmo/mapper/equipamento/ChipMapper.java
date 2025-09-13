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
package com.cosmo.cosmo.mapper.equipamento;

import com.cosmo.cosmo.dto.equipamento.ChipCreateDTO;
import com.cosmo.cosmo.dto.equipamento.ChipUpdateDTO;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.entity.equipamento.Chip;
import org.springframework.stereotype.Component;

@Component
public class ChipMapper {

    public Chip toEntity(ChipCreateDTO createDTO, Empresa empresa, Departamento departamento) {
        if (createDTO == null) {
            return null;
        }

        Chip chip = new Chip();

        // Mapear campos comuns de Equipamento
        mapCommonFields(createDTO, chip, empresa, departamento);

        // Mapear campos específicos de Chip
        mapChipFields(createDTO, chip);

        return chip;
    }

    public void updateEntity(ChipUpdateDTO updateDTO, Chip chip, Empresa empresa, Departamento departamento) {
        if (updateDTO == null || chip == null) {
            return;
        }

        // Atualizar campos comuns de Equipamento
        mapCommonFields(updateDTO, chip, empresa, departamento);

        // Atualizar campos específicos de Chip
        mapChipFields(updateDTO, chip);
    }

    private void mapCommonFields(Object dto, Chip chip, Empresa empresa, Departamento departamento) {
        if (dto instanceof ChipCreateDTO) {
            ChipCreateDTO createDTO = (ChipCreateDTO) dto;
            chip.setNumeroPatrimonio(createDTO.getNumeroPatrimonio());
            chip.setSerialNumber(createDTO.getSerialNumber());
            chip.setMarca(createDTO.getMarca());
            chip.setModelo(createDTO.getModelo());
            chip.setEstadoConservacao(createDTO.getEstadoConservacao());
            chip.setStatus(createDTO.getStatus());
            chip.setTermoResponsabilidade(createDTO.getTermoResponsabilidade());
            chip.setValor(createDTO.getValor());
            chip.setNotaFiscal(createDTO.getNotaFiscal());
            chip.setSiglaEstado(createDTO.getSiglaEstado());
            chip.setObservacoes(createDTO.getObservacoes());
        } else if (dto instanceof ChipUpdateDTO) {
            ChipUpdateDTO updateDTO = (ChipUpdateDTO) dto;
            chip.setNumeroPatrimonio(updateDTO.getNumeroPatrimonio());
            chip.setSerialNumber(updateDTO.getSerialNumber());
            chip.setMarca(updateDTO.getMarca());
            chip.setModelo(updateDTO.getModelo());
            chip.setEstadoConservacao(updateDTO.getEstadoConservacao());
            chip.setStatus(updateDTO.getStatus());
            chip.setTermoResponsabilidade(updateDTO.getTermoResponsabilidade());
            chip.setValor(updateDTO.getValor());
            chip.setNotaFiscal(updateDTO.getNotaFiscal());
            chip.setSiglaEstado(updateDTO.getSiglaEstado());
            chip.setObservacoes(updateDTO.getObservacoes());
        }

        chip.setEmpresa(empresa);
        chip.setDepartamento(departamento);
    }

    private void mapChipFields(Object dto, Chip chip) {
        if (dto instanceof ChipCreateDTO) {
            ChipCreateDTO createDTO = (ChipCreateDTO) dto;
            chip.setNumeroTelefone(createDTO.getNumeroTelefone());
            chip.setIccid(createDTO.getIccid());
            chip.setOperadora(createDTO.getOperadora());
            chip.setTipoPlano(createDTO.getTipoPlano());
        } else if (dto instanceof ChipUpdateDTO) {
            ChipUpdateDTO updateDTO = (ChipUpdateDTO) dto;
            chip.setNumeroTelefone(updateDTO.getNumeroTelefone());
            chip.setIccid(updateDTO.getIccid());
            chip.setOperadora(updateDTO.getOperadora());
            chip.setTipoPlano(updateDTO.getTipoPlano());
        }
    }
}
