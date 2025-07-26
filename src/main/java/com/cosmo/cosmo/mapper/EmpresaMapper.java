package com.cosmo.cosmo.mapper;

import com.cosmo.cosmo.dto.EmpresaRequestDTO;
import com.cosmo.cosmo.dto.EmpresaResponseDTO;
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
