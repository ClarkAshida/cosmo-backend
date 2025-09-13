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

import com.cosmo.cosmo.dto.equipamento.MonitorCreateDTO;
import com.cosmo.cosmo.dto.equipamento.MonitorUpdateDTO;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.entity.equipamento.Monitor;
import org.springframework.stereotype.Component;

@Component
public class MonitorMapper {

    public Monitor toEntity(MonitorCreateDTO createDTO, Empresa empresa, Departamento departamento) {
        if (createDTO == null) {
            return null;
        }

        Monitor monitor = new Monitor();

        // Mapear campos comuns de Equipamento
        mapCommonFields(createDTO, monitor, empresa, departamento);

        // Mapear campos específicos de Monitor
        mapMonitorFields(createDTO, monitor);

        return monitor;
    }

    public void updateEntity(MonitorUpdateDTO updateDTO, Monitor monitor, Empresa empresa, Departamento departamento) {
        if (updateDTO == null || monitor == null) {
            return;
        }

        // Atualizar campos comuns de Equipamento
        mapCommonFields(updateDTO, monitor, empresa, departamento);

        // Atualizar campos específicos de Monitor
        mapMonitorFields(updateDTO, monitor);
    }

    private void mapCommonFields(Object dto, Monitor monitor, Empresa empresa, Departamento departamento) {
        if (dto instanceof MonitorCreateDTO) {
            MonitorCreateDTO createDTO = (MonitorCreateDTO) dto;
            monitor.setNumeroPatrimonio(createDTO.getNumeroPatrimonio());
            monitor.setSerialNumber(createDTO.getSerialNumber());
            monitor.setMarca(createDTO.getMarca());
            monitor.setModelo(createDTO.getModelo());
            monitor.setEstadoConservacao(createDTO.getEstadoConservacao());
            monitor.setStatus(createDTO.getStatus());
            monitor.setTermoResponsabilidade(createDTO.getTermoResponsabilidade());
            monitor.setValor(createDTO.getValor());
            monitor.setNotaFiscal(createDTO.getNotaFiscal());
            monitor.setSiglaEstado(createDTO.getSiglaEstado());
            monitor.setObservacoes(createDTO.getObservacoes());
        } else if (dto instanceof MonitorUpdateDTO) {
            MonitorUpdateDTO updateDTO = (MonitorUpdateDTO) dto;
            monitor.setNumeroPatrimonio(updateDTO.getNumeroPatrimonio());
            monitor.setSerialNumber(updateDTO.getSerialNumber());
            monitor.setMarca(updateDTO.getMarca());
            monitor.setModelo(updateDTO.getModelo());
            monitor.setEstadoConservacao(updateDTO.getEstadoConservacao());
            monitor.setStatus(updateDTO.getStatus());
            monitor.setTermoResponsabilidade(updateDTO.getTermoResponsabilidade());
            monitor.setValor(updateDTO.getValor());
            monitor.setNotaFiscal(updateDTO.getNotaFiscal());
            monitor.setSiglaEstado(updateDTO.getSiglaEstado());
            monitor.setObservacoes(updateDTO.getObservacoes());
        }

        monitor.setEmpresa(empresa);
        monitor.setDepartamento(departamento);
    }

    private void mapMonitorFields(Object dto, Monitor monitor) {
        if (dto instanceof MonitorCreateDTO) {
            MonitorCreateDTO createDTO = (MonitorCreateDTO) dto;
            monitor.setTamanhoTela(createDTO.getTamanhoTela());
            monitor.setResolucao(createDTO.getResolucao());
        } else if (dto instanceof MonitorUpdateDTO) {
            MonitorUpdateDTO updateDTO = (MonitorUpdateDTO) dto;
            monitor.setTamanhoTela(updateDTO.getTamanhoTela());
            monitor.setResolucao(updateDTO.getResolucao());
        }
    }
}
