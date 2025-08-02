package com.cosmo.cosmo.dto.equipamento;

import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ImpressoraUpdateDTO {

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

    // Campos específicos de Impressora
    private String tipoImpressora;
    private Boolean colorida;
    private Boolean multifuncional;
    private String enderecoIP;
    private String modeloSuprimento;
}
