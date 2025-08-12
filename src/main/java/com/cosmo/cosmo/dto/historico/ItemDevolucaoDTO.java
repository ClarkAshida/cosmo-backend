package com.cosmo.cosmo.dto.historico;

import com.cosmo.cosmo.enums.StatusEquipamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDevolucaoDTO {

    @NotNull(message = "ID do histórico é obrigatório")
    private Long historicoId;

    @Size(max = 1000, message = "Observações de devolução devem ter no máximo 1000 caracteres")
    private String observacoesDevolucao;

    private StatusEquipamento novoStatus; // Se não informado, será DISPONIVEL por padrão
}
