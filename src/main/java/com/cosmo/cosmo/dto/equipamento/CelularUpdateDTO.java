package com.cosmo.cosmo.dto.equipamento;

import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.enums.StatusPropriedade;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CelularUpdateDTO {

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
    private StatusPropriedade statusPropriedade;

    // Campos específicos de Celular
    private String imei;
    private String imei2;
    private String eid;
    private Boolean gerenciadoPorMDM;
    private String MDM;
}
