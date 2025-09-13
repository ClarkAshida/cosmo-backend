package com.cosmo.cosmo.dto.equipamento;

import com.cosmo.cosmo.dto.departamento.DepartamentoResponseDTO;
import com.cosmo.cosmo.dto.empresa.EmpresaResponseDTO;
import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.enums.StatusPropriedade;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EquipamentoResponseDTO extends RepresentationModel<EquipamentoResponseDTO> {

    private Long id;
    private String numeroPatrimonio;
    private String serialNumber;
    private String marca;
    private String modelo;
    private EstadoConservacao estadoConservacao;
    private StatusEquipamento status;
    private Boolean termoResponsabilidade;
    private Float valor;
    private String notaFiscal;
    private String observacoes;
    private StatusPropriedade statusPropriedade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campo para identificar o tipo de equipamento
    private String tipo;

    // Campo para conter os detalhes específicos do equipamento
    private Object details;

    // Objetos aninhados para HATEOAS (substituindo os campos redundantes)
    private DepartamentoResponseDTO departamento;
    private EmpresaResponseDTO empresa;
}
