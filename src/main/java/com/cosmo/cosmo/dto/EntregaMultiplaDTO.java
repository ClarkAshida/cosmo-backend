package com.cosmo.cosmo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaMultiplaDTO {

    @NotEmpty(message = "Lista de equipamentos não pode estar vazia")
    private List<Long> equipamentoIds;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    @Size(max = 1000, message = "Observações de entrega devem ter no máximo 1000 caracteres")
    private String observacoesEntrega;

    @Size(max = 255, message = "URL do termo de entrega deve ter no máximo 255 caracteres")
    private String urlTermoEntrega;
}
