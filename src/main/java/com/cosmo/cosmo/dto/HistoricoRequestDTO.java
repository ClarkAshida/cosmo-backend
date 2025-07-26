package com.cosmo.cosmo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoRequestDTO {

    @NotNull(message = "Equipamento é obrigatório")
    private Long equipamentoId;

    @NotNull(message = "Usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "Data de entrega é obrigatória")
    private LocalDateTime dataEntrega;

    private LocalDateTime dataDevolucao;

    private String observacoesEntrega;

    private String observacoesDevolucao;

    private String urlTermoEntrega;

    private String urlTermoDevolucao;
}
