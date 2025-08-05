package com.cosmo.cosmo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmpresaResponseDTO extends RepresentationModel<EmpresaResponseDTO> {

    private Long id;
    private String nome;
    private String estado;
}
