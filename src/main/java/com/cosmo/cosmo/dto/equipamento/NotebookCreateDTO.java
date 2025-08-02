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
public class NotebookCreateDTO {

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

    // Campos específicos de Computador
    private String sistemaOperacional;
    private String processador;
    private String memoriaRAM;
    private String armazenamento;
    private String hostname;
    private String dominio;
    private Boolean remoteAccessEnabled;
    private Boolean antivirusEnabled;
    private StatusPropriedade statusPropriedade;
}
