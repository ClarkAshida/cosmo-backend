package com.cosmo.cosmo.dto.departamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DepartamentoResponseDTO extends RepresentationModel<DepartamentoResponseDTO> {

    private Long id;
    private String nome;
}
