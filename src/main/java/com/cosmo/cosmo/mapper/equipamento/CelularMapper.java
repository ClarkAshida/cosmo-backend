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

import com.cosmo.cosmo.dto.equipamento.CelularCreateDTO;
import com.cosmo.cosmo.dto.equipamento.CelularUpdateDTO;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.entity.equipamento.Celular;
import org.springframework.stereotype.Component;

@Component
public class CelularMapper {

    public Celular toEntity(CelularCreateDTO createDTO, Empresa empresa, Departamento departamento) {
        if (createDTO == null) {
            return null;
        }

        Celular celular = new Celular();

        // Mapear campos comuns de Equipamento
        mapCommonFields(createDTO, celular, empresa, departamento);

        // Mapear campos específicos de Celular
        mapCelularFields(createDTO, celular);

        return celular;
    }

    public void updateEntity(CelularUpdateDTO updateDTO, Celular celular, Empresa empresa, Departamento departamento) {
        if (updateDTO == null || celular == null) {
            return;
        }

        // Atualizar campos comuns de Equipamento
        mapCommonFields(updateDTO, celular, empresa, departamento);

        // Atualizar campos específicos de Celular
        mapCelularFields(updateDTO, celular);
    }

    private void mapCommonFields(Object dto, Celular celular, Empresa empresa, Departamento departamento) {
        if (dto instanceof CelularCreateDTO) {
            CelularCreateDTO createDTO = (CelularCreateDTO) dto;
            celular.setNumeroPatrimonio(createDTO.getNumeroPatrimonio());
            celular.setSerialNumber(createDTO.getSerialNumber());
            celular.setMarca(createDTO.getMarca());
            celular.setModelo(createDTO.getModelo());
            celular.setEstadoConservacao(createDTO.getEstadoConservacao());
            celular.setStatus(createDTO.getStatus());
            celular.setTermoResponsabilidade(createDTO.getTermoResponsabilidade());
            celular.setValor(createDTO.getValor());
            celular.setNotaFiscal(createDTO.getNotaFiscal());
            celular.setSiglaEstado(createDTO.getSiglaEstado());
            celular.setObservacoes(createDTO.getObservacoes());
        } else if (dto instanceof CelularUpdateDTO) {
            CelularUpdateDTO updateDTO = (CelularUpdateDTO) dto;
            celular.setNumeroPatrimonio(updateDTO.getNumeroPatrimonio());
            celular.setSerialNumber(updateDTO.getSerialNumber());
            celular.setMarca(updateDTO.getMarca());
            celular.setModelo(updateDTO.getModelo());
            celular.setEstadoConservacao(updateDTO.getEstadoConservacao());
            celular.setStatus(updateDTO.getStatus());
            celular.setTermoResponsabilidade(updateDTO.getTermoResponsabilidade());
            celular.setValor(updateDTO.getValor());
            celular.setNotaFiscal(updateDTO.getNotaFiscal());
            celular.setSiglaEstado(updateDTO.getSiglaEstado());
            celular.setObservacoes(updateDTO.getObservacoes());
        }

        celular.setEmpresa(empresa);
        celular.setDepartamento(departamento);
    }

    private void mapCelularFields(Object dto, Celular celular) {
        if (dto instanceof CelularCreateDTO) {
            CelularCreateDTO createDTO = (CelularCreateDTO) dto;
            celular.setImei(createDTO.getImei());
            celular.setImei2(createDTO.getImei2());
            celular.setEid(createDTO.getEid());
            celular.setGerenciadoPorMDM(createDTO.getGerenciadoPorMDM());
            celular.setMDM(createDTO.getMDM());
        } else if (dto instanceof CelularUpdateDTO) {
            CelularUpdateDTO updateDTO = (CelularUpdateDTO) dto;
            celular.setImei(updateDTO.getImei());
            celular.setImei2(updateDTO.getImei2());
            celular.setEid(updateDTO.getEid());
            celular.setGerenciadoPorMDM(updateDTO.getGerenciadoPorMDM());
            celular.setMDM(updateDTO.getMDM());
        }
    }
}
