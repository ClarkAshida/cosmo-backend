package com.cosmo.cosmo.dto.equipamento;

import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChipCreateDTO {

    // Campos comuns de Equipamento
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
    private String siglaEstado;
    private Long departamentoId;
    private String observacoes;

    // Campos específicos de Chip
    private String numeroTelefone;
    private String iccid;
    private String operadora;
    private String tipoPlano;
}
