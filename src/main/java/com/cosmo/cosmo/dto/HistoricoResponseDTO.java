package com.cosmo.cosmo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoResponseDTO {

    private Long id;

    // Dados do equipamento
    private EquipamentoResponseDTO equipamento;

    // Dados do usuário
    private UsuarioResponseDTO usuario;

    // Datas
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataEntrega;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataDevolucao;

    // Observações e documentos
    private String observacoesEntrega;
    private String observacoesDevolucao;
    private String urlTermoEntrega;
    private String urlTermoDevolucao;

    // Controle de status do registro (novos campos)
    private Boolean statusRegistroHistorico;
    private String motivoCancelamento;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCancelamento;
}
