package com.cosmo.cosmo.dto;

import com.cosmo.cosmo.enums.EstadoConservacao;
import com.cosmo.cosmo.enums.StatusEquipamento;
import com.cosmo.cosmo.enums.TipoEquipamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoRequestDTO {

    @Size(max = 50, message = "Número do patrimônio deve ter no máximo 50 caracteres")
    private String numeroPatrimonio;

    @Size(max = 50, message = "Número de série deve ter no máximo 50 caracteres")
    private String serialNumber;

    @Size(max = 20, message = "IMEI deve ter no máximo 20 caracteres")
    private String imei;

    // Propriedades específicas para equipamentos celular
    @Size(min = 32, max = 32, message = "EID deve ter exatamente 32 dígitos")
    private String eid;

    @Size(max = 20, message = "Número de telefone deve ter no máximo 20 caracteres")
    private String numeroTelefone;

    @Size(max = 20, message = "ICCID deve ter no máximo 20 caracteres")
    private String iccid;

    @NotNull(message = "Tipo do equipamento é obrigatório")
    private TipoEquipamento tipoEquipamento;

    @Size(max = 50, message = "Marca deve ter no máximo 50 caracteres")
    private String marca;

    @Size(max = 50, message = "Modelo deve ter no máximo 50 caracteres")
    private String modelo;

    private EstadoConservacao estadoConservacao;

    private Boolean termoResponsabilidade = false;

    @NotNull(message = "Empresa é obrigatória")
    private Long empresaId;

    @Size(min = 2, max = 2, message = "Sigla do estado deve ter exatamente 2 caracteres")
    private String siglaEstado;

    @NotNull(message = "Departamento é obrigatório")
    private Long departamentoId;

    @Size(max = 50, message = "Sistema operacional deve ter no máximo 50 caracteres")
    private String sistemaOperacional;

    @Size(max = 100, message = "Processador deve ter no máximo 100 caracteres")
    private String processador;

    @Size(max = 50, message = "Armazenamento deve ter no máximo 50 caracteres")
    private String armazenamento;

    @Size(max = 50, message = "Hostname deve ter no máximo 50 caracteres")
    private String hostname;

    @Size(max = 100, message = "Domínio deve ter no máximo 100 caracteres")
    private String dominio;

    private Boolean remoteAccessEnabled = false;

    private Boolean antivirusEnabled = false;

    @Positive(message = "Valor deve ser positivo")
    private Float valor;

    @Size(max = 50, message = "Nota fiscal deve ter no máximo 50 caracteres")
    private String notaFiscal;

    private String observacoes;

    @NotNull(message = "Status é obrigatório")
    private StatusEquipamento status;
}
