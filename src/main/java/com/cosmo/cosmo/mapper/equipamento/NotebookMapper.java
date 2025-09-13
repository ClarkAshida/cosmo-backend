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

import com.cosmo.cosmo.dto.equipamento.NotebookCreateDTO;
import com.cosmo.cosmo.dto.equipamento.NotebookUpdateDTO;
import com.cosmo.cosmo.entity.Departamento;
import com.cosmo.cosmo.entity.Empresa;
import com.cosmo.cosmo.entity.equipamento.Notebook;
import org.springframework.stereotype.Component;

@Component
public class NotebookMapper {

    public Notebook toEntity(NotebookCreateDTO createDTO, Empresa empresa, Departamento departamento) {
        if (createDTO == null) {
            return null;
        }

        Notebook notebook = new Notebook();

        // Mapear campos comuns de Equipamento
        mapCommonFields(createDTO, notebook, empresa, departamento);

        // Mapear campos específicos de Computador
        mapComputadorFields(createDTO, notebook);

        return notebook;
    }

    public void updateEntity(NotebookUpdateDTO updateDTO, Notebook notebook, Empresa empresa, Departamento departamento) {
        if (updateDTO == null || notebook == null) {
            return;
        }

        // Atualizar campos comuns de Equipamento
        mapCommonFields(updateDTO, notebook, empresa, departamento);

        // Atualizar campos específicos de Computador
        mapComputadorFields(updateDTO, notebook);
    }

    private void mapCommonFields(Object dto, Notebook notebook, Empresa empresa, Departamento departamento) {
        if (dto instanceof NotebookCreateDTO) {
            NotebookCreateDTO createDTO = (NotebookCreateDTO) dto;
            notebook.setNumeroPatrimonio(createDTO.getNumeroPatrimonio());
            notebook.setSerialNumber(createDTO.getSerialNumber());
            notebook.setMarca(createDTO.getMarca());
            notebook.setModelo(createDTO.getModelo());
            notebook.setEstadoConservacao(createDTO.getEstadoConservacao());
            notebook.setStatus(createDTO.getStatus());
            notebook.setTermoResponsabilidade(createDTO.getTermoResponsabilidade());
            notebook.setValor(createDTO.getValor());
            notebook.setNotaFiscal(createDTO.getNotaFiscal());
            notebook.setSiglaEstado(createDTO.getSiglaEstado());
            notebook.setObservacoes(createDTO.getObservacoes());
        } else if (dto instanceof NotebookUpdateDTO) {
            NotebookUpdateDTO updateDTO = (NotebookUpdateDTO) dto;
            notebook.setNumeroPatrimonio(updateDTO.getNumeroPatrimonio());
            notebook.setSerialNumber(updateDTO.getSerialNumber());
            notebook.setMarca(updateDTO.getMarca());
            notebook.setModelo(updateDTO.getModelo());
            notebook.setEstadoConservacao(updateDTO.getEstadoConservacao());
            notebook.setStatus(updateDTO.getStatus());
            notebook.setTermoResponsabilidade(updateDTO.getTermoResponsabilidade());
            notebook.setValor(updateDTO.getValor());
            notebook.setNotaFiscal(updateDTO.getNotaFiscal());
            notebook.setSiglaEstado(updateDTO.getSiglaEstado());
            notebook.setObservacoes(updateDTO.getObservacoes());
        }

        notebook.setEmpresa(empresa);
        notebook.setDepartamento(departamento);
    }

    private void mapComputadorFields(Object dto, Notebook notebook) {
        if (dto instanceof NotebookCreateDTO) {
            NotebookCreateDTO createDTO = (NotebookCreateDTO) dto;
            notebook.setSistemaOperacional(createDTO.getSistemaOperacional());
            notebook.setProcessador(createDTO.getProcessador());
            notebook.setMemoriaRAM(createDTO.getMemoriaRAM());
            notebook.setArmazenamento(createDTO.getArmazenamento());
            notebook.setHostname(createDTO.getHostname());
            notebook.setDominio(createDTO.getDominio());
            notebook.setRemoteAccessEnabled(createDTO.getRemoteAccessEnabled());
            notebook.setAntivirusEnabled(createDTO.getAntivirusEnabled());
            notebook.setStatusPropriedade(createDTO.getStatusPropriedade());
        } else if (dto instanceof NotebookUpdateDTO) {
            NotebookUpdateDTO updateDTO = (NotebookUpdateDTO) dto;
            notebook.setSistemaOperacional(updateDTO.getSistemaOperacional());
            notebook.setProcessador(updateDTO.getProcessador());
            notebook.setMemoriaRAM(updateDTO.getMemoriaRAM());
            notebook.setArmazenamento(updateDTO.getArmazenamento());
            notebook.setHostname(updateDTO.getHostname());
            notebook.setDominio(updateDTO.getDominio());
            notebook.setRemoteAccessEnabled(updateDTO.getRemoteAccessEnabled());
            notebook.setAntivirusEnabled(updateDTO.getAntivirusEnabled());
            notebook.setStatusPropriedade(updateDTO.getStatusPropriedade());
        }
    }
}
