package com.cosmo.cosmo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String cargo;
    private String cpf;
    private String cidade;
    private DepartamentoResponseDTO departamento;
    private EmpresaResponseDTO empresa;
    private Boolean ativo;
}
