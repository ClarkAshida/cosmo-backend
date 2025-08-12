package com.cosmo.cosmo.dto.historico;

import com.cosmo.cosmo.enums.StatusEquipamento;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucaoEquipamentoDTO {

    @Size(max = 1000, message = "Observações de devolução devem ter no máximo 1000 caracteres")
    private String observacoesDevolucao;

    @Size(max = 255, message = "URL do termo de devolução deve ter no máximo 255 caracteres")
    private String urlTermoDevolucao;

    private StatusEquipamento novoStatus; // Se não informado, será DISPONIVEL por padrão
}
