package com.cosmo.cosmo.dto.historico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucaoMultiplaDTO {

    @NotEmpty(message = "Lista de itens para devolução não pode estar vazia")
    @Valid
    private List<ItemDevolucaoDTO> itensDevolvidos;

    @Size(max = 255, message = "URL do termo de devolução deve ter no máximo 255 caracteres")
    private String urlTermoDevolucao; // URL única para toda a transação
}
