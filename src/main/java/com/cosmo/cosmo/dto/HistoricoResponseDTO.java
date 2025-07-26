package com.cosmo.cosmo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoResponseDTO {

    private Long id;
    private EquipamentoResponseDTO equipamento;
    private UsuarioResponseDTO usuario;
    private LocalDateTime dataEntrega;
    private LocalDateTime dataDevolucao;
    private String observacoesEntrega;
    private String observacoesDevolucao;
    private String urlTermoEntrega;
    private String urlTermoDevolucao;
}
