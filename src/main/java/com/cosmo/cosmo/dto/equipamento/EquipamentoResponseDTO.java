package com.cosmo.cosmo.dto.equipamento;

import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EquipamentoResponseDTO {

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
    private Long empresaId;
    private String empresaNome;
    private String siglaEstado;
    private Long departamentoId;
    private String departamentoNome;
    private String observacoes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campo para identificar o tipo de equipamento
    private String tipo;

    // Campo para conter os detalhes específicos do equipamento
    private Object details;
}
