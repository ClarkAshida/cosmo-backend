package com.cosmo.cosmo.dto.usuario;

import com.cosmo.cosmo.dto.departamento.DepartamentoResponseDTO;
import com.cosmo.cosmo.dto.empresa.EmpresaResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {

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
