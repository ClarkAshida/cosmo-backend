package com.cosmo.cosmo.dto;

import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.enums.TipoEquipamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoResponseDTO {

    private Long id;

    // Identificadores únicos
    private String numeroPatrimonio;
    private String serialNumber;
    private String imei;
    private String eid;
    private String numeroTelefone;
    private String iccid;

    // Informações de identificação do equipamento
    private TipoEquipamento tipoEquipamento;
    private String marca;
    private String modelo;
    private EstadoConservacao estadoConservacao;
    private Boolean termoResponsabilidade;

    // Dados relacionados (apenas IDs e nomes para evitar referência circular)
    private EmpresaResponseDTO empresa;
    private String siglaEstado;
    private DepartamentoResponseDTO departamento;

    // Informações técnicas do equipamento
    private String sistemaOperacional;
    private String processador;
    private String armazenamento;
    private String hostname;
    private String dominio;

    // Configuração de segurança
    private Boolean remoteAccessEnabled;
    private Boolean antivirusEnabled;

    // Informações de compra
    private Float valor;
    private String notaFiscal;

    private String observacoes;
    private StatusEquipamento status;
}
