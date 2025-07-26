package com.cosmo.cosmo.dto;

import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.enums.TipoEquipamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoResponseDTO {

    private Long id;
    private String numeroPatrimonio;
    private String serialNumber;
    private String imei;

    // Propriedades específicas para equipamentos celular
    private String eid;
    private String numeroTelefone;
    private String iccid;

    private TipoEquipamento tipoEquipamento;
    private String marca;
    private String modelo;
    private EstadoConservacao estadoConservacao;
    private Boolean termoResponsabilidade;
    private EmpresaResponseDTO empresa;
    private String siglaEstado;
    private DepartamentoResponseDTO departamento;
    private String sistemaOperacional;
    private String processador;
    private String armazenamento;
    private String hostname;
    private String dominio;
    private Boolean remoteAccessEnabled;
    private Boolean antivirusEnabled;
    private Float valor;
    private String notaFiscal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String observacoes;
    private StatusEquipamento status;
}
