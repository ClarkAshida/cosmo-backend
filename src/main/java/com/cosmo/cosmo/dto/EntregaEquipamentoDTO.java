package com.cosmo.cosmo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaEquipamentoDTO {

    @NotNull(message = "ID do equipamento é obrigatório")
    private Long equipamentoId;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    @Size(max = 1000, message = "Observações de entrega devem ter no máximo 1000 caracteres")
    private String observacoesEntrega;

    @Size(max = 255, message = "URL do termo de entrega deve ter no máximo 255 caracteres")
    private String urlTermoEntrega;
}
