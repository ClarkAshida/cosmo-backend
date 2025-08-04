package com.cosmo.cosmo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoMultiplaResponseDTO {

    private Integer totalItens;
    private Integer itensSucesso;
    private Integer itensErro;
    private List<HistoricoResponseDTO> historicosProcessados; // Históricos criados/atualizados com sucesso
    private List<String> erros; // Lista de erros encontrados durante o processamento
    private String observacaoGeral; // Observação sobre a operação como um todo
}
