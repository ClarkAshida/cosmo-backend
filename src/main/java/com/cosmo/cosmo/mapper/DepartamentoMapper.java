package com.cosmo.cosmo.mapper;

import com.cosmo.cosmo.dto.DepartamentoRequestDTO;
import com.cosmo.cosmo.dto.DepartamentoResponseDTO;
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
