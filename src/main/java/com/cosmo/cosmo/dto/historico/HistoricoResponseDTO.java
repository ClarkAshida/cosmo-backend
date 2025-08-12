package com.cosmo.cosmo.dto.historico;

import com.cosmo.cosmo.dto.equipamento.EquipamentoResponseDTO;
import com.cosmo.cosmo.dto.usuario.UsuarioResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HistoricoResponseDTO extends RepresentationModel<HistoricoResponseDTO> {

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
